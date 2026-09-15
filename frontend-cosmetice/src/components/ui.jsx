import React from "react";
import "./ui.css";

export function Badge({ children, color = "pink" }) {
  return (
    <span className={`badge badge-${color}`}>
      {children}
    </span>
  );
}

export function Button({ children, onClick, variant = "primary", size = "md", disabled = false, style = {} }) {
  return (
    <button 
      onClick={disabled ? undefined : onClick} 
      className={`btn btn-${size} btn-${variant}`} 
      disabled={disabled}
      style={style}
    >
      {children}
    </button>
  );
}

export function Input({ label, value, onChange, type = "text", placeholder, style = {} }) {
  return (
    <div className="form-group">
      {label && <label className="form-label">{label}</label>}
      <input
        type={type}
        value={value}
        onChange={e => onChange(e.target.value)}
        placeholder={placeholder}
        className="form-input"
        style={style}
      />
    </div>
  );
}

export function Select({ label, value, onChange, options }) {
  return (
    <div className="form-group">
      {label && <label className="form-label">{label}</label>}
      <select value={value} onChange={e => onChange(e.target.value)} className="form-select">
        {options.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
      </select>
    </div>
  );
}

export function Card({ children, style = {} }) {
  return (
    <div className="card" style={style}>
      {children}
    </div>
  );
}

export function Modal({ open, onClose, title, children }) {
  if (!open) return null;
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h3 className="modal-title">{title}</h3>
          <button onClick={onClose} className="modal-close-btn">×</button>
        </div>
        {children}
      </div>
    </div>
  );
}