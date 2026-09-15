package org.example.userservice.domain;

public interface INotificationAdapter {
    void trimiteAlertaSecuritate(String email, String telefon, String mesaj);
}