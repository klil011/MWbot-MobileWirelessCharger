package it.mounir.MWbot.domain;

public class RichiestaVeicolo {

    private final int idUtente;
    private final String veicoloId;
    private final boolean preferenzaMessaggio;
    private final Enum<TipoServizio> tipoServizio; // "RICARICA" o "SOSTA"

    public RichiestaVeicolo(int idUtente, String veicoloId, boolean preferenzaMessaggio, Enum<TipoServizio> tipoServizio) {
        this.idUtente = idUtente;
        this.veicoloId = veicoloId;
        this.preferenzaMessaggio = preferenzaMessaggio;
        this.tipoServizio = tipoServizio;
    }

    public String getVeicoloId() {
        return veicoloId;
    }

    public boolean isPreferenzaMessaggio() {
        return preferenzaMessaggio;
    }

    public Enum<TipoServizio> getTipoServizio() {
        return tipoServizio;
    }
}
