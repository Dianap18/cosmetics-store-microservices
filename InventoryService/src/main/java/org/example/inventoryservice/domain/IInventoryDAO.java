package org.example.inventoryservice.domain;

import java.util.List;

public interface IInventoryDAO {
    int getStocCurent(StoreID idMagazin, int idProdus);
    List<Magazin> findMagazineCuStoc(int idProdus);
    boolean updateStoc(StoreID idMagazin, int idProdus, int cantitateNoua);
    boolean inregistreazaVanzare(StoreID idMagazin, int idProdus, int cantitate);
    List<Magazin> getToateMagazinele();
}