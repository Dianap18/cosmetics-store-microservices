package org.example.reportingservice.domain;

import java.util.List;

public interface IReportExporter {
    Raport exporta(List<ProdusReport> date);
}