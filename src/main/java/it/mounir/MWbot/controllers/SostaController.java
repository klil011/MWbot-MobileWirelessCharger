package it.mounir.MWbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Sosta API", description = "Gestione delle richieste di sosta per i veicoli")
public class SostaController {

    private final SostaService sostaService;

    public SostaController(SostaService sostaService) {
        this.sostaService = sostaService;
    }

    @PostMapping("/sosta")
    @Operation(
            summary = "Richiede la sosta per un veicolo",
            description = "Permette di richiedere la sosta per un veicolo. L'endpoint restituisce l'ID della sosta creata",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Richiesta di sosta ricevuta",
                            content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi")
            }
    )
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
