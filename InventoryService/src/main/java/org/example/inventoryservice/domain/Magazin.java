package org.example.inventoryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Magazin {
    private StoreID id;
    private String nume;
    private String adresa;
}