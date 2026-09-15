package org.example.inventoryservice.controllers;

import org.example.inventoryservice.domain.dto.MagazinDTO;
import org.example.inventoryservice.domain.dto.SearchResultDTO;
import org.example.inventoryservice.services.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/cauta")
    public ResponseEntity<SearchResultDTO> cauta(@RequestParam int idProdus, @RequestParam int idMagazin) {
        return ResponseEntity.ok(inventoryService.cautaProdus(idProdus, idMagazin));
    }

    @PostMapping("/vanzare")
    public ResponseEntity<String> vinde(@RequestBody Map<String, Object> date) {
        int idM = (int) date.get("idMagazin");
        int idP = (int) date.get("idProdus");
        int cant = (int) date.get("cantitate");

        if (inventoryService.vinde(idM, idP, cant)) {
            return ResponseEntity.ok("Vanzare ok.");
        }
        return ResponseEntity.badRequest().body("Eroare: stoc insuficient.");
    }

    @PutMapping("/actualizare")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> date) {
        int idM = (int) date.get("idMagazin");
        int idP = (int) date.get("idProdus");
        int cant = (int) date.get("cantitateNoua");

        if (inventoryService.actualizeaza(idM, idP, cant)) {
            return ResponseEntity.ok("Stoc actualizat.");
        }
        return ResponseEntity.badRequest().body("Eroare la update.");
    }

    @GetMapping("/stoc")
    public ResponseEntity<Integer> getStoc(@RequestParam int idProdus, @RequestParam int idMagazin) {
        return ResponseEntity.ok(inventoryService.getStocDirect(idProdus, idMagazin));
    }

    @GetMapping("/magazine-cu-stoc")
    public ResponseEntity<List<Integer>> getMagazineCuStoc(@RequestParam int idProdus) {
        return ResponseEntity.ok(inventoryService.getMagazineCuStoc(idProdus));
    }

    @GetMapping("/magazine")
    public ResponseEntity<List<MagazinDTO>> getMagazine() {
        return ResponseEntity.ok(inventoryService.getToateMagazinele());
    }
}