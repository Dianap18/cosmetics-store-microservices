import React, { useState } from "react";
import { useApp } from "../context/AppContext";
import { apiService } from "../api/backend";
import { Card, Input, Button } from "../components/ui";
import "./LoginPage.css";

export default function LoginPage({ t }) {
  const { setUser, setPage } = useApp();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = () => {
    apiService.login(email, password)
      .then(res => {
        const u = res.data;
        setUser(u);
        if (u.rol === "ANGAJAT") setPage("angajat");
          else if (u.rol === "MANAGER") setPage("manager");
          else if (u.rol === "ADMIN") setPage("admin");
          else setPage("catalog");
      })
      .catch(() => {
        setError("Email sau parola incorecta.");
      });
  };

  const handleEmailChange = (val) => {
    setEmail(val);
    if (error) setError("");
  };

  const handlePasswordChange = (val) => {
    setPassword(val);
    if (error) setError("");
  };

  return (
    <div className="login-container">
      <Card>
        <div className="login-header">
          <div className="login-logo">✦</div>
          <h2 className="login-title">{t.auth.loginTitle}</h2>
          <p className="login-subtitle">{t.auth.subtitle}</p>
        </div>
        <div className="login-form">
          <Input 
            label={t.auth.email} 
            value={email} 
            onChange={handleEmailChange} 
            type="email" 
            placeholder="email@exemplu.ro" 
          />
          <Input 
            label={t.auth.password} 
            value={password} 
            onChange={handlePasswordChange} 
            type="password" 
            placeholder="*****" 
          />
          {error && <div className="login-error">{error}</div>}
          <Button onClick={handleLogin}>{t.auth.loginBtn}</Button>
        </div>
      </Card>
    </div>
  );
}