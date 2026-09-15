import React, { useState, useEffect, useCallback } from "react";
import { apiService } from "../api/backend";
import { useApp } from "../context/AppContext";
import { Card, Input, Button, Select, Modal } from "../components/ui";
import ProductCard from "../components/ProductCard";
import "./AngajatPage.css";

export default function AngajatPage({ t }) {
  const { user } = useApp();
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [sort, setSort] = useState("name");
  const [filterProducer, setFilterProducer] = useState("");
  const [filterAvail, setFilterAvail] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");
  const [searchResult, setSearchResult] = useState(null);
  
  const [sellModal, setSellModal] = useState(null);
  const [sellQuantity, setSellQuantity] = useState(1);
  
  const [stockModal, setStockModal] = useState(null);
  const [newStock, setNewStock] = useState("");
  const [toast, setToast] = useState("");
  const [magazine, setMagazine] = useState([]);

  const idMagazin = user?.idMagazin || 1;

  const showToast = (msg) => { 
    setToast(msg); 
    setTimeout(() => setToast(""), 2500); 
  };

  const loadProducts = useCallback(() => {
    apiService.getProducts().then(res => {
      const lista = res.data || [];
      return Promise.all(
        lista.map(async (p) => {
          try {
            const stocRes = await apiService.getProductStockDirect(p.id, idMagazin);
            return { ...p, stoc: stocRes.data !== undefined ? stocRes.data : 0 };
          } catch {
            return { ...p, stoc: 0 };
          }
        })
      );
    })
    .then(produseCuStocuri => setProducts(produseCuStocuri))
    .catch(() => setProducts([]));
  }, [idMagazin]);

  useEffect(() => { 
    loadProducts(); 
  }, [loadProducts]);

  useEffect(() => {
    apiService.getAllStores()
        .then(res => {
            setMagazine(res.data || []);
        })
        .catch(err => console.log("EROARE MAGAZINE:", err));
  }, []);

  const producers = [...new Set(products.map(p => p.producator))];

  const filtered = products
    .filter(p => p.denumire.toLowerCase().includes(search.toLowerCase()))
    .filter(p => filterProducer ? p.producator === filterProducer : true)
    .filter(p => filterAvail === "available" ? p.stoc > 0 : filterAvail === "unavailable" ? p.stoc === 0 : true)
    .filter(p => minPrice === "" || p.pretVanzare >= parseFloat(minPrice))
    .filter(p => maxPrice === "" || p.pretVanzare <= parseFloat(maxPrice))
    .sort((a, b) => sort === "name" ? a.denumire.localeCompare(b.denumire) : (a.pretVanzare || 0) - (b.pretVanzare || 0));

  const handleSearch = () => {
    if (!search) return;
    apiService.searchAngajat(search, idMagazin)
        .then(res => {
          const data = res.data;
            if (!data || !data.denumire) {
                setSearchResult({ notFound: true });
                return;
            }
            if (data.stocLocal > 0) {
                setSearchResult({ local: true, product: data });
            } else {
                const numeMagazine = (data.disponibilInAlteMagazine || []).map(id => {
                    const mag = magazine.find(m => Number(m.id) === Number(id));
                    return mag ? mag.nume : `Magazin ID:${id}`;
                });
                setSearchResult({ 
                    local: false, 
                    stores: numeMagazine.length > 0 ? numeMagazine : [] 
                });
            }
        })
        .catch(() => setSearchResult({ notFound: true }));
  };

  const handleSearchChange = (val) => {
    setSearch(val);
    if (searchResult) setSearchResult(null);
  };

  const handleSell = () => {
    const qty = parseInt(sellQuantity, 10);
    
    if (isNaN(qty) || qty <= 0) {
        showToast(t.common.error);
        return;
    }

    apiService.sellProduct(idMagazin, sellModal.id, qty).then(() => {
      loadProducts();
      setSellModal(null);
      showToast(t.common.success);
    }).catch(() => showToast(t.common.error));
  };

  const handleUpdateStock = () => {
      if (!newStock || isNaN(newStock)) return;
      if (parseInt(newStock) < 0) {
          showToast("Stocul nu poate fi negativ.");
          return;
      }
      apiService.updateStock(idMagazin, stockModal.id, parseInt(newStock)).then(() => {
          loadProducts();
          setStockModal(null);
          setNewStock("");
          showToast(t.common.success);
      }).catch(() => showToast(t.common.error));
  };
  
  const handleExport = (format) => {
    apiService.exportReport(format).then(response => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `raport.${format}`);
      document.body.appendChild(link);
      link.click();
    });
  };

  return (
    <div className="dashboard-container">
      {toast && <div className="toast-notification">{toast}</div>}

      <h2 className="dashboard-title">{t.nav.dashboard}</h2>

      <Card className="dashboard-card">
        <div className="search-row">
          <div className="search-input-wrapper">
            <Input label={t.products.search} value={search} onChange={handleSearchChange} placeholder={t.products.search} />
          </div>
          <Button onClick={handleSearch}>{t.common.search}</Button>
        </div>

        <div className="filters-row">
          <Select label="" value={sort} onChange={setSort}
            options={[{ value: "name", label: t.products.sortName }, { value: "price", label: t.products.sortPrice }]} />
          
          <Select label="" value={filterProducer} onChange={setFilterProducer}
            options={[{ value: "", label: t.products.producer }, ...producers.map(p => ({ value: p, label: p }))]} />
            
          <Select label="" value={filterAvail} onChange={setFilterAvail}
            options={[{ value: "", label: t.products.availability }, { value: "available", label: t.products.available }, { value: "unavailable", label: t.products.unavailable }]} />
            
          <div className="price-input-wrapper">
            <Input type="number" placeholder="Pret min" value={minPrice} onChange={setMinPrice} />
          </div>
          <div className="price-input-wrapper">
            <Input type="number" placeholder="Pret max" value={maxPrice} onChange={setMaxPrice} />
          </div>
        </div>

        {searchResult && (
          <div className={`search-result-box ${searchResult.local ? "local" : "external"}`}>
            {searchResult.local && (
              <div className="result-text-local">
                <b>{searchResult.product.denumire}</b> — {t.products.available} | {t.products.stock}: {searchResult.product.cantitateLocala}
              </div>
            )}
            
            {!searchResult.local && !searchResult.notFound && (
              <div className="result-text-external">
                {searchResult.stores && searchResult.stores.length > 0 
                  ? `${t.products.notAvailableHere} ${searchResult.stores.join(", ")}` 
                  : "Acest produs nu este disponibil in niciun magazin."}
              </div>
            )}
            
            {searchResult.notFound && <div className="result-text-not-found">{t.common.noResults}</div>}
          </div>
        )}

        <div className="export-row">
          <span className="export-label">{t.products.export}:</span>
          {["csv", "json", "xml", "doc"].map(f => (
            <Button key={f} size="sm" variant="secondary" onClick={() => handleExport(f)}>{f.toUpperCase()}</Button>
          ))}
        </div>
      </Card>

      <div className="products-grid">
        {filtered.map(p => (
          <ProductCard key={p.id} product={p} role="ANGAJAT" t={t}
            onSell={(prod) => { 
              setSellModal(prod); 
              setSellQuantity(1); 
            }}
            onUpdateStock={(prod) => { setStockModal(prod); setNewStock(String(prod.stoc || 0)); }}
          />
        ))}
      </div>

      <Modal open={!!sellModal} onClose={() => setSellModal(null)} title={t.products.sell}>
        {sellModal && (
          <div className="modal-body">
            <p>Vânzare produs: <b>{sellModal.denumire}</b></p>
            <p style={{ fontSize: '0.9em', color: 'gray' }}>Stoc disponibil: {sellModal.stoc}</p>
            
            <div style={{ marginTop: '15px', marginBottom: '15px' }}>
              <Input 
                label="Cantitate de vândut" 
                value={sellQuantity} 
                onChange={setSellQuantity} 
                type="number"
                min="1"
                max={sellModal.stoc}
              />
            </div>

            <div className="modal-actions">
              <Button onClick={handleSell}>{t.common.yes}</Button>
              <Button variant="secondary" onClick={() => setSellModal(null)}>{t.common.no}</Button>
            </div>
          </div>
        )}
      </Modal>

      <Modal open={!!stockModal} onClose={() => setStockModal(null)} title={t.products.updateStock}>
        {stockModal && (
          <div className="modal-body">
            <Input label={t.products.stock} value={newStock} onChange={setNewStock} type="number" />
            <div className="modal-actions">
              <Button onClick={handleUpdateStock}>{t.common.save}</Button>
              <Button variant="secondary" onClick={() => setStockModal(null)}>{t.common.cancel}</Button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}