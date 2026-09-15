package org.example.reportingservice.controllers;

import org.example.reportingservice.domain.Raport;
import org.example.reportingservice.domain.Statistica;
import org.example.reportingservice.services.ReportingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rapoarte")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/statistici")
    public ResponseEntity<List<Statistica>> getStatistici() {
        return ResponseEntity.ok(reportingService.calculeazaStatistici());
    }

    @GetMapping("/export/{format}")
    public ResponseEntity<byte[]> exportRaport(@PathVariable String format) {
        Raport raport = reportingService.genereazaRaport(format);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                "raport." + raport.getTipFormat());

        return ResponseEntity.ok()
                .headers(headers)
                .body(raport.getContinut());
    }
}