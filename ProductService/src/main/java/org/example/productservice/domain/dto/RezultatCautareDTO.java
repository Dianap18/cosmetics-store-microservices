package org.example.productservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RezultatCautareDTO {
    private String denumire;
    private String producator;
    private double pretAchizitie;
    private double pretVanzare;
    private List<String> imagini;
    private int stocLocal;
    private List<Integer> disponibilInAlteMagazine;
    private String mesaj;
}