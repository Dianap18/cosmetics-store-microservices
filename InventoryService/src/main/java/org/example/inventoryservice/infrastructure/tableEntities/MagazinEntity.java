package org.example.inventoryservice.infrastructure.tableEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.example.inventoryservice.domain.Magazin;
import org.example.inventoryservice.domain.StoreID;

@Data
@Entity
@Table(name = "Magazin")
public class MagazinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_magazin")
    private int idMagazin;

    @Column(name = "Nume", length = 100)
    private String nume;

    @Column(name = "Adresa", length = 200)
    private String adresa;

    public MagazinEntity() {}

    public Magazin ToMagazin() {
        return new Magazin(new StoreID(this.idMagazin), this.nume, this.adresa);
    }
}