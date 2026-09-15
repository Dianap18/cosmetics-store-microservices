package org.example.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificare {
    private NotificationID id;
    private int idUtilizator;
    private LocalDateTime dataExpedierii;
    private TipNotificare tip;
    private String destinatar;
    private String mesaj;
    private String status;
}