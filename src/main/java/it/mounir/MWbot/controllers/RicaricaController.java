package it.mounir.MWbot.controllers;

import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.services.RicaricaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RicaricaController {

    private final RicaricaService ricaricaService;

    public RicaricaController(RicaricaService ricaricaService) {
        this.ricaricaService = ricaricaService;
    }

    @PostMapping("/ricarica")
    public ResponseEntity<String> richiestaRicarica(@RequestBody RichiestaRicarica richiestaRicarica) {
        ricaricaService.richiestaRicarica(richiestaRicarica);

        return ResponseEntity
                .ok("Richiesta di ricarica ricevuta per veicolo " + richiestaRicarica.getVeicoloId());
    }
}
