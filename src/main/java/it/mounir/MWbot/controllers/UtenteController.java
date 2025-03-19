package it.mounir.MWbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mounir.MWbot.model.Utente;
import it.mounir.MWbot.services.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Utenti", description = "Gestione degli account utente")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/user")
    @Operation(
            summary = "Crea un nuovo utente",
            description = "Registra un nuovo utente o aggiorna un account esistente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utente creato o aggiornato con successo",
                            content = @Content(schema = @Schema(implementation = Utente.class))),
                    @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi")
            }
    )
    public ResponseEntity<Utente> createUser(@RequestBody Utente utente) {
        return ResponseEntity
                .ok(utenteService.createOrUpdateAccount(utente));
    }

    @GetMapping("/users")
    @Operation(
            summary = "Recupera tutti gli utenti",
            description = "Restituisce la lista completa degli utenti registrati.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista di utenti recuperata con successo",
                            content = @Content(schema = @Schema(implementation = Utente.class)))
            }
    )
    public ResponseEntity<Iterable<Utente>> getAllUsers() {
        return ResponseEntity
                .ok(utenteService.getAllUsers());
    }
}
