package org.example.userservice.domain.dto;

import lombok.Data;

@Data
public class UtilizatorRequestDTO {
    private String username;
    private String parola;
    private String rol;
    private String email;
    private String telefon;
    private int idMagazin;
}