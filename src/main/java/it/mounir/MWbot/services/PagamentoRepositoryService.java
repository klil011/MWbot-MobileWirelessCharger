package it.mounir.MWbot.services;

import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.repositories.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public List<Pagamento> visualizzaPagamenti(LocalDateTime dataInizio, LocalDateTime dataFine, TipoServizio tipoServizio) {
        return pagamentoRepository.visualizzaPagamenti(dataInizio, dataFine, tipoServizio.ordinal());
    }
}
