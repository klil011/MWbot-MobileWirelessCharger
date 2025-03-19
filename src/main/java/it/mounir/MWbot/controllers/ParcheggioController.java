package it.mounir.MWbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.mounir.MWbot.services.CodaService;
import it.mounir.MWbot.services.ParcheggioRepositoryService;
import it.mounir.MWbot.services.ParcheggioService;
import it.mounir.MWbot.services.SostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ParcheggioController {

    private final ParcheggioService parcheggioService;
    private final CodaService codaService;
    private final ParcheggioRepositoryService parcheggioRepositoryService;

    @Autowired
    public ParcheggioController(ParcheggioService parcheggioService, SostaService sostaService, CodaService codaService, ParcheggioRepositoryService parcheggioRepositoryService) {
        this.parcheggioService = parcheggioService;
        this.codaService = codaService;
        this.parcheggioRepositoryService = parcheggioRepositoryService;
    }

    @GetMapping("/monitor")
    @Operation(
            summary = "Monitora la disponibilità dei parcheggi",
            description = "Restituisce il numero di parcheggi disponibili o il numero di veicoli in coda se tutti i posti sono occupati.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Numero di parcheggi disponibili o veicoli in coda",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(responseCode = "406", description = "Tutti i posti sono occupati")
            }
    )
    public ResponseEntity<String> monitor() {
        if(parcheggioService.getPostiLiberiCount() == 0) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Tutti i posti sono al momento occupati, ci sono " + codaService.getCodaRichiesteCount()
                            + " veicoli in coda.");
        }

        return ResponseEntity
                .ok("Parcheggi disponibili: " + parcheggioService.getPostiLiberiCount());
    }

    @PostMapping("/aggiorna/costo/orario")
    @Operation(
            summary = "Aggiorna il costo orario del parcheggio",
            description = "Aggiorna il costo orario del parcheggio identificato dall'ID fornito.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Costo orario aggiornato con successo"),
                    @ApiResponse(responseCode = "403", description = "ID parcheggio non valido")
            }
    )
    public ResponseEntity<String> aggiornaCostoOrario(
            @Parameter(description = "ID del parcheggio", example = "1")
            @RequestParam int idParcheggio,

            @Parameter(description = "Nuovo costo orario", example = "5")
            @RequestParam int costoSosta) {

        /* Il sistema gestisce un solo parcheggio, quindi per semplicità il controllo è posto solo su un valore unico */
        if (idParcheggio != 1) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        parcheggioRepositoryService.updateCostoSostaById(idParcheggio, costoSosta);
        return ResponseEntity
                .ok("Costo sosta orario aggiornato con successo !");
    }

    @PostMapping("/aggiorna/costo/kw")
    @Operation(
            summary = "Aggiorna il costo di ricarica per kW",
            description = "Aggiorna il costo di ricarica per kW del parcheggio identificato dall'ID fornito.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Costo di ricarica aggiornato con successo"),
                    @ApiResponse(responseCode = "403", description = "ID parcheggio non valido")
            }
    )
    public ResponseEntity<String> aggiornaCostoKw(
            @Parameter(description = "ID del parcheggio", example = "1")
            @RequestParam int idParcheggio,

            @Parameter(description = "Nuovo costo per kW", example = "10")
            @RequestParam int costoKw) {

        if (idParcheggio != 1) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        parcheggioRepositoryService.updateCostoKwById(idParcheggio, costoKw);
        return ResponseEntity
                .ok("Costo di ricarica aggiornato con successo !");
    }

}
