package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ricarica")
public class Ricarica {

    @Id
    @Column("id_ricarica")
    private int idRicarica;

    @Column("id_veicolo")
    private String idVeicolo;

    @Column("id_prenotazione")
    private Integer idPrenotazione;

    @Column("id_utente")
    private int idUtente;

    @Column("id_mwbot")
    private int idMwbot;

    @Column("percentuale_ricaricare")
    private int percentualeRicaricare;

    @Column("percentuale_iniziale")
    private int percentualeIniziale;

    private int stato;

    public int getIdRicarica() {
        return idRicarica;
    }

    public void setIdRicarica(int idRicarica) {
        this.idRicarica = idRicarica;
    }

    public Integer getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(Integer idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

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

    public int getIdMwbot() {
        return idMwbot;
    }

    public void setIdMwbot(int idMwbot) {
        this.idMwbot = idMwbot;
    }

    public int getPercentualeRicaricare() {
        return percentualeRicaricare;
    }

    public void setPercentualeRicaricare(int percentualeRicaricare) {
        this.percentualeRicaricare = percentualeRicaricare;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public int getPercentualeIniziale() { return percentualeIniziale; }

    public void setPercentualeIniziale(int percentualeIniziale) { this.percentualeIniziale = percentualeIniziale; }
}
