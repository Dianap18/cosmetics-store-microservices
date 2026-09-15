package org.example.reportingservice.infrastructure;

import org.springframework.web.client.RestTemplate;

public class RestTemplateSingleton {

    private static RestTemplateSingleton instance;
    private final RestTemplate restTemplate;

    private RestTemplateSingleton() {
        this.restTemplate = new RestTemplate();
    }

    public static RestTemplateSingleton getInstance() {
        if (instance == null) {
            instance = new RestTemplateSingleton();
        }
        return instance;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}