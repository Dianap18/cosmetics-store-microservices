package org.example.reportingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdusReport {
    private String denumire;
    private String producator;
    private double pretVanzare;
}