package it.mounir.MWbot.DTO;

public class RichiestaRicarica {
    private String veicoloId;
    private Boolean riceviMessaggio;

    public String getVeicoloId() {
        return veicoloId;
    }

    public void setVeicoloId(String veicoloId) {
        this.veicoloId = veicoloId;
    }

    public Boolean getRiceviMessaggio() {
        return riceviMessaggio;
    }

    public void setRiceviMessaggio(Boolean riceviMessaggio) {
        this.riceviMessaggio = riceviMessaggio;
    }
}
