package it.mounir.MWbot.controllers;

import it.mounir.MWbot.model.Prenotazione;
import it.mounir.MWbot.services.PrenotazioneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping("/prenotazione")
    public Prenotazione createPrenotazione(@RequestBody Prenotazione prenotazione) {
            return prenotazioneService.createOrUpdatePrenotazione(prenotazione);
    }

    @DeleteMapping("/prenotazione/{id}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) {
        boolean eliminato = prenotazioneService.deletePrenotazioneById(id);
        if(eliminato) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/prenotazioni/{id}")
    public List<Prenotazione> getAllPrenotazioniByUtente(@PathVariable Long id) {
        return prenotazioneService.findPrenotazioniByUtente(id);
    }

    @GetMapping("/prenotazioni")
    public Iterable<Prenotazione> getAllPrenotazioni() {
        return prenotazioneService.getAllPrenotazioni();
    }

}
