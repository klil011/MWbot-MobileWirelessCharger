package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("pagamento")
public class Pagamento {

    @Id
    @Column("id_pagamento")
    private int idPagamento;

    @Column("id_utente")
    private int idUtente;

    private int importo;

    private int servizio;

    @Column("data_ora_pagamento")
    private LocalDateTime dataOraPagamento;

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getImporto() {
        return importo;
    }

    public void setImporto(int importo) {
        this.importo = importo;
    }

    public int getServizio() {
        return servizio;
    }

    public void setServizio(int servizio) {
        this.servizio = servizio;
    }

    public LocalDateTime getDataOraPagamento() {
        return dataOraPagamento;
    }

    public void setDataOraPagamento(LocalDateTime dataOraPagamento) {
        this.dataOraPagamento = dataOraPagamento;
    }
}
