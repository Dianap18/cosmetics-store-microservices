import React, { useState } from "react";
import { Card, Badge, Button } from "./ui";
import "./ProductCard.css";

export default function ProductCard({ product, role, t, onSell, onEdit, onDelete, onUpdateStock }) {
  const [currentImgIndex, setCurrentImgIndex] = useState(0);

  const isAvailable = product.stoc > 0;
  const hasImages = product.imagini && product.imagini.length > 0;
  const imagini = hasImages ? product.imagini : ["https://placehold.co/200x200/f3f4f6/333?text=Fara+Imagine"];

  const prevImage = (e) => {
    e.preventDefault();
    setCurrentImgIndex((prev) => (prev === 0 ? imagini.length - 1 : prev - 1));
  };

  const nextImage = (e) => {
    e.preventDefault();
    setCurrentImgIndex((prev) => (prev === imagini.length - 1 ? 0 : prev + 1));
  };

  return (
    <Card className="product-card">
      <div className="product-image-container">
        <img 
          src={imagini[currentImgIndex]} 
          alt={product.denumire} 
          className="product-image" 
        />
        
        {hasImages && imagini.length > 1 && (
          <>
            <button onClick={prevImage} className="image-nav-btn image-nav-btn-prev">
              &#10094;
            </button>
            <button onClick={nextImage} className="image-nav-btn image-nav-btn-next">
              &#10095;
            </button>

            <div className="image-dots-container">
              {imagini.map((_, idx) => (
                <div 
                  key={idx} 
                  className={`image-dot ${currentImgIndex === idx ? "image-dot-active" : ""}`} 
                />
              ))}
            </div>
          </>
        )}
      </div>

      <div>
        <div className="product-title">{product.denumire}</div>
        <div className="product-producer">{product.producator}</div>
      </div>
      
      <div className="product-meta-row">
        <span className="product-price">{product.pretVanzare?.toFixed(2)} RON</span>
        <Badge color={isAvailable ? "green" : "red"}>{isAvailable ? t.products.available : t.products.unavailable}</Badge>
      </div>
      
      {isAvailable && product.magazine && product.magazine.length > 0 && (
        <div className="product-location-box">
          <span className="product-location-label">{t.products.availableIn}</span> <br />
          <b className="product-location-name">{product.magazine.join(", ")}</b>
        </div>
      )}
      
      {(role === "ANGAJAT" || role === "MANAGER") && (
        <div className="product-stock-text">{t.products.stock}: <b>{product.stoc}</b></div>
      )}
      
      {role === "MANAGER" && (
        <div className="product-manager-price">{t.products.buyPrice}: {product.pretAchizitie?.toFixed(2)} RON</div>
      )}
      
      <div className="product-actions-box">
        {role === "ANGAJAT" && isAvailable && (
          <Button size="sm" onClick={() => onSell(product)}>{t.products.sell}</Button>
        )}
        {role === "ANGAJAT" && (
          <Button size="sm" variant="ghost" onClick={() => onUpdateStock(product)}>{t.products.updateStock}</Button>
        )}
        {role === "MANAGER" && (
          <>
            <Button size="sm" variant="ghost" onClick={() => onEdit(product)}>{t.products.edit}</Button>
            <Button size="sm" variant="danger" onClick={() => onDelete(product.id)}>{t.products.delete}</Button>
          </>
        )}
      </div>
    </Card>
  );
}