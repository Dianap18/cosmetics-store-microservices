package org.example.productservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ProdusResponseDTO {
    private int id;
    private String denumire;
    private String producator;
    private double pretAchizitie;
    private double pretVanzare;
    private List<String> imagini;
}