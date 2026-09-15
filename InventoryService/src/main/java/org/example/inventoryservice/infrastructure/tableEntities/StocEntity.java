package org.example.inventoryservice.infrastructure.tableEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.example.inventoryservice.domain.Stoc;
import org.example.inventoryservice.domain.StoreID;

@Data
@Entity
@Table(name = "Stoc")
public class StocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stoc")
    private int idStoc;

    @ManyToOne
    @JoinColumn(name = "id_magazin", nullable = false)
    private MagazinEntity magazin;

    @Column(name = "IdProdus")
    private int idProdus;

    @Column(name = "Cantitate")
    private int cantitate;

    public StocEntity() {}

    public Stoc ToStoc() {
        return new Stoc(new StoreID(this.magazin.getIdMagazin()), this.idProdus, this.cantitate);
    }
}