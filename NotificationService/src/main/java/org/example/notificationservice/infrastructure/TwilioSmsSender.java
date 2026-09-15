package org.example.notificationservice.infrastructure;

import org.example.notificationservice.domain.ISmsSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioSmsSender implements ISmsSender {

    private final String numarTwilio;

    public TwilioSmsSender(
            @Value("${twilio.account_sid}") String accountSid,
            @Value("${twilio.auth_token}") String authToken,
            @Value("${twilio.phone_number}") String numarTwilio) {

        this.numarTwilio = numarTwilio;

        try {
            Twilio.init(accountSid, authToken);
            System.out.println("Twilio s-a conectat cu succes!");
        } catch (Exception e) {
            System.err.println("Eroare la conectarea cu Twilio: " + e.getMessage());
        }
    }

    @Override
    public void trimite(String numarTelefon, String mesaj) {
        try {
            Message.creator(
                    new PhoneNumber(numarTelefon),
                    new PhoneNumber(numarTwilio),
                    mesaj
            ).create();
            System.out.println("SMS trimis cu succes către: " + numarTelefon);
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea SMS: " + e.getMessage());
        }
    }
}