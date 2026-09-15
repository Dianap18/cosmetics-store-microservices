package org.example.notificationservice.infrastructure.tableEntities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Notificari")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "IdUtilizator", nullable = false)
    private int idUtilizator;

    @Column(name = "DataExpedierii")
    private LocalDateTime dataExpedierii;

    @Column(name = "TipContact", nullable = false)
    private String tipContact;

    @Column(name = "Destinatar", nullable = false)
    private String destinatar;

    @Column(name = "Mesaj", length = 1000)
    private String mesaj;

    @Column(name = "Status")
    private String status;
}