package org.example.notificationservice.domain;

public interface IEmailSender {
    void trimite(String destinatar, String mesaj);
}