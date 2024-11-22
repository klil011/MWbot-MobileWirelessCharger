package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("parcheggio")
public class Parcheggio {

    @Id
    @Column("id_parcheggio")
    private int idParcheggio;

    @Column("capacità")
    private int capacita;

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

    public int getCostoKw() {
        return costoKw;
    }

    public void setCostoKw(int costoKw) {
        this.costoKw = costoKw;
    }
}
