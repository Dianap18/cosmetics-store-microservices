package org.example.notificationservice.domain;

public interface ISmsSender {
    void trimite(String numarTelefon, String mesaj);
}