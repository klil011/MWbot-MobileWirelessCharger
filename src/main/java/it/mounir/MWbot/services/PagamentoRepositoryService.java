package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.repositories.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentoRepositoryService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoRepositoryService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Pagamento createOrUpdatePagamento(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public Optional<Pagamento> getPagamento(Long id) {
        return pagamentoRepository.findById(id);
    }

    public Iterable<Pagamento> getAllPagamenti() {
        return pagamentoRepository.findAll();
    }
}
