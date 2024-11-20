package it.mounir.MWbot.DTO;

import it.mounir.MWbot.domain.TipoServizio;

public abstract class Richiesta {

    private int idUtente;
    private String veicoloId;
    private TipoServizio tipoServizio;

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getVeicoloId() {
        return veicoloId;
    }

    public void setVeicoloId(String veicoloId) {
        this.veicoloId = veicoloId;
    }

    public TipoServizio getTipoServizio() {
        return tipoServizio;
    }

    public void setTipoServizio(TipoServizio tipoServizio) {
        this.tipoServizio = tipoServizio;
    }
}
