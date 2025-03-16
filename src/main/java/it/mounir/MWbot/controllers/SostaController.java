package it.mounir.MWbot.controllers;

import it.mounir.MWbot.DTO.RichiestaSosta;
import it.mounir.MWbot.model.Sosta;
import it.mounir.MWbot.services.SostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class SostaController {

    private final SostaService sostaService;

    public SostaController(SostaService sostaService) {
        this.sostaService = sostaService;
    }

    @PostMapping("/sosta")
    public ResponseEntity<String> richiestaSosta(@RequestBody RichiestaSosta richiestaSosta) {
        Sosta sostaSalvata = sostaService.richiestaSosta(richiestaSosta);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sostaSalvata.getIdSosta())
                .toUri();

        return ResponseEntity
                .created(location)
                .body("Richiesta di sosta ricevuta per veicolo " + richiestaSosta.getVeicoloId());
    }
}
