package org.example.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilizator {
    private UserID id;
    private String username;
    private String parola;
    private String rol;
    private String email;
    private String telefon;
    private int idMagazin;
}