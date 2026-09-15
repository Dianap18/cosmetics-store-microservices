import React, { useState, useEffect, useCallback } from "react";
import { apiService } from "../api/backend";
import { Card, Button, Input, Modal, Badge, Select } from "../components/ui";
import "./ManagerPage.css";

export default function ManagerPage({ t }) {
  const [products, setProducts] = useState([]);
  const [stores, setStores] = useState([]);
  const [editModal, setEditModal] = useState(null);
  const [form, setForm] = useState({});
  const [toast, setToast] = useState("");

  const [search, setSearch] = useState("");
  const [selectedStore, setSelectedStore] = useState("");
  const [filterProducer, setFilterProducer] = useState("");
  const [filterAvail, setFilterAvail] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");

  const showToast = (msg) => { 
    setToast(msg); 
    setTimeout(() => setToast(""), 2500); 
  };

  const loadData = useCallback(() => {
    Promise.all([
      apiService.getProducts(),
      apiService.getAllStores().catch(() => ({ data: [] }))
    ])
    .then(async ([prodRes, storesRes]) => {
      const listaProduse = prodRes.data || [];
      setStores(storesRes.data || []);

      const produseCuStoc = await Promise.all(
        listaProduse.map(async (p) => {
          if (selectedStore) {
            try {
              const stocRes = await apiService.getProductStockDirect(p.id, selectedStore);
              return { ...p, stoc: stocRes.data !== undefined ? stocRes.data : 0 };
            } catch {
              return { ...p, stoc: 0 };
            }
          } else {
            try {
              const globalRes = await apiService.getMagazineCuStoc(p.id);
              return { ...p, stoc: globalRes.data?.length > 0 ? "Disponibil" : 0 };
            } catch {
              return { ...p, stoc: 0 };
            }
          }
        })
      );
      setProducts(produseCuStoc);
    })
    .catch(() => setProducts([]));
  }, [selectedStore]);

  useEffect(() => { 
    loadData(); 
  }, [loadData]);

  const producers = [...new Set(products.map(p => p.producator).filter(Boolean))];

  const filteredProducts = products
    .filter(p => p.denumire?.toLowerCase().includes(search.toLowerCase()))
    .filter(p => filterProducer ? p.producator === filterProducer : true)
    .filter(p => {
      if (!filterAvail) return true;
      const areStoc = p.stoc === "Disponibil" || parseInt(p.stoc) > 0;
      return filterAvail === "available" ? areStoc : !areStoc;
    })
    .filter(p => minPrice === "" || p.pretVanzare >= parseFloat(minPrice))
    .filter(p => maxPrice === "" || p.pretVanzare <= parseFloat(maxPrice));

  const openEdit = (product) => {
    setForm(product ? { ...product } : { denumire: "", producator: "", pretAchizitie: "", pretVanzare: "", imagini: [] });
    setEditModal(product ? product.id : "new");
  };

  const handleSave = () => {
    const dataToSend = {
      denumire: form.denumire,
      producator: form.producator,
      pretAchizitie: parseFloat(form.pretAchizitie),
      pretVanzare: parseFloat(form.pretVanzare),
      imagini: form.imagini || []
    };

    if (editModal === "new") {
      apiService.addProduct(dataToSend).then(() => {
        loadData();
        setEditModal(null);
        showToast(t.common.success);
      });
    } else {
      apiService.updateProduct(editModal, dataToSend).then(() => {
        loadData();
        setEditModal(null);
        showToast(t.common.success);
      });
    }
  };

  const handleDelete = (id) => {
    apiService.deleteProduct(id).then(() => {
      loadData();
      showToast(t.common.success);
    });
  };

  const handleExport = (format) => {
    apiService.exportReport(format).then(response => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `raport-manager.${format}`);
      document.body.appendChild(link);
      link.click();
    });
  };

  return (
    <div className="manager-container">
      {toast && <div className="toast-notification">{toast}</div>}

      <div className="manager-header">
        <h2 className="manager-title">{t.nav.dashboard} — Manager</h2>
        <Button onClick={() => openEdit(null)}>{t.products.add}</Button>
      </div>

      <Card className="manager-card">
        <div className="filters-row">
          <div className="input-search-width">
            <Input label={t.products.search} value={search} onChange={setSearch} placeholder="Nume produs..." />
          </div>
          <Select label="Magazin" value={selectedStore} onChange={setSelectedStore}
            options={[{ value: "", label: "Toate (Global)" }, ...stores.map(s => ({ value: s.id, label: s.nume }))]} />
          <Select label="Producător" value={filterProducer} onChange={setFilterProducer}
            options={[{ value: "", label: "Toți" }, ...producers.map(p => ({ value: p, label: p }))]} />
          <Select label="Disponibilitate" value={filterAvail} onChange={setFilterAvail}
            options={[{ value: "", label: "Toate" }, { value: "available", label: "În Stoc" }, { value: "unavailable", label: "Fără Stoc" }]} />
          <div className="input-price-width">
            <Input type="number" placeholder="Preț Min" value={minPrice} onChange={setMinPrice} />
          </div>
          <div className="input-price-width">
            <Input type="number" placeholder="Preț Max" value={maxPrice} onChange={setMaxPrice} />
          </div>
        </div>

        <div className="export-row">
          <span className="export-label">{t.products.export}:</span>
          {["csv", "json", "xml", "doc"].map(f => (
            <Button key={f} size="sm" variant="secondary" onClick={() => handleExport(f)}>{f.toUpperCase()}</Button>
          ))}
        </div>
      </Card>

      <div className="table-wrapper">
        <table className="manager-table">
          <thead>
            <tr className="table-header-row">
              <th className="table-th">Imagine</th>
              {[t.products.name, t.products.producer, t.products.buyPrice, t.products.sellPrice, t.products.stock, t.common.actions].map(h => (
                <th key={h} className="table-th">{h}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {filteredProducts.map(p => (
              <tr key={p.id} className="table-tr">
                <td className="table-td">
                  {p.imagini && p.imagini.length > 0 ? (
                    <img src={p.imagini[0]} alt="" className="product-thumbnail" />
                  ) : (
                    <div className="product-thumbnail-placeholder"></div>
                  )}
                </td>
                <td className="table-td-text">{p.denumire}</td>
                <td className="table-td-muted">{p.producator}</td>
                <td className="table-td-text">{p.pretAchizitie?.toFixed(2)} RON</td>
                <td className="table-td-price">{p.pretVanzare?.toFixed(2)} RON</td>
                <td className="table-td">
                  <Badge color={p.stoc !== 0 ? "green" : "red"}>{p.stoc || 0}</Badge>
                </td>
                <td className="table-td">
                  <div className="actions-wrapper">
                    <Button size="sm" variant="ghost" onClick={() => openEdit(p)}>{t.products.edit}</Button>
                    <Button size="sm" variant="danger" onClick={() => handleDelete(p.id)}>{t.products.delete}</Button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal open={!!editModal} onClose={() => setEditModal(null)} title={editModal === "new" ? t.products.add : t.products.edit}>
        <div className="modal-form">
          <Input label={t.products.name} value={form.denumire || ""} onChange={v => setForm(f => ({ ...f, denumire: v }))} />
          <Input label={t.products.producer} value={form.producator || ""} onChange={v => setForm(f => ({ ...f, producator: v }))} />
          <Input label={t.products.buyPrice} value={form.pretAchizitie || ""} onChange={v => setForm(f => ({ ...f, pretAchizitie: v }))} type="number" />
          <Input label={t.products.sellPrice} value={form.pretVanzare || ""} onChange={v => setForm(f => ({ ...f, pretVanzare: v }))} type="number" />
          <Input label="Link Imagine (URL)" value={form.imagini?.[0] || ""} onChange={v => setForm(f => ({ ...f, imagini: [v] }))} />
          <div className="modal-actions">
            <Button onClick={handleSave}>{t.common.save}</Button>
            <Button variant="secondary" onClick={() => setEditModal(null)}>{t.common.cancel}</Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}