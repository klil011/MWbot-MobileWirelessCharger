package it.mounir.MWbot.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("sosta")
public class Sosta {

    @Column("id_sosta")
    private int idSosta;

    @Column("id_utente")
    private int idUtente;

    @Column("id_veicolo")
    private int idVeicolo;

    public int getIdVeicolo() {
        return idVeicolo;
    }

    public void setIdVeicolo(int idVeicolo) {
        this.idVeicolo = idVeicolo;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getIdSosta() {
        return idSosta;
    }

    public void setIdSosta(int idSosta) {
        this.idSosta = idSosta;
    }
}
