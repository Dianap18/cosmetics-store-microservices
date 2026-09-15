package org.example.reportingservice.domain;

public interface IExporterFactory {
    IReportExporter getExporter(String format);
}