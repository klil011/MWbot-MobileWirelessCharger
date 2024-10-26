package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("posto_auto")
public class PostoAuto {

    @Id
    @Column("id_posto_auto")
    private int idPostoAuto;

    private int stato;

    @Column("id_veicolo")
    private int idVeicolo;

    @Column("id_parcheggio")
    private int idParcheggio;

    public int getIdPostoAuto() {
        return idPostoAuto;
    }

    public void setIdPostoAuto(int idPostoAuto) {
        this.idPostoAuto = idPostoAuto;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public int getIdVeicolo() {
        return idVeicolo;
    }

    public void setIdVeicolo(int idVeicolo) {
        this.idVeicolo = idVeicolo;
    }

    public int getIdParcheggio() {
        return idParcheggio;
    }

    public void setIdParcheggio(int idParcheggio) {
        this.idParcheggio = idParcheggio;
    }
}
