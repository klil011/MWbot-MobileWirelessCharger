package it.mounir.MWbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mounir.MWbot.model.Prenotazione;
import it.mounir.MWbot.services.ParcheggioService;
import it.mounir.MWbot.services.PrenotazioneRepositoryService;
import it.mounir.MWbot.services.PrenotazioneService;
import it.mounir.MWbot.services.UtenteService;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Prenotazioni API", description = "Gestione delle prenotazioni nel parcheggio")
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
    @Operation(
            summary = "Crea una nuova prenotazione",
            description = "Permette agli utenti premium di prenotare un posto nel parcheggio",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Prenotazione creata"),
                    @ApiResponse(responseCode = "403", description = "L'utente non è premium"),
                    @ApiResponse(responseCode = "406", description = "Nessun posto disponibile")
            }
    )
    public ResponseEntity<String> createPrenotazione(@RequestBody Prenotazione prenotazione) {

        /*
         * TODO: richiamare prenotazioneService pre controllare che l'utente sia effettivamente di tipo premium,
         *  se si allora salvare la prenotazione, altrimenti restituisci un messaggio di errore.*/

        if (utenteService.isUtentePremium(prenotazione.getIdUtente()) != 1) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("vincolo tipo utente premium non soddisfatto");
        }

        if (!parcheggioService.disponibilitaPrenotazione(prenotazione.getOrarioInizio(), prenotazione.getOrarioFine())
                && parcheggioService.getPostiLiberiCount() < 1) {

            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("nessun posto libero dispnibile per essere prenotato");
        }

        Prenotazione prenotazioneSalvata = prenotazioneService.gestisciPrenotazione(prenotazione);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prenotazioneSalvata.getIdPrenotazione())
                .toUri();

        return ResponseEntity
                .created(location)
                .body("Prenotazione effettuata");

    }

    @DeleteMapping("/prenotazione/{id}")
    @Operation(
            summary = "Elimina una prenotazione",
            description = "Permette di eliminare una prenotazione esistente",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Prenotazione eliminata con successo"),
                    @ApiResponse(responseCode = "404", description = "Prenotazione non trovata")
            }
    )
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) {
        boolean eliminato = prenotazioneRepositoryService.deletePrenotazioneById(id);
        if(eliminato) {
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @GetMapping("/prenotazioni/{id}")
    @Operation(
            summary = "Recupera le prenotazioni di un utente",
            description = "Restituisce l'elenco delle prenotazioni effettuate da un utente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Elenco delle prenotazioni restituito con successo")
            }
    )
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioniByUtente(@PathVariable Long id) {
        return ResponseEntity
                .ok(prenotazioneRepositoryService.findPrenotazioniByUtente(id));
    }

    @GetMapping("/prenotazioni")
    @Operation(
            summary = "Recupera tutte le prenotazioni",
            description = "Restituisce l'elenco completo delle prenotazioni",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Elenco delle prenotazioni restituito con successo")
            }
    )
    public ResponseEntity<Iterable<Prenotazione>> getAllPrenotazioni() {
        return ResponseEntity
                .ok(prenotazioneRepositoryService.getAllPrenotazioni());
    }

}
