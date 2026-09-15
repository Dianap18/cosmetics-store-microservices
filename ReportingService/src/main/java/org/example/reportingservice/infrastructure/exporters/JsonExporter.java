package org.example.reportingservice.infrastructure.exporters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.reportingservice.domain.IReportExporter;
import org.example.reportingservice.domain.ProdusReport;
import org.example.reportingservice.domain.Raport;

import java.time.LocalDate;
import java.util.List;

public class JsonExporter implements IReportExporter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Raport exporta(List<ProdusReport> date) {
        try {
            byte[] continut = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(date);
            return new Raport(null, "json", LocalDate.now().toString(), continut);
        } catch (Exception e) {
            return new Raport(null, "json", LocalDate.now().toString(), "[]".getBytes());
        }
    }
}