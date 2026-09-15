package org.example.reportingservice.services;

import org.example.reportingservice.domain.IExporterFactory;
import org.example.reportingservice.domain.IInventoryClient;
import org.example.reportingservice.domain.IProductClient;
import org.example.reportingservice.domain.IReportExporter;
import org.example.reportingservice.domain.ProdusReport;
import org.example.reportingservice.domain.Raport;
import org.example.reportingservice.domain.Statistica;
import org.example.reportingservice.domain.StocReport;
import org.example.reportingservice.domain.VanzareReport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingService {

    private final IProductClient productClient;
    private final IInventoryClient inventoryClient;
    private final IExporterFactory exporterFactory;

    public ReportingService(IProductClient productClient,
                            IInventoryClient inventoryClient,
                            IExporterFactory exporterFactory) {
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
        this.exporterFactory = exporterFactory;
    }

    public Raport genereazaRaport(String format) {
        List<ProdusReport> date = productClient.fetchToateProdusele();
        IReportExporter exporter = exporterFactory.getExporter(format);
        return exporter.exporta(date);
    }

    public List<Statistica> calculeazaStatistici() {
        List<ProdusReport> produse = productClient.fetchToateProdusele();
        List<VanzareReport> vanzari = inventoryClient.fetchVanzari();
        List<StocReport> stocuri = inventoryClient.fetchStocuri();

        List<Statistica> statistici = new ArrayList<>();

        statistici.add(new Statistica(
                "Total produse in catalog",
                produse.size()));

        statistici.add(new Statistica(
                "Total vanzari inregistrate",
                vanzari.size()));

        double medie = produse.isEmpty() ? 0 : (double) vanzari.size() / produse.size();
        statistici.add(new Statistica(
                "Medie vanzari per produs",
                medie));

        long faraStoc = stocuri.stream()
                .filter(s -> s.getCantitate() == 0)
                .count();
        statistici.add(new Statistica(
                "Produse fara stoc",
                faraStoc));

        return statistici;
    }
}