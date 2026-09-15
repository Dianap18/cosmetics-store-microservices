package org.example.inventoryservice.services;

import org.example.inventoryservice.domain.*;
import org.example.inventoryservice.domain.dto.MagazinDTO;
import org.example.inventoryservice.domain.dto.SearchResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final IInventoryDAO inventoryDAO;

    public InventoryService(IInventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public SearchResultDTO cautaProdus(int idProdus, int idMagazinRaw) {
        StoreID sID = new StoreID(idMagazinRaw);
        SearchResultDTO dto = new SearchResultDTO();
        int stocLocal = inventoryDAO.getStocCurent(sID, idProdus);

        if (stocLocal > 0) {
            dto.setDisponibilLocal(true);
            dto.setCantitateLocala(stocLocal);
        } else {
            dto.setDisponibilLocal(false);
            List<Magazin> magazine = inventoryDAO.findMagazineCuStoc(idProdus);

            List<MagazinDTO> alternative = magazine.stream()
                    .filter(m -> m.getId().getId() != idMagazinRaw)
                    .map(m -> new MagazinDTO(m.getId().getId(), m.getNume(), m.getAdresa()))
                    .collect(Collectors.toList());
            dto.setMagazineAlternative(alternative);
        }
        return dto;
    }

    public boolean vinde(int idMag, int idProd, int cant) {
        return inventoryDAO.inregistreazaVanzare(new StoreID(idMag), idProd, cant);
    }

    public boolean actualizeaza(int idMag, int idProd, int cant) {
        if (cant < 0) return false;
        return inventoryDAO.updateStoc(new StoreID(idMag), idProd, cant);
    }

    public int getStocDirect(int idProdus, int idMagazin) {
        return inventoryDAO.getStocCurent(new StoreID(idMagazin), idProdus);
    }

    public List<Integer> getMagazineCuStoc(int idProdus) {
        return inventoryDAO.findMagazineCuStoc(idProdus).stream()
                .map(m -> m.getId().getId())
                .collect(Collectors.toList());
    }

    public List<MagazinDTO> getToateMagazinele() {
        return inventoryDAO.getToateMagazinele().stream()
                .map(m -> new MagazinDTO(m.getId().getId(), m.getNume(), m.getAdresa()))
                .collect(Collectors.toList());
    }
}