package it.mounir.MWbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.services.PagamentoRepositoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "Pagamento API", description = "Gestione dei pagamenti")
public class PagamentoController {

    private final PagamentoRepositoryService pagamentoRepositoryService;

    public PagamentoController(PagamentoRepositoryService pagamentoRepositoryService) {
        this.pagamentoRepositoryService = pagamentoRepositoryService;
    }

    @GetMapping("/filtra/pagamenti")
    @Operation(
            summary = "Visualizza i pagamenti",
            description = "Restituisce un elenco di pagamenti filtrati per data di inizio, data di fine e tipo di servizio.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista di pagamenti restituita con successo",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Pagamento.class))),
                    @ApiResponse(responseCode = "400", description = "Richiesta non valida"),
                    @ApiResponse(responseCode = "500", description = "Errore interno del server")
            }
    )
    public ResponseEntity<List<Pagamento>> visualizzaPagamenti(
            @Parameter(description = "Data di inizio", example = "2024-03-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInizio,

            @Parameter(description = "Data di fine", example = "2024-03-10T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFine,

            @Parameter(description = "Tipo di servizio", example = "STANDARD")
            @RequestParam String tipoServizio){
        return ResponseEntity
                .ok(pagamentoRepositoryService.visualizzaPagamenti(dataInizio, dataFine, TipoServizio.valueOf(tipoServizio)));
    }

    @PostMapping("/pagamento")
    @Operation(
            summary = "Crea un nuovo pagamento",
            description = "Aggiunge un nuovo pagamento al sistema e restituisce l'oggetto creato.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento creato con successo",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Pagamento.class))),
                    @ApiResponse(responseCode = "400", description = "Dati di pagamento non validi"),
                    @ApiResponse(responseCode = "500", description = "Errore interno del server")
            }
    )
    public ResponseEntity<Pagamento> createPayment(@RequestBody Pagamento pagamento) {
        return ResponseEntity
                .ok(pagamentoRepositoryService.createOrUpdatePagamento(pagamento));
    }

    @GetMapping("/pagamenti")
    @Operation(
            summary = "Recupera tutti i pagamenti",
            description = "Restituisce tutti i pagamenti presenti nel sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista di pagamenti restituita con successo",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Pagamento.class))),
                    @ApiResponse(responseCode = "500", description = "Errore interno del server")
            }
    )
    public ResponseEntity<Iterable<Pagamento>> getAllPayments() {
        return ResponseEntity
                .ok(pagamentoRepositoryService.getAllPagamenti());
    }
}
