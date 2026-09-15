package org.example.reportingservice.domain;

import java.util.List;

public interface IInventoryClient {
    List<VanzareReport> fetchVanzari();
    List<StocReport> fetchStocuri();
}