package org.example.productservice.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProdusRequestDTO {
    private String denumire;
    private String producator;
    private double pretAchizitie;
    private double pretVanzare;
    private List<String> imagini;
}