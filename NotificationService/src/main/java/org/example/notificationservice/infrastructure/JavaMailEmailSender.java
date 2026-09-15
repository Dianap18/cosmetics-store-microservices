package org.example.notificationservice.infrastructure;

import org.example.notificationservice.domain.IEmailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class JavaMailEmailSender implements IEmailSender {

    private final JavaMailSender mailSender;

    public JavaMailEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void trimite(String destinatar, String mesaj) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(destinatar);
            email.setSubject("Alertă Securitate Cont");
            email.setText(mesaj);
            mailSender.send(email);
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea emailului: " + e.getMessage());
        }
    }
}