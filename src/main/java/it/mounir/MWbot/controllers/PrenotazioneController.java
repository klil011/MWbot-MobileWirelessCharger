package it.mounir.MWbot.controllers;

import it.mounir.MWbot.model.Prenotazione;
import it.mounir.MWbot.services.ParcheggioService;
import it.mounir.MWbot.services.PrenotazioneRepositoryService;
import it.mounir.MWbot.services.PrenotazioneService;
import it.mounir.MWbot.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;
    private final PrenotazioneRepositoryService prenotazioneRepositoryService;
    private final UtenteService utenteService;
    private final ParcheggioService parcheggioService;

    public PrenotazioneController(PrenotazioneService prenotazioneService, PrenotazioneRepositoryService prenotazioneRepositoryService, UtenteService utenteService, ParcheggioService parcheggioService) {
        this.prenotazioneService = prenotazioneService;
        this.prenotazioneRepositoryService = prenotazioneRepositoryService;
        this.utenteService = utenteService;
        this.parcheggioService = parcheggioService;
    }

    @PostMapping("/prenotazione")
    public ResponseEntity<String> createPrenotazione(@RequestBody Prenotazione prenotazione) {

        /*
        * TODO: richiamare prenotazioneService pre controllare che l'utente sia effettivamente di tipo premium,
        *  se si allora salvare la prenotazione, altrimenti restituisci un messaggio di errore.*/

        if(utenteService.isUtentePremium(prenotazione.getIdUtente()) > 0) {

            if (parcheggioService.getPostiLiberiCount() < 1) {
                return ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE)
                        .body("nessun posto libero dispnibile per essere prenotato");
            }
            prenotazioneService.gestisciPrenotazione(prenotazione);

            //FIXME: in realtà è completata la prenotazione se poi effettimente non entra in conflitto con un altra prenotazione
            return ResponseEntity.ok("Prenotazione effettuata");
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("vincolo tipo utente premium non soddisfatto");
    }

    @DeleteMapping("/prenotazione/{id}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) {
        boolean eliminato = prenotazioneRepositoryService.deletePrenotazioneById(id);
        if(eliminato) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/prenotazioni/{id}")
    public List<Prenotazione> getAllPrenotazioniByUtente(@PathVariable Long id) {
        return prenotazioneRepositoryService.findPrenotazioniByUtente(id);
    }

    @GetMapping("/prenotazioni")
    public Iterable<Prenotazione> getAllPrenotazioni() {
        return prenotazioneRepositoryService.getAllPrenotazioni();
    }

}
