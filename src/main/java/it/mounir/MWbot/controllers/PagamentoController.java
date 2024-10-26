package it.mounir.MWbot.controllers;

import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.services.PagamentoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/pagamento")
    public Pagamento createPayment(@RequestBody Pagamento pagamento) {
        return pagamentoService.createOrUpdatePagamento(pagamento);
    }

    @GetMapping("/pagamenti")
    public Iterable<Pagamento> getAllPayments() {
        return pagamentoService.getAllPagamenti();
    }
}
