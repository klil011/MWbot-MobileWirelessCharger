package it.mounir.MWbot.controllers;

import it.mounir.MWbot.services.CodaService;
import it.mounir.MWbot.services.ParcheggioService;
import it.mounir.MWbot.services.SostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class ParcheggioController {

    @Autowired
    private final ParcheggioService parcheggioService;
    @Autowired
    private final CodaService codaService;

    public ParcheggioController(ParcheggioService parcheggioService, SostaService sostaService, CodaService codaService) {
        this.parcheggioService = parcheggioService;
        this.codaService = codaService;
    }

    @GetMapping("/numeroPostiLiberi")
    public String numeroPostiLiberi() {
        return "Posti liberi: " + parcheggioService.getPostiLiberiCount();
    }

    @GetMapping("/monitor")
    public String monitor() {
        if(parcheggioService.getPostiLiberiCount() == 0) {
            return "Tutti i posti sono al momento occupati, ci sono " + codaService.getCodaRichiesteCount()
                    + " veicoli in coda.";
        }

        return "Parcheggi disponibili: " + parcheggioService.getPostiLiberiCount();
    }

}
