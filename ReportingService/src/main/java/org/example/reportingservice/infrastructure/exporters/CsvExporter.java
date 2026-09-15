package org.example.reportingservice.infrastructure.exporters;

import org.example.reportingservice.domain.IReportExporter;
import org.example.reportingservice.domain.ProdusReport;
import org.example.reportingservice.domain.Raport;

import java.time.LocalDate;
import java.util.List;

public class CsvExporter implements IReportExporter {

    @Override
    public Raport exporta(List<ProdusReport> date) {
        StringBuilder sb = new StringBuilder();
        sb.append("denumire,producator,pretVanzare\n");
        for (ProdusReport p : date) {
            sb.append(p.getDenumire()).append(",")
                    .append(p.getProducator()).append(",")
                    .append(p.getPretVanzare()).append("\n");
        }
        return new Raport(null, "csv", LocalDate.now().toString(), sb.toString().getBytes());
    }
}