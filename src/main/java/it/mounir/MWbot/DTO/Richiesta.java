package it.mounir.MWbot.DTO;

import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Ricarica;

public abstract class Richiesta {

    private int idRichiesta;
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

    @Override
    public String toString(){
        return ("idUtente: " + this.getIdUtente() +"\n"+
                "veicoloId: " + this.getVeicoloId() +"\n"+
                "servizio: " + this.getTipoServizio());

    }

    public int getIdRichiesta() {
        return idRichiesta;
    }

    public void setIdRichiesta(int idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

}
