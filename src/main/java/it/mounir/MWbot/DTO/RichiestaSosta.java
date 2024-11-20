package it.mounir.MWbot.DTO;

public class RichiestaSosta {
    private int idUtente;
    private String veicoloId;
    private int tempoSosta;

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

    public int getTempoSosta() {
        return tempoSosta;
    }

    public void setTempoSosta(int tempoSosta) {
        this.tempoSosta = tempoSosta;
    }
}
