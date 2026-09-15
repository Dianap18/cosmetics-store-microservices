package org.example.inventoryservice.infrastructure.tableEntities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Vanzare")
public class VanzareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vanzare")
    private int idVanzare;

    @ManyToOne
    @JoinColumn(name = "id_magazin", nullable = false)
    private MagazinEntity magazin;

    @Column(name = "IdProdus")
    private int idProdus;

    @Column(name = "CantitateVanduta")
    private int cantitateVanduta;

    @Column(name = "DataVanzarii")
    private LocalDateTime dataVanzarii;
}