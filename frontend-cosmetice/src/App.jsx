import React, { useState } from "react";
import { AppContext } from "./context/AppContext";
import { translations } from "./i18n/translations";
import Navbar from "./components/Navbar";
import CatalogPage from "./pages/CatalogPage";
import AngajatPage from "./pages/AngajatPage";
import ManagerPage from "./pages/ManagerPage";
import StatsPage from "./pages/StatsPage";
import AdminPage from "./pages/AdminPage";
import LoginPage from "./pages/LoginPage";

export default function App() {
  const [lang, setLang] = useState("ro");
  const [user, setUser] = useState(null);
  const [page, setPage] = useState("catalog");

  const t = translations[lang];

  const renderPage = () => {
    switch (page) {
      case "login":    return <LoginPage t={t} />;
      case "catalog":  return <CatalogPage t={t} role={user?.rol} />;
      case "angajat":  return <AngajatPage t={t} />;
      case "manager":  return <ManagerPage t={t} />;
      case "stats":    return <StatsPage t={t} />;
      case "admin":    return <AdminPage t={t} />;
      default:         return <CatalogPage t={t} role={user?.rol} />;
    }
  };

  return (
    <AppContext.Provider value={{ user, setUser, lang, setLang, page, setPage }}>
      <div style={{ minHeight: "100vh", background: "#fafafa", fontFamily: "system-ui, sans-serif" }}>
        <Navbar t={t} />
        {renderPage()}
      </div>
    </AppContext.Provider>
  );
}