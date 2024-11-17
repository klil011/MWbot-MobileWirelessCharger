package it.mounir.MWbot.controllers;

import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.services.RicaricaService;
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
    public String richiestaRicarica(@RequestBody RichiestaRicarica richiestaRicarica) {
        ricaricaService.richiestaRicarica(richiestaRicarica);

        //System.out.println(richiestaRicarica.getRiceviMessaggio());
        return "Richiesta di ricarica ricevuta per veicolo " + richiestaRicarica.getVeicoloId();
    }
}
