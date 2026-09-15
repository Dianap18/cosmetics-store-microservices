package org.example.inventoryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stoc {
    private StoreID idMagazin;
    private int idProdus;
    private int cantitate;
}