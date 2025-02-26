package it.mounir.MWbot.controllers;

import it.mounir.MWbot.services.CodaService;
import it.mounir.MWbot.services.ParcheggioRepositoryService;
import it.mounir.MWbot.services.ParcheggioService;
import it.mounir.MWbot.services.SostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    public ResponseEntity<String> aggiornaCostoOrario(@RequestParam int idParcheggio, @RequestParam int costoSosta) {

        /*FIXME: verificare che l'ID del parcheggio esista*/
        parcheggioRepositoryService.updateCostoSostaById(idParcheggio, costoSosta);
        return ResponseEntity
                .ok("Costo sosta orario aggiornato con successo !");
    }

    @PostMapping("/aggiorna/costo/kw")
    public ResponseEntity<String> aggiornaCostoKw(@RequestParam int idParcheggio, @RequestParam int costoKw) {

        /*FIXME: verificare che l'ID del parcheggio esista*/
        parcheggioRepositoryService.updateCostoKwById(idParcheggio, costoKw);
        return ResponseEntity
                .ok("Costo di ricarica aggiornato con successo !");
    }

}
