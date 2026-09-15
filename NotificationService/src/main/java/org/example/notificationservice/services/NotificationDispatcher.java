package org.example.notificationservice.services;

import org.example.notificationservice.domain.INotificationDAO;
import org.example.notificationservice.domain.INotificationObserver;
import org.example.notificationservice.domain.Notificare;
import org.example.notificationservice.domain.TipNotificare;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationDispatcher {

    private final List<INotificationObserver> observatori;
    private final INotificationDAO notificationDAO;

    public NotificationDispatcher(List<INotificationObserver> observatori, INotificationDAO notificationDAO) {
        this.observatori = observatori;
        this.notificationDAO = notificationDAO;
    }

    public void difuzeazaNotificare(int idUtilizator, String email, String telefon, String mesaj) {

        if (email != null && !email.isEmpty()) {
            Notificare notificareEmail = new Notificare();
            notificareEmail.setIdUtilizator(idUtilizator);
            notificareEmail.setDataExpedierii(LocalDateTime.now());
            notificareEmail.setTip(TipNotificare.EMAIL);
            notificareEmail.setMesaj(mesaj);
            notificareEmail.setDestinatar(email);
            notificareEmail.setStatus("IN_CURS");
            salveazaSiActualizeazaStatus(notificareEmail);
        }

        if (telefon != null && !telefon.isEmpty()) {
            Notificare notificareSms = new Notificare();
            notificareSms.setIdUtilizator(idUtilizator);
            notificareSms.setDataExpedierii(LocalDateTime.now());
            notificareSms.setTip(TipNotificare.SMS);
            notificareSms.setMesaj(mesaj);
            notificareSms.setDestinatar(telefon);
            notificareSms.setStatus("IN_CURS");
            salveazaSiActualizeazaStatus(notificareSms);
        }

        for (INotificationObserver obs : observatori) {
            try {
                obs.trimiteNotificare(email, telefon, mesaj);
            } catch (Exception e) {
                System.err.println("Eroare la un observator: " + e.getMessage());
            }
        }
    }

    private void salveazaSiActualizeazaStatus(Notificare notificare) {
        try {
            boolean succes = notificationDAO.salveazaIstoric(notificare);
            if (succes) {
                notificare.setStatus("PROCESAT");
            }
        } catch (Exception e) {
            notificare.setStatus("EROARE_DATABASE");
            System.err.println("Baza de date nu a putut salva notificarea: " + e.getMessage());
        }
    }
}