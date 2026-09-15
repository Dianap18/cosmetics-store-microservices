package org.example.userservice.controllers;

import org.example.userservice.domain.Utilizator;
import org.example.userservice.domain.dto.AuthResponseDTO;
import org.example.userservice.domain.dto.LoginDTO;
import org.example.userservice.domain.dto.SchimbaParolaDTO;
import org.example.userservice.domain.dto.UtilizatorRequestDTO;
import org.example.userservice.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO credentials) {
        AuthResponseDTO raspuns = userService.autentificare(credentials);
        if (raspuns.isSucces()) {
            return ResponseEntity.ok(raspuns);
        }
        return ResponseEntity.status(401).body(raspuns);
    }

    @GetMapping
    public ResponseEntity<List<Utilizator>> getAll(@RequestParam(required = false) String rol) {
        if (rol != null && !rol.isEmpty()) {
            return ResponseEntity.ok(userService.obtineUtilizatoriDupaRol(rol));
        }
        return ResponseEntity.ok(userService.obtineTotiUtilizatorii());
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UtilizatorRequestDTO user) {
        if (userService.adaugaUtilizator(user)) {
            return ResponseEntity.ok("Utilizator creat cu succes!");
        }
        return ResponseEntity.badRequest().body("Eroare la crearea utilizatorului.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody UtilizatorRequestDTO user) {
        if (userService.actualizeazaUtilizator(id, user)) {
            return ResponseEntity.ok("Utilizator actualizat cu succes!");
        }
        return ResponseEntity.status(404).body("Utilizatorul nu a putut fi modificat.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (userService.stergeUtilizator(id)) {
            return ResponseEntity.ok("Utilizator șters!");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/schimba-parola")
    public ResponseEntity<String> schimbaParola(@PathVariable int id, @RequestBody SchimbaParolaDTO dateParola) {
        if (userService.schimbaParola(id, dateParola.getParolaNoua())) {
            return ResponseEntity.ok("Parolă modificată! S-a trimis notificare pe email și SMS.");
        }
        return ResponseEntity.badRequest().body("Eroare la schimbarea parolei.");
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCSV() {
        byte[] continut = userService.exportUtilizatoriCSV();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "utilizatori.csv");
        return ResponseEntity.ok().headers(headers).body(continut);
    }
}