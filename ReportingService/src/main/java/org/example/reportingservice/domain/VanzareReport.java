package org.example.reportingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VanzareReport {
    private int idProdus;
    private int cantitateVanduta;
    private String dataVanzarii;
}