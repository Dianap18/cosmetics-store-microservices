import React, { useState, useEffect } from "react";
import { apiService } from "../api/backend";
import { useApp } from "../context/AppContext";
import { Card, Button, Input, Select, Modal, Badge } from "../components/ui";
import "./AdminPage.css";

export default function AdminPage({ t }) {
  const [users, setUsers] = useState([]);
  const [filterRole, setFilterRole] = useState("");
  const [editModal, setEditModal] = useState(null);
  const [form, setForm] = useState({});
  const [toast, setToast] = useState("");

  const showToast = (msg) => { 
    setToast(msg); 
    setTimeout(() => setToast(""), 3500); 
  };

  const loadUsers = () => {
    apiService.getUsers()
      .then(res => setUsers(res.data || []))
      .catch(() => setUsers([]));
  };

  useEffect(() => { 
    loadUsers(); 
  }, []);

  const filtered = users.filter(u => filterRole ? u.rol === filterRole : true);

  const openEdit = (user) => {
    const userId = user ? (user.id?.id || user.id) : "new"; 
    setForm(user ? { ...user, parola: "" } : { username: "", email: "", telefon: "", rol: "CLIENT", parola: "", idMagazin: 0 });
    setEditModal(userId);
  };

  const handleSave = () => {
    const credentialsChanged = form.parola && form.parola.trim() !== "";

    if (editModal === "new") {
      apiService.addUser(form).then(() => {
        loadUsers();
        setEditModal(null);
        showToast(t.common.success);
      }).catch(() => showToast("Eroare la crearea utilizatorului!"));
    } else {
      apiService.updateUser(editModal, form).then(() => {
        loadUsers();
        setEditModal(null);
        if (credentialsChanged) {
          showToast(`${t.common.success}. Utilizatorul a fost notificat automat prin Email si SMS.`);
        } else {
          showToast(t.common.success);
        }
      }).catch((err) => {
        console.error(err);
        showToast("Eroare! Backend-ul a refuzat actualizarea.");
      });
    }
  };

  const handleDelete = (id) => {
    apiService.deleteUser(id).then(() => {
      loadUsers();
      showToast(t.common.success);
    }).catch(() => showToast("Eroare la stergere!"));
  };

  const handleExportCSV = () => {
      apiService.exportUsers().then(response => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const a = document.createElement("a");
          a.href = url;
          a.download = "utilizatori.csv";
          a.click();
          URL.revokeObjectURL(url);
      }).catch(() => showToast("Eroare la export!"));
  };

  const roleColor = { CLIENT: "gray", ANGAJAT: "green", MANAGER: "blue", ADMIN: "purple" };

  return (
    <div className="admin-container">
      {toast && <div className="toast-notification">{toast}</div>}

      <div className="admin-header">
        <h2 className="admin-title">{t.users.title}</h2>
        <div className="header-actions">
          <Button size="sm" variant="secondary" onClick={handleExportCSV}>{t.users.exportCSV}</Button>
          <Button onClick={() => openEdit(null)}>{t.users.add}</Button>
        </div>
      </div>

      <Card className="admin-card">
        <Select label={t.users.filterByRole} value={filterRole} onChange={setFilterRole}
          options={[{ value: "", label: t.users.allRoles || "Toate rolurile" }, { value: "CLIENT", label: "Client" }, { value: "ANGAJAT", label: "Angajat" }, { value: "MANAGER", label: "Manager" }, { value: "ADMIN", label: "Administrator" }]} />
      </Card>

      <div className="table-wrapper">
        <table className="admin-table">
          <thead>
            <tr className="table-header-row">
              {[t.users.name, t.users.email, "Telefon", t.users.role, t.common.actions].map(h => (
                <th key={h} className="table-th">{h}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {filtered.map(u => (
              <tr key={u.id?.id || u.id} className="table-tr">
                <td className="table-td-name">{u.username}</td>
                <td className="table-td-muted">{u.email}</td>
                <td className="table-td-muted">{u.telefon || "-"}</td>
                <td className="table-td"><Badge color={roleColor[u.rol] || "gray"}>{u.rol}</Badge></td>
                <td className="table-td">
                  <div className="actions-wrapper">
                    <Button size="sm" variant="ghost" onClick={() => openEdit(u)}>{t.users.edit}</Button>
                    <Button size="sm" variant="danger" onClick={() => handleDelete(u.id?.id || u.id)}>{t.users.delete}</Button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Modal open={!!editModal} onClose={() => setEditModal(null)} title={editModal === "new" ? t.users.add : t.users.edit}>
        <div className="modal-form">
          <Input label={t.users.name} value={form.username || ""} onChange={v => setForm(f => ({ ...f, username: v }))} />
          <Input label={t.users.email} value={form.email || ""} onChange={v => setForm(f => ({ ...f, email: v }))} type="email" />
          <Input label="Telefon" value={form.telefon || ""} onChange={v => setForm(f => ({ ...f, telefon: v }))} placeholder="+407..." />
          <Input label="Parola" value={form.parola || ""} onChange={v => setForm(f => ({ ...f, parola: v }))} type="password" />
          <Select label={t.users.role} value={form.rol || "CLIENT"} onChange={v => setForm(f => ({ ...f, rol: v }))}
            options={["CLIENT", "ANGAJAT", "MANAGER", "ADMIN"].map(r => ({ value: r, label: r === "ADMIN" ? "Administrator" : r }))} />
          <div className="modal-actions">
            <Button onClick={handleSave}>{t.common.save}</Button>
            <Button variant="secondary" onClick={() => setEditModal(null)}>{t.common.cancel}</Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}