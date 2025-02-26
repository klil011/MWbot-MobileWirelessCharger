package it.mounir.MWbot.controllers;

import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.services.PagamentoRepositoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PagamentoController {

    private final PagamentoRepositoryService pagamentoRepositoryService;

    public PagamentoController(PagamentoRepositoryService pagamentoRepositoryService) {
        this.pagamentoRepositoryService = pagamentoRepositoryService;
    }

    @GetMapping("/visualizza/pagamenti")
    public ResponseEntity<List<Pagamento>> visualizzaPagamenti(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFine,
            @RequestParam String tipoServizio){
        return ResponseEntity
                .ok(pagamentoRepositoryService.visualizzaPagamenti(dataInizio, dataFine, TipoServizio.valueOf(tipoServizio)));
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Pagamento> createPayment(@RequestBody Pagamento pagamento) {
        return ResponseEntity
                .ok(pagamentoRepositoryService.createOrUpdatePagamento(pagamento));
    }

    @GetMapping("/pagamenti")
    public ResponseEntity<Iterable<Pagamento>> getAllPayments() {
        return ResponseEntity
                .ok(pagamentoRepositoryService.getAllPagamenti());
    }
}
