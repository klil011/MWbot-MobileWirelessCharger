package it.mounir.MWbot.domain;

public class OccupazionePosto {
    private int idRichiesta;
    private long tempo;
    private TipoServizio tipoServizio;

    public OccupazionePosto(int idRichiesta, TipoServizio tipoServizio, long tempo) {
        this.idRichiesta = idRichiesta;
        this.tipoServizio = tipoServizio;
        this.tempo = tempo;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }

    public int getIdRichiesta() {
        return idRichiesta;
    }

    public void setIdRichiesta(int idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

    public TipoServizio getTipoServizio() {
        return tipoServizio;
    }

    public void setTipoServizio(TipoServizio tipoServizio) {
        this.tipoServizio = tipoServizio;
    }
}
