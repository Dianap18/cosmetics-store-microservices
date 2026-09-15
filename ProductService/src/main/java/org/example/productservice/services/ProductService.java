package org.example.productservice.services;

import org.example.productservice.domain.*;
import org.example.productservice.domain.dto.*;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {
    private final IProductDAO productDAO;
    private final IInventoryAdapter inventoryAdapter;

    public ProductService(IProductDAO productDAO, IInventoryAdapter inventoryAdapter) {
        this.productDAO = productDAO;
        this.inventoryAdapter = inventoryAdapter;
    }

    public List<ProdusResponseDTO> obtineCatalog(String producator, Double pretMin, Double pretMax, String sortare) {
        List<Produs> produse = productDAO.getToateProdusele();
        Stream<Produs> stream = produse.stream();

        if (producator != null) stream = stream.filter(p -> p.getProducator().equalsIgnoreCase(producator));
        if (pretMin != null) stream = stream.filter(p -> p.getPretVanzare() >= pretMin);
        if (pretMax != null) stream = stream.filter(p -> p.getPretVanzare() <= pretMax);

        if (sortare != null) {
            if (sortare.equals("pret_crescator")) stream = stream.sorted(Comparator.comparingDouble(Produs::getPretVanzare));
            else if (sortare.equals("denumire")) stream = stream.sorted(Comparator.comparing(Produs::getDenumire));
        }

        return stream.map(p -> new ProdusResponseDTO(
                p.getId().getId(),
                p.getDenumire(),
                p.getProducator(),
                p.getPretAchizitie(),
                p.getPretVanzare(),
                p.getImagini()
        )).collect(Collectors.toList());
    }

    public void adaugaProdusNou(ProdusRequestDTO dto) {
        if (dto.getPretAchizitie() < 0 || dto.getPretVanzare() < 0) {
            throw new IllegalArgumentException("Prețurile nu pot fi negative.");
        }
        productDAO.salveazaProdus(mapToDomain(null, dto));
    }

    public boolean actualizeazaProdus(int id, ProdusRequestDTO dto) {
        if (dto.getPretAchizitie() < 0 || dto.getPretVanzare() < 0) {
            throw new IllegalArgumentException("Prețurile nu pot fi negative.");
        }
        return productDAO.actualizeazaProdus(mapToDomain(id, dto));
    }

    public boolean stergeProdus(int id) {
        return productDAO.stergeProdus(id);
    }

    private Produs mapToDomain(Integer id, ProdusRequestDTO dto) {
        return Produs.builder()
                .id(id != null ? new ProductID(id) : null)
                .denumire(dto.getDenumire())
                .producator(dto.getProducator())
                .pretAchizitie(dto.getPretAchizitie())
                .pretVanzare(dto.getPretVanzare())
                .imagini(dto.getImagini())
                .build();
    }

    public RezultatCautareDTO cautaProdusAngajat(String denumire, int idMagazin) {
        Produs p = productDAO.getProdusDupaDenumire(denumire);
        if (p == null) return new RezultatCautareDTO(null, null, 0, 0, null, 0, null, "Nu există.");

        int stoc = inventoryAdapter.verificaStoc(p.getId().getId(), idMagazin);
        List<Integer> alteMag = (stoc == 0) ? inventoryAdapter.gasesteMagazineCuStoc(p.getId().getId()) : null;

        return new RezultatCautareDTO(p.getDenumire(), p.getProducator(), p.getPretAchizitie(),
                p.getPretVanzare(), p.getImagini(), stoc, alteMag, "OK");
    }

    public RezultatCautareClientDTO cautaProdusClient(String denumire) {
        Produs p = productDAO.getProdusDupaDenumire(denumire);
        if (p == null) return new RezultatCautareClientDTO(null, null, 0, null, null, "Produsul nu a fost găsit.");

        List<Integer> magazine = inventoryAdapter.gasesteMagazineCuStoc(p.getId().getId());
        return new RezultatCautareClientDTO(p.getDenumire(), p.getProducator(), p.getPretVanzare(),
                p.getImagini(), magazine, magazine.isEmpty() ? "Indisponibil" : "Disponibil în magazinele listate");
    }
}