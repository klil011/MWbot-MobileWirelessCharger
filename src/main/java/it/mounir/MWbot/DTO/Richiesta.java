package it.mounir.MWbot.DTO;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.mounir.MWbot.domain.TipoServizio;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipoServizio", visible = true)  // Usa il campo 'tipoServizio' come discriminatore
@JsonSubTypes({
        @JsonSubTypes.Type(value = RichiestaRicarica.class, name = "RICARICA"),
        @JsonSubTypes.Type(value = RichiestaSosta.class, name = "SOSTA")
})
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

    public Boolean isRicarica(Richiesta richiesta) {
        return richiesta instanceof RichiestaRicarica;
    }

}
