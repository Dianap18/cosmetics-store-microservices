package org.example.notificationservice.services;

import org.example.notificationservice.domain.INotificationObserver;
import org.example.notificationservice.domain.ISmsSender;
import org.springframework.stereotype.Component;

@Component
public class SMSObserver implements INotificationObserver {

    private final ISmsSender smsSender;

    public SMSObserver(ISmsSender smsSender) {
        this.smsSender = smsSender;
    }

    @Override
    public void trimiteNotificare(String email, String telefon, String mesaj) {
        if (telefon != null && !telefon.isEmpty()) {
            smsSender.trimite(telefon, mesaj);
        }
    }
}