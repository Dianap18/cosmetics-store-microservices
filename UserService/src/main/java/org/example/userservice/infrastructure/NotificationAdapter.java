package org.example.userservice.infrastructure;

import org.example.userservice.domain.INotificationAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationAdapter implements INotificationAdapter {

    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;

    public NotificationAdapter(@Value("${notification.service.url:http://localhost:8084/api/notificari}") String url) {
        this.restTemplate = new RestTemplate();
        this.notificationServiceUrl = url;
    }

    @Override
    public void trimiteAlertaSecuritate(String email, String telefon, String mesaj) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("telefon", telefon);
            payload.put("mesaj", mesaj);

            restTemplate.postForEntity(notificationServiceUrl + "/trimite", payload, String.class);
        } catch (Exception e) {
            System.err.println("Eroare la contactarea NotificationService: " + e.getMessage());
        }
    }
}