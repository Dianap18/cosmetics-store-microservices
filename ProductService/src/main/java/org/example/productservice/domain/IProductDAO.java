package org.example.productservice.domain;
import java.util.List;

public interface IProductDAO {
    List<Produs> getToateProdusele();
    Produs getProdusDupaDenumire(String denumire);
    boolean salveazaProdus(Produs produs);
    boolean actualizeazaProdus(Produs produs);
    boolean stergeProdus(int id);
}