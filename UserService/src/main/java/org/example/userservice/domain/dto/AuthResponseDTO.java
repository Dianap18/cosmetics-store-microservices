package org.example.userservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private boolean succes;
    private String mesaj;
    private String rol;
    private int idMagazin;
}