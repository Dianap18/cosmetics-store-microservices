package org.example.reportingservice.infrastructure;

import org.example.reportingservice.domain.IInventoryClient;
import org.example.reportingservice.domain.StocReport;
import org.example.reportingservice.domain.VanzareReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class InventoryServiceClient implements IInventoryClient {

    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;

    public InventoryServiceClient(
            @Value("${inventory.service.url:http://localhost:8083/api/inventory}") String inventoryUrl) {
        this.restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();
        this.inventoryServiceUrl = inventoryUrl;
    }

    @Override
    public List<VanzareReport> fetchVanzari() {
        try {
            return restTemplate.exchange(
                    inventoryServiceUrl + "/vanzari",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<VanzareReport>>() {}
            ).getBody();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<StocReport> fetchStocuri() {
        try {
            return restTemplate.exchange(
                    inventoryServiceUrl + "/stocuri",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<StocReport>>() {}
            ).getBody();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}