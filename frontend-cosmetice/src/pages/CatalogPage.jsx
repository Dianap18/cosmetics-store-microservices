import React, { useState, useEffect, useCallback } from "react";
import { useApp } from "../context/AppContext";
import { apiService } from "../api/backend";
import { Card, Input, Button, Select } from "../components/ui";
import ProductCard from "../components/ProductCard";
import "./CatalogPage.css";

export default function CatalogPage({ t, role }) {
  const { user } = useApp();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [sort, setSort] = useState("name");
  const [searchResult, setSearchResult] = useState(null);

  const incarcaDatele = useCallback(() => {
    setLoading(true);
    Promise.all([
      apiService.getProducts(),
      apiService.getAllStores().catch(() => ({ data: [] }))
    ])
    .then(async ([prodRes, storesRes]) => {
      const listaProduse = prodRes.data || [];
      const listaMagazine = storesRes.data || [];

      const mapMagazine = {};
      listaMagazine.forEach(m => {
        mapMagazine[m.id] = m.nume;
      });

      const produseCuStocuri = await Promise.all(
        listaProduse.map(async (p) => {
          try {
            if (role === "ANGAJAT" && user && user.idMagazin) {
              const stocRes = await apiService.getProductStockDirect(p.id, user.idMagazin);
              const cantitateLocala = stocRes.data !== undefined ? stocRes.data : 0;
              return { ...p, stoc: cantitateLocala, magazine: [mapMagazine[user.idMagazin]] };
            } else {
              const stocRes = await apiService.getMagazineCuStoc(p.id);
              const ids = stocRes.data || [];
              const numeMagazine = ids.map(id => mapMagazine[id] || `Magazin ID:${id}`);
              return { ...p, stoc: ids.length > 0 ? 1 : 0, magazine: numeMagazine };
            }
          } catch {
            return { ...p, stoc: 0, magazine: [] };
          }
        })
      );
      
      setProducts(produseCuStocuri);
    })
    .catch(() => setProducts([]))
    .finally(() => setLoading(false));
  }, [role, user]);

  useEffect(() => {
    incarcaDatele();
  }, [incarcaDatele]);

  const handleSell = (product) => {
    if (!user || !user.idMagazin) return;
    apiService.sellProduct(user.idMagazin, product.id, 1)
      .then(() => {
        alert("Produs vândut cu succes!");
        incarcaDatele();
      })
      .catch((err) => {
        alert(err.response?.data || "Eroare la vânzare. Stoc insuficient.");
      });
  };

  const handleUpdateStock = (product) => {
    if (!user || !user.idMagazin) return;
    const cantitateNoua = prompt(`Introduceți noul stoc pentru ${product.denumire}:`, product.stoc);
    if (cantitateNoua === null || cantitateNoua === "") return;

    const cantitateNumar = parseInt(cantitateNoua, 10);
    if (isNaN(cantitateNumar) || cantitateNumar < 0) {
      alert("Introduceți un număr valid și pozitiv.");
      return;
    }

    apiService.updateStock(user.idMagazin, product.id, cantitateNumar)
      .then(() => {
        alert("Stoc actualizat cu succes!");
        incarcaDatele();
      })
      .catch(() => {
        alert("Eroare la actualizarea stocului.");
      });
  };

  const handleSearch = () => {
    if (!search) return;
    apiService.searchClient(search)
      .then(res => {
        const data = res.data;
        if (data && data.disponibilInMagazinele && data.disponibilInMagazinele.length > 0) {
          apiService.getAllStores().then(storesRes => {
            const lista = storesRes.data || [];
            const numeMagazine = data.disponibilInMagazinele.map(id => {
              const gasit = lista.find(m => m.id === id);
              return gasit ? gasit.nume : `Magazin ID:${id}`;
            });
            setSearchResult({ found: true, stores: numeMagazine });
          });
        } else {
          setSearchResult({ found: false });
        }
      })
      .catch(() => setSearchResult({ found: false }));
  };

  const filtered = products
    .filter(p => p.denumire.toLowerCase().includes(search.toLowerCase()))
    .sort((a, b) => sort === "name" ? a.denumire.localeCompare(b.denumire) : (a.pretVanzare || 0) - (b.pretVanzare || 0));

  if (loading) return <div className="loading-box">{t.common.loading}</div>;

  return (
    <div className="catalog-container">
      <h2 className="catalog-title">{t.products.title}</h2>

      <Card className="catalog-filter-card">
        <div className="filter-actions-box">
          <div className="search-input-wrapper">
            <Input label={t.products.search} value={search} onChange={setSearch} placeholder={t.products.search} />
          </div>
          <Button onClick={handleSearch}>{t.common.search}</Button>
          <Select label={`${t.products.sortName} / ${t.products.sortPrice}`} value={sort} onChange={setSort}
            options={[{ value: "name", label: t.products.sortName }, { value: "price", label: t.products.sortPrice }]} />
        </div>

        {searchResult && (
          <div className={`search-result ${searchResult.found ? "found" : "not-found"}`}>
            {searchResult.found
              ? <span className="search-text-found">{t.products.availableIn} <b>{searchResult.stores.join(", ")}</b></span>
              : <span className="search-text-not-found">{t.common.noResults}</span>}
          </div>
        )}
      </Card>

      <div className="products-grid">
        {filtered.map(p => (
          <ProductCard 
            key={p.id} 
            product={p} 
            role="CLIENT"
            t={t} 
            onSell={handleSell}
            onUpdateStock={handleUpdateStock}
        />
        ))}
      </div>
    </div>
  );
}