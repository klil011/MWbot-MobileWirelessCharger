package it.mounir.MWbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Ricarica API", description = "Gestione delle richieste di ricarica per i veicoli elettrici")
public class RicaricaController {

    private final RicaricaService ricaricaService;

    public RicaricaController(RicaricaService ricaricaService) {
        this.ricaricaService = ricaricaService;
    }

    @PostMapping("/ricarica")
    @Operation(
            summary = "Richiede una ricarica per un veicolo",
            description = "Permette di richiedere la ricarica per un veicolo elettrico. L'endpoint restituisce l'ID della ricarica creata.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Richiesta di ricarica ricevuta",
                            content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi")
            }
    )
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
