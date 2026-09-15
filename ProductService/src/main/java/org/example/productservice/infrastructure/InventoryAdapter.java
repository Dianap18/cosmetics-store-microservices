package org.example.productservice.infrastructure;

import org.example.productservice.domain.IInventoryAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryAdapter implements IInventoryAdapter {

    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;

    public InventoryAdapter(@Value("${inventory.service.url:http://localhost:8083/api/inventory}") String url) {
        this.restTemplate = new RestTemplate();
        this.inventoryServiceUrl = url;
    }

    @Override
    public int verificaStoc(int idProdus, int idMagazin) {
        try {
            String url = inventoryServiceUrl + "/stoc?idProdus=" + idProdus + "&idMagazin=" + idMagazin;
            ResponseEntity<Integer> response = restTemplate.getForEntity(url, Integer.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception ignored) {}
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> gasesteMagazineCuStoc(int idProdus) {
        try {
            String url = inventoryServiceUrl + "/magazine-cu-stoc?idProdus=" + idProdus;
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (List<Integer>) response.getBody();
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }
}