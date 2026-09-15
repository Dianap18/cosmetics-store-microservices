package org.example.productservice.domain;
import java.util.List;

public interface IInventoryAdapter {
    int verificaStoc(int idProdus, int idMagazin);
    List<Integer> gasesteMagazineCuStoc(int idProdus);
}