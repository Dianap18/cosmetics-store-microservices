package org.example.reportingservice.infrastructure.exporters;

import org.example.reportingservice.domain.IReportExporter;
import org.example.reportingservice.domain.ProdusReport;
import org.example.reportingservice.domain.Raport;

import java.time.LocalDate;
import java.util.List;

public class DocExporter implements IReportExporter {

    @Override
    public Raport exporta(List<ProdusReport> date) {
        StringBuilder sb = new StringBuilder();
        sb.append("RAPORT PRODUSE\n");
        sb.append("================\n\n");
        for (ProdusReport p : date) {
            sb.append("Denumire: ").append(p.getDenumire())
                    .append(" | Producator: ").append(p.getProducator())
                    .append(" | Pret: ").append(p.getPretVanzare()).append("\n");
        }
        return new Raport(null, "doc", LocalDate.now().toString(), sb.toString().getBytes());
    }
}