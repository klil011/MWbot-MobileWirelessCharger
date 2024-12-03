package it.mounir.MWbot.domain;

public class OccupazionePosto {
    private int idRichiesta;
    private int idUtente;
    private long tempo;
    private TipoServizio tipoServizio;

    public OccupazionePosto(int idRichiesta, int idUtente, TipoServizio tipoServizio, long tempo) {

        this.idRichiesta = idRichiesta;
        this.idUtente = idUtente;
        this.tipoServizio = tipoServizio;
        this.tempo = tempo;
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
}
