package org.example.reportingservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Raport {
    private ReportID id;
    private String tipFormat;
    private String dataGenerare;
    private byte[] continut;
}