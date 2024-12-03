package it.mounir.MWbot.services;

import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.model.Ricarica;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PagamentoService {

    private double costoKw = 2;
    private double costoOra = 2;
    private final PagamentoRepositoryService pagamentoRepositoryService;

    public PagamentoService(PagamentoRepositoryService pagamentoRepositoryService) {
        this.pagamentoRepositoryService = pagamentoRepositoryService;
    }

    public double calcolaImporto(int tempo, TipoServizio servizio, long idUtente) {
        double importo = 0;
        if(servizio.equals(TipoServizio.SOSTA)) {
            importo = tempo * costoOra;
        }
        else {
            importo = tempo * costoKw;
        }

        Pagamento pagamento = this.creaOggettoPagamento(idUtente, importo, servizio.ordinal());
        this.salvaPagamento(pagamento);
        return importo;
    }

    private Pagamento salvaPagamento(Pagamento pagamento) {
        try{
            Pagamento pagamentoSalvato = pagamentoRepositoryService.createOrUpdatePagamento(pagamento);
            return pagamento;

        }catch(RuntimeException e){
            System.err.println("Errore durante l'interazione con il DB: " + e.getMessage());
            throw new RuntimeException("Non è stato possibile completare la richiesta di pagamento", e);
        }
    }

    private Pagamento creaOggettoPagamento(long idUtente, double importo, int servizio) {
        Pagamento pagamento = new Pagamento();
        pagamento.setIdUtente((int)idUtente);
        pagamento.setImporto(importo);
        pagamento.setServizio(servizio);
        pagamento.setDataOraPagamento(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        return pagamento;
    }

    public double getCostoKw() {
        return costoKw;
    }

    public void setCostoKw(double costoKw) {
        this.costoKw = costoKw;
    }

    public double getCostoOra() {
        return costoOra;
    }

    public void setCostoOra(double costoOra) {
        this.costoOra = costoOra;
    }
}
