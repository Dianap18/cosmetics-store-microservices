package org.example.productservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produs {
    private ProductID id;
    private String denumire;
    private String producator;
    private double pretAchizitie;
    private double pretVanzare;
    private List<String> imagini;
}