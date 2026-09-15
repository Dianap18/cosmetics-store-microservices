package org.example.inventoryservice.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class SearchResultDTO {
    private boolean disponibilLocal;
    private int cantitateLocala;
    private List<MagazinDTO> magazineAlternative;
}