package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("parcheggio")
public class Parcheggio {

    @Id
    @Column("id_veicolo")
    private int idParcheggio;

    private int capacita;

    @Column("posti_occupati")
    private int postiOccupati;

    private int costoKw;

    public int getIdParcheggio() {
        return idParcheggio;
    }

    public void setIdParcheggio(int idParcheggio) {
        this.idParcheggio = idParcheggio;
    }

    public int getCapacita() {
        return capacita;
    }

    public void setCapacita(int capacita) {
        this.capacita = capacita;
    }

    public int getPostiOccupati() {
        return postiOccupati;
    }

    public void setPostiOccupati(int postiOccupati) {
        this.postiOccupati = postiOccupati;
    }

    public int getCostoKw() {
        return costoKw;
    }

    public void setCostoKw(int costoKw) {
        this.costoKw = costoKw;
    }
}
