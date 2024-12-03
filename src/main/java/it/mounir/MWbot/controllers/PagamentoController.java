package it.mounir.MWbot.controllers;

import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.services.PagamentoRepositoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PagamentoController {

    private final PagamentoRepositoryService pagamentoRepositoryService;

    public PagamentoController(PagamentoRepositoryService pagamentoRepositoryService) {
        this.pagamentoRepositoryService = pagamentoRepositoryService;
    }

    @PostMapping("/pagamento")
    public Pagamento createPayment(@RequestBody Pagamento pagamento) {
        return pagamentoRepositoryService.createOrUpdatePagamento(pagamento);
    }

    @GetMapping("/pagamenti")
    public Iterable<Pagamento> getAllPayments() {
        return pagamentoRepositoryService.getAllPagamenti();
    }
}
