package org.example.notificationservice.services;

import org.example.notificationservice.domain.IEmailSender;
import org.example.notificationservice.domain.INotificationObserver;
import org.springframework.stereotype.Component;

@Component
public class EmailObserver implements INotificationObserver {

    private final IEmailSender emailSender;

    public EmailObserver(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void trimiteNotificare(String email, String telefon, String mesaj) {
        if (email != null && !email.isEmpty()) {
            emailSender.trimite(email, mesaj);
        }
    }
}