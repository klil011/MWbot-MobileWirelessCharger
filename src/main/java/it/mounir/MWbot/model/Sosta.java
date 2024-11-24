package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("sosta")
public class Sosta {

    @Id
    @Column("id_sosta")
    private int idSosta;

    @Column("id_utente")
    private int idUtente;

    @Column("id_veicolo")
    private String idVeicolo;

    @Column("tempo_sosta")
    private int tempoSosta;

    private int stato;

    public String getIdVeicolo() {
        return idVeicolo;
    }

    public void setIdVeicolo(String idVeicolo) {
        this.idVeicolo = idVeicolo;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getIdSosta() {
        return idSosta;
    }

    public void setIdSosta(int idSosta) {
        this.idSosta = idSosta;
    }

    public int getTempoSosta() {
        return tempoSosta;
    }

    public void setTempoSosta(int tempoSosta) {
        this.tempoSosta = tempoSosta;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }
}
