package org.example.notificationservice.controllers;

import org.example.notificationservice.services.NotificationDispatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notificari")
public class NotificationController {

    private final NotificationDispatcher dispatcher;

    public NotificationController(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping("/trimite")
    public ResponseEntity<String> primesteSiDifuzeaza(@RequestBody Map<String, Object> date) {
        int idUtilizator = (int) date.getOrDefault("idUtilizator", 0); // Luăm ID-ul dacă există
        String email = (String) date.get("email");
        String telefon = (String) date.get("telefon");
        String mesaj = (String) date.get("mesaj");

        dispatcher.difuzeazaNotificare(idUtilizator, email, telefon, mesaj);
        return ResponseEntity.ok("Notificare procesată.");
    }
}