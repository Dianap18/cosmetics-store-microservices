import React from "react";
import { useApp } from "../context/AppContext";
import { Badge, Button } from "./ui";
import "./Navbar.css";

export default function Navbar({ t }) {
  const { user, setUser, lang, setLang, setPage, page } = useApp();

  const navItems = () => {
    if (!user) return [{ key: "catalog", label: t.nav.products }, { key: "login", label: t.nav.login }];
    if (user.rol === "ANGAJAT") return [{ key: "angajat", label: t.nav.dashboard }];
    if (user.rol === "MANAGER") return [{ key: "catalog", label: t.nav.products }, { key: "manager", label: t.nav.dashboard }, { key: "stats", label: t.nav.stats }];
    if (user.rol === "ADMINISTRATOR") return [{ key: "admin", label: t.nav.users }];
    return [{ key: "catalog", label: t.nav.products }];
  };

  return (
    <nav className="navbar">
      <div className="nav-left">
        <span className="nav-logo">✦ Cosmetice</span>
        <div className="nav-links">
          {navItems().map(item => (
            <button 
              key={item.key} 
              onClick={() => setPage(item.key)}
              className={`nav-btn ${page === item.key ? "active" : ""}`}
            >
              {item.label}
            </button>
          ))}
        </div>
      </div>

      <div className="nav-right">
        <div className="lang-switcher">
          {["ro", "en", "fr"].map(l => (
            <button 
              key={l} 
              onClick={() => setLang(l)}
              className={`lang-btn ${lang === l ? "active" : ""}`}
            >
              {l}
            </button>
          ))}
        </div>

        {user && (
          <div className="user-section">
            <Badge color={user.rol === "ADMINISTRATOR" ? "purple" : user.rol === "MANAGER" ? "blue" : user.rol === "ANGAJAT" ? "green" : "gray"}>
              {user.rol}
            </Badge>
            <Button size="sm" variant="ghost" onClick={() => { setUser(null); setPage("catalog"); }}>
              {t.nav.logout}
            </Button>
          </div>
        )}
      </div>
    </nav>
  );
}