package org.example.userservice.infrastructure.tableEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.example.userservice.domain.Utilizator;
import org.example.userservice.domain.UserID;

@Data
@Entity
@Table(name = "Utilizatori")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "Username", unique = true, nullable = false)
    private String username;

    @Column(name = "Parola", nullable = false)
    private String parola;

    @Column(name = "Rol", nullable = false)
    private String rol;

    @Column(name = "Email")
    private String email;

    @Column(name = "Telefon")
    private String telefon;

    @Column(name = "IdMagazin")
    private Integer idMagazin;

    public UserEntity() {}

    public UserEntity(Utilizator u) {
        this.username = u.getUsername();
        this.parola = u.getParola();
        this.rol = u.getRol();
        this.email = u.getEmail();
        this.telefon = u.getTelefon();
        this.idMagazin = u.getIdMagazin() > 0 ? u.getIdMagazin() : null;
    }

    public Utilizator toUtilizator() {
        return new Utilizator(
                new UserID(this.id),
                this.username,
                this.parola,
                this.rol,
                this.email,
                this.telefon,
                this.idMagazin != null ? this.idMagazin : 0
        );
    }
}