package it.mounir.MWbot.controllers;

import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.services.RicaricaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class RicaricaController {

    private final RicaricaService ricaricaService;

    public RicaricaController(RicaricaService ricaricaService) {
        this.ricaricaService = ricaricaService;
    }

    @PostMapping("/ricarica")
    public ResponseEntity<String> richiestaRicarica(@RequestBody RichiestaRicarica richiestaRicarica) {
        Ricarica ricaricaSalvata = ricaricaService.richiestaRicarica(richiestaRicarica);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ricaricaSalvata.getIdRicarica())
                .toUri();

        return ResponseEntity
                .created(location)
                .body("Richiesta di ricarica ricevuta per veicolo " + richiestaRicarica.getVeicoloId());
    }
}
