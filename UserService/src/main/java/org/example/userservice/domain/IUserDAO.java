package org.example.userservice.domain;

import java.util.List;

public interface IUserDAO {
    List<Utilizator> getTotiUtilizatorii();
    Utilizator getUtilizatorDupaId(int id);
    boolean salveazaUtilizator(Utilizator utilizator);
    boolean stergeUtilizator(int id);
    Utilizator cautaDupaUsernameSiParola(String username, String parola);
    List<Utilizator> getUtilizatoriDupaRol(String rol);
}