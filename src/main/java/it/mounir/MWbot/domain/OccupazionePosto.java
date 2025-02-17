package it.mounir.MWbot.domain;

import java.time.LocalDateTime;

public class OccupazionePosto {
    private int idRichiesta;
    private int idUtente;
    private long tempo;
    private TipoServizio tipoServizio;
    private LocalDateTime inizio;
    private LocalDateTime fine;

    public OccupazionePosto(int idRichiesta, int idUtente, TipoServizio tipoServizio, long tempo, LocalDateTime inizio, LocalDateTime fine) {

        this.idRichiesta = idRichiesta;
        this.idUtente = idUtente;
        this.tipoServizio = tipoServizio;
        this.tempo = tempo;
        this.inizio = inizio;
        this.fine = fine;
    }

    public long getTempo() {
        return tempo;
    }

    public int getIdRichiesta() {
        return idRichiesta;
    }

    public TipoServizio getTipoServizio() {
        return tipoServizio;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public LocalDateTime getFine() {
        return fine;
    }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public boolean isPrenotazione() {
        return inizio != null && fine != null;
    }

}
