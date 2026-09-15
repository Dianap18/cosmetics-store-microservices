package org.example.productservice.infrastructure.tableEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.example.productservice.domain.Produs;
import org.example.productservice.domain.ProductID;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "Produse")
public class ProdusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "Denumire", nullable = false)
    private String denumire;

    @Column(name = "Producator")
    private String producator;

    @Column(name = "PretAchizitie")
    private double pretAchizitie;

    @Column(name = "PretVanzare")
    private double pretVanzare;

    @OneToMany(mappedBy = "produs", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ImagineEntity> imagini = new ArrayList<>();

    public ProdusEntity() {
    }

    public ProdusEntity(Produs produs) {
        if (produs.getId() != null) {
            this.id = produs.getId().getId();
        }
        this.denumire = produs.getDenumire();
        this.producator = produs.getProducator();
        this.pretAchizitie = produs.getPretAchizitie();
        this.pretVanzare = produs.getPretVanzare();

        if (produs.getImagini() != null) {
            for (String cale : produs.getImagini()) {
                ImagineEntity img = new ImagineEntity();
                img.setCaleFisier(cale);
                img.setProdus(this);
                this.imagini.add(img);
            }
        }
    }

    public Produs ToProdus() {
        List<String> imaginiStrings = new ArrayList<>();
        if (this.imagini != null) {
            imaginiStrings = this.imagini.stream()
                    .map(ImagineEntity::getCaleFisier)
                    .collect(Collectors.toList());
        }

        return Produs.builder()
                .id(new ProductID(this.id))
                .denumire(this.denumire)
                .producator(this.producator)
                .pretAchizitie(this.pretAchizitie)
                .pretVanzare(this.pretVanzare)
                .imagini(imaginiStrings)
                .build();
    }
}