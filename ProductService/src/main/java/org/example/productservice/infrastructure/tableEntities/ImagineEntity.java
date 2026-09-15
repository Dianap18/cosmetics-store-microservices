package org.example.productservice.infrastructure.tableEntities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "Imagini")
public class ImagineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "CaleFisier", nullable = false)
    private String caleFisier;

    @ManyToOne
    @JoinColumn(name = "IdProdus", nullable = false)
    @ToString.Exclude
    private ProdusEntity produs;
}