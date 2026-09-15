package org.example.productservice.controllers;

import org.example.productservice.domain.dto.ProdusRequestDTO;
import org.example.productservice.domain.dto.ProdusResponseDTO;
import org.example.productservice.domain.dto.RezultatCautareDTO;
import org.example.productservice.domain.dto.RezultatCautareClientDTO;
import org.example.productservice.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produse")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProdusResponseDTO>> obtineCatalog(
            @RequestParam(required = false) String producator,
            @RequestParam(required = false) Double pretMin,
            @RequestParam(required = false) Double pretMax,
            @RequestParam(required = false) String sortare) {
        return ResponseEntity.ok(productService.obtineCatalog(producator, pretMin, pretMax, sortare));
    }

    @PostMapping
    public ResponseEntity<String> adaugaProdus(@RequestBody ProdusRequestDTO dto) {
        productService.adaugaProdusNou(dto);
        return ResponseEntity.ok("Produs adăugat cu succes!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizeazaProdus(@PathVariable int id, @RequestBody ProdusRequestDTO dto) {
        boolean succes = productService.actualizeazaProdus(id, dto);
        return succes ? ResponseEntity.ok("Actualizat!") : ResponseEntity.badRequest().body("Eroare la actualizare.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> stergeProdus(@PathVariable int id) {
        boolean succes = productService.stergeProdus(id);
        return succes ? ResponseEntity.ok("Șters cu succes!") : ResponseEntity.notFound().build();
    }

    @GetMapping("/cautare-angajat")
    public ResponseEntity<RezultatCautareDTO> cautaAngajat(@RequestParam String denumire, @RequestParam int idMagazin) {
        return ResponseEntity.ok(productService.cautaProdusAngajat(denumire, idMagazin));
    }

    @GetMapping("/cautare-client")
    public ResponseEntity<RezultatCautareClientDTO> cautaClient(@RequestParam String denumire) {
        return ResponseEntity.ok(productService.cautaProdusClient(denumire));
    }
}