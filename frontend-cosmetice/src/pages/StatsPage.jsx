import React, { useState, useEffect } from "react";
import { apiService } from "../api/backend";
import { useApp } from "../context/AppContext";
import { Card } from "../components/ui";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import "./StatsPage.css";

export default function StatsPage({ t }) {
  const { user } = useApp();
  const idMagazin = user?.idMagazin || 1;

  const [stats, setStats] = useState([]);
  const [productsData, setProductsData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
      Promise.all([
          apiService.getStats(),
          apiService.getProducts().then(async res => {
              const lista = res.data || [];
              return Promise.all(lista.map(async p => {
                  try {
                      const stocRes = await apiService.getProductStockDirect(p.id, idMagazin);
                      return { ...p, stoc: stocRes.data ?? 0 };
                  } catch {
                      return { ...p, stoc: 0 };
                  }
              }));
          })
      ])
      .then(([statsRes, produse]) => {
          setStats(statsRes.data || []);
          setProductsData(produse);
      })
      .finally(() => setLoading(false));
  }, [idMagazin]);

  const brandMap = {};
  productsData.forEach(p => {
    const brand = p.producator || "Necunoscut";
    brandMap[brand] = (brandMap[brand] || 0) + 1;
  });
  
  const dataBranduri = Object.keys(brandMap).map(key => ({
    nume: key,
    produse: brandMap[key]
  }));

  const dataPreturi = productsData.slice(0, 10).map(p => ({
    nume: p.denumire,
    achizitie: p.pretAchizitie || 0,
    vanzare: p.pretVanzare || 0
  }));

  const dataStocuri = productsData.map(p => ({
    nume: p.denumire,
    stoc: p.stoc
  }));

  if (loading) {
    return <div className="stats-loading">Se incarca graficele...</div>;
  }

  return (
    <div className="stats-container">
      <h2 className="stats-title">
        Analiza si Statistici Magazin
      </h2>

      <div className="charts-flex">
        
        <Card className="chart-card">
          <h3 className="chart-heading">
            1. Distributia Catalogului pe Producatori (Branduri)
          </h3>
          <ResponsiveContainer width="100%" height={800}>
            <BarChart data={dataBranduri} layout="vertical" margin={{ top: 5, right: 30, left: 100, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis type="number" />
              <YAxis type="category" dataKey="nume" width={90} />
              <Tooltip />
              <Legend />
              <Bar name="Numar Produse" dataKey="produse" fill="#3b82f6" radius={[0, 4, 4, 0]} barSize={30} />
            </BarChart>
          </ResponsiveContainer>
        </Card>

        <Card className="chart-card">
          <h3 className="chart-heading">
            2. Comparatie Preturi: Achizitie vs. Vanzare (Top 10 Produse)
          </h3>
          <ResponsiveContainer width="100%" height={800}>
            <BarChart data={dataPreturi} layout="vertical" margin={{ top: 5, right: 30, left: 120, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis type="number" />
              <YAxis type="category" dataKey="nume" width={110} />
              <Tooltip />
              <Legend />
              <Bar name="Pret Achizitie (RON)" dataKey="achizitie" fill="#f59e0b" radius={[0, 4, 4, 0]} />
              <Bar name="Pret Vanzare (RON)" dataKey="vanzare" fill="#10b981" radius={[0, 4, 4, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </Card>

        <Card className="chart-card">
          <h3 className="chart-heading">
            3. Situatia Stocurilor pe Produse
          </h3>
          <ResponsiveContainer width="100%" height={1400}>
            <BarChart data={dataStocuri} layout="vertical" margin={{ top: 5, right: 30, left: 120, bottom: 5 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis type="number" />
            <YAxis type="category" dataKey="nume" width={110} />
            <Tooltip />
            <Legend />
            <Bar name="Bucati in Stoc" dataKey="stoc" fill="#ec4899" radius={[0, 4, 4, 0]} barSize={25} />
          </BarChart>
          </ResponsiveContainer>
        </Card>

      </div>
    </div>
  );
}