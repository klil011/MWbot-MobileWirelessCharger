package it.mounir.MWbot.DTO;

public class RichiestaRicarica {
    private String veicoloId;
    private Boolean riceviMessaggio;
    private int percentualeIniziale;
    private int percentualeDesiderata;

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

    public int getPercentualeDesiderata() {
        return percentualeDesiderata;
    }

    public void setPercentualeDesiderata(int percentualeDesiderata) {
        this.percentualeDesiderata = percentualeDesiderata;
    }

    public int getPercentualeIniziale() {
        return percentualeIniziale;
    }

    public void setPercentualeIniziale(int percentualeIniziale) {
        this.percentualeIniziale = percentualeIniziale;
    }
}
