package it.mounir.MWbot.controllers;

import it.mounir.MWbot.DTO.RichiestaSosta;
import it.mounir.MWbot.services.SostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SostaController {

    private final SostaService sostaService;

    public SostaController(SostaService sostaService) {
        this.sostaService = sostaService;
    }

    @PostMapping("/sosta")
    public ResponseEntity<String> richiestaSosta(@RequestBody RichiestaSosta richiestaSosta) {
        sostaService.richiestaSosta(richiestaSosta);
        return ResponseEntity
                .ok("Richiesta di sosta ricevuta per veicolo " + richiestaSosta.getVeicoloId());
    }
}
