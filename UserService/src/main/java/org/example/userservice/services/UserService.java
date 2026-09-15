package org.example.userservice.services;

import org.example.userservice.domain.INotificationAdapter;
import org.example.userservice.domain.IUserDAO;
import org.example.userservice.domain.UserID;
import org.example.userservice.domain.Utilizator;
import org.example.userservice.domain.dto.AuthResponseDTO;
import org.example.userservice.domain.dto.LoginDTO;
import org.example.userservice.domain.dto.UtilizatorRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final IUserDAO userDAO;
    private final INotificationAdapter notificationAdapter;

    public UserService(IUserDAO userDAO, INotificationAdapter notificationAdapter) {
        this.userDAO = userDAO;
        this.notificationAdapter = notificationAdapter;
    }

    public List<Utilizator> obtineTotiUtilizatorii() {
        return userDAO.getTotiUtilizatorii();
    }

    public List<Utilizator> obtineUtilizatoriDupaRol(String rol) {
        return userDAO.getUtilizatoriDupaRol(rol);
    }

    public boolean adaugaUtilizator(UtilizatorRequestDTO dto) {
        return userDAO.salveazaUtilizator(mapToDomain(null, dto));
    }

    public boolean stergeUtilizator(int id) {
        return userDAO.stergeUtilizator(id);
    }

    public AuthResponseDTO autentificare(LoginDTO loginDate) {
        Utilizator utilizator = userDAO.cautaDupaUsernameSiParola(loginDate.getUsername(), loginDate.getParola());

        if (utilizator != null) {
            return new AuthResponseDTO(true, "Logare reușită!", utilizator.getRol(), utilizator.getIdMagazin());
        } else {
            return new AuthResponseDTO(false, "Username sau parolă incorecte.", null, 0);
        }
    }

    public boolean schimbaParola(int id, String parolaNoua) {
        Utilizator utilizator = userDAO.getUtilizatorDupaId(id);
        if (utilizator != null) {
            utilizator.setParola(parolaNoua);
            boolean succes = userDAO.salveazaUtilizator(utilizator);

            if (succes) {
                String mesaj = "Atenție! Parola contului tău a fost schimbată recent.";
                notificationAdapter.trimiteAlertaSecuritate(utilizator.getEmail(), utilizator.getTelefon(), mesaj);
            }
            return succes;
        }
        return false;
    }

    public boolean actualizeazaUtilizator(int id, UtilizatorRequestDTO dateNoi) {
        Utilizator utilizatorExistent = userDAO.getUtilizatorDupaId(id);

        if (utilizatorExistent != null) {
            utilizatorExistent.setUsername(dateNoi.getUsername());
            utilizatorExistent.setEmail(dateNoi.getEmail());
            utilizatorExistent.setRol(dateNoi.getRol());
            utilizatorExistent.setTelefon(dateNoi.getTelefon());
            utilizatorExistent.setIdMagazin(dateNoi.getIdMagazin());

            boolean credentialeModificate = false;

            if (dateNoi.getParola() != null && !dateNoi.getParola().trim().isEmpty()) {
                utilizatorExistent.setParola(dateNoi.getParola());
                credentialeModificate = true;
            }

            boolean succes = userDAO.salveazaUtilizator(utilizatorExistent);

            if (succes && credentialeModificate) {
                String mesaj = "Atenție! Datele contului tău au fost modificate de către un administrator.";
                notificationAdapter.trimiteAlertaSecuritate(
                        utilizatorExistent.getEmail(),
                        utilizatorExistent.getTelefon(),
                        mesaj
                );
            }
            return succes;
        }
        return false;
    }

    public byte[] exportUtilizatoriCSV() {
        List<Utilizator> utilizatori = userDAO.getTotiUtilizatorii();
        StringBuilder sb = new StringBuilder();
        sb.append("Username,Email,Telefon,Rol\n");
        for (Utilizator u : utilizatori) {
            sb.append(u.getUsername()).append(",")
                    .append(u.getEmail()).append(",")
                    .append(u.getTelefon() != null ? u.getTelefon() : "").append(",")
                    .append(u.getRol()).append("\n");
        }
        return sb.toString().getBytes();
    }

    private Utilizator mapToDomain(Integer id, UtilizatorRequestDTO dto) {
        return new Utilizator(
                id != null ? new UserID(id) : null,
                dto.getUsername(),
                dto.getParola(),
                dto.getRol(),
                dto.getEmail(),
                dto.getTelefon(),
                dto.getIdMagazin()
        );
    }
}