package org.example.inventoryservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MagazinDTO {
    private int id;
    private String nume;
    private String adresa;
}