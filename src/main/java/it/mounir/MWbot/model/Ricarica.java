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
    private int idVeicolo;

    @Column("id_prenotazione")
    private int idPrenotazione;

    @Column("id_utente")
    private int idUtente;

    @Column("id_mwbot")
    private int idMwbot;

    private int percentualeRicaricare;

    private int stato;

    public int getIdRicarica() {
        return idRicarica;
    }

    public void setIdRicarica(int idRicarica) {
        this.idRicarica = idRicarica;
    }

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    public int getIdVeicolo() {
        return idVeicolo;
    }

    public void setIdVeicolo(int idVeicolo) {
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
}
