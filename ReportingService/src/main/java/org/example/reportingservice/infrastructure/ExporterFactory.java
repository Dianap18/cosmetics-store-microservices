package org.example.reportingservice.infrastructure;

import org.example.reportingservice.domain.IExporterFactory;
import org.example.reportingservice.domain.IReportExporter;
import org.example.reportingservice.infrastructure.exporters.CsvExporter;
import org.example.reportingservice.infrastructure.exporters.DocExporter;
import org.example.reportingservice.infrastructure.exporters.JsonExporter;
import org.example.reportingservice.infrastructure.exporters.XmlExporter;
import org.springframework.stereotype.Component;

@Component
public class ExporterFactory implements IExporterFactory {

    @Override
    public IReportExporter getExporter(String format) {
        switch (format.toLowerCase()) {
            case "csv":  return new CsvExporter();
            case "json": return new JsonExporter();
            case "xml":  return new XmlExporter();
            case "doc":  return new DocExporter();
            default: throw new IllegalArgumentException("Format necunoscut: " + format);
        }
    }
}