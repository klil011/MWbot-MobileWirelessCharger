package it.mounir.MWbot.controllers;

import it.mounir.MWbot.model.Parcheggio;
import it.mounir.MWbot.services.CodaService;
import it.mounir.MWbot.services.ParcheggioRepositoryService;
import it.mounir.MWbot.services.ParcheggioService;
import it.mounir.MWbot.services.SostaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String monitor() {
        if(parcheggioService.getPostiLiberiCount() == 0) {
            return "Tutti i posti sono al momento occupati, ci sono " + codaService.getCodaRichiesteCount()
                    + " veicoli in coda.";
        }

        return "Parcheggi disponibili: " + parcheggioService.getPostiLiberiCount();
    }

    @PostMapping("/aggiorna/costo/orario")
    public String aggiornaCostoOrario(@RequestParam int idParcheggio, @RequestParam int costoSosta) {
        parcheggioRepositoryService.updateCostoSostaById(idParcheggio, costoSosta);
        return "Costo sosta orario aggiornato con successo !";
    }

    @PostMapping("/aggiorna/costo/kw")
    public String aggiornaCostoKw(@RequestParam int idParcheggio, @RequestParam int costoKw) {
        parcheggioRepositoryService.updateCostoKwById(idParcheggio, costoKw);
        return "Costo di ricarica aggiornato con successo !";
    }

}
