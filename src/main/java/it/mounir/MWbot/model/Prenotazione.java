package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("prenotazione")
public class Prenotazione {

    @Id
    @Column("id_prenotazione")
    private int idPrenotazione;

    @Column("id_utente")
    private int idUtente;

    @Column("orario_prenotazione")
    private LocalDateTime orarioPrenotazione;

    @Column("orario_effettivo")
    private LocalDateTime orarioEffettivo;

    private int  durata;

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(int idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public LocalDateTime getOrarioPrenotazione() {
        return orarioPrenotazione;
    }

    public void setOrarioPrenotazione(LocalDateTime orarioPrenotazione) {
        this.orarioPrenotazione = orarioPrenotazione;
    }

    public LocalDateTime getOrarioEffettivo() {
        return orarioEffettivo;
    }

    public void setOrarioEffettivo(LocalDateTime orarioEffettivo) {
        this.orarioEffettivo = orarioEffettivo;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }
}
