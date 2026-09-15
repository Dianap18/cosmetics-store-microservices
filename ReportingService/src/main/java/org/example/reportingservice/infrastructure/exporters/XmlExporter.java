package org.example.reportingservice.infrastructure.exporters;

import org.example.reportingservice.domain.IReportExporter;
import org.example.reportingservice.domain.ProdusReport;
import org.example.reportingservice.domain.Raport;

import java.time.LocalDate;
import java.util.List;

public class XmlExporter implements IReportExporter {

    @Override
    public Raport exporta(List<ProdusReport> date) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<raport>\n");
        for (ProdusReport p : date) {
            sb.append("  <produs>\n");
            sb.append("    <denumire>").append(p.getDenumire()).append("</denumire>\n");
            sb.append("    <producator>").append(p.getProducator()).append("</producator>\n");
            sb.append("    <pretVanzare>").append(p.getPretVanzare()).append("</pretVanzare>\n");
            sb.append("  </produs>\n");
        }
        sb.append("</raport>");
        return new Raport(null, "xml", LocalDate.now().toString(), sb.toString().getBytes());
    }
}