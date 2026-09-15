package org.example.reportingservice.infrastructure;

import org.example.reportingservice.domain.IProductClient;
import org.example.reportingservice.domain.ProdusReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class ProductServiceClient implements IProductClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductServiceClient(
            @Value("${product.service.url:http://localhost:8082/api/produse}") String productUrl) {
        this.restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();
        this.productServiceUrl = productUrl;
    }

    @Override
    public List<ProdusReport> fetchToateProdusele() {
        try {
            return restTemplate.exchange(
                    productServiceUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProdusReport>>() {}
            ).getBody();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}