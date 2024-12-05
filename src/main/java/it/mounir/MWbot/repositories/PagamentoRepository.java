package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Pagamento;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {

    @Query("SELECT * FROM pagamento WHERE data_ora_pagamento BETWEEN :dataInizio AND :dataFine AND servizio = :tipoServizio")
    List<Pagamento> visualizzaPagamenti(LocalDateTime dataInizio, LocalDateTime  dataFine, int tipoServizio);
}
