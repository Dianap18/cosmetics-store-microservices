package org.example.productservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RezultatCautareClientDTO {
    private String denumire;
    private String producator;
    private double pretVanzare;
    private List<String> imagini;
    private List<Integer> disponibilInMagazinele;
    private String mesaj;
}