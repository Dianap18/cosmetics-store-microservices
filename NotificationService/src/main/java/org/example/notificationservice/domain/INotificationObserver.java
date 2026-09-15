package org.example.notificationservice.domain;

public interface INotificationObserver {
    void trimiteNotificare(String email, String telefon, String mesaj);
}