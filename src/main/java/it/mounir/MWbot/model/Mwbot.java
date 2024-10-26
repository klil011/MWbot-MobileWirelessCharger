package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("mwbot")
public class Mwbot {

    @Id
    @Column("id_mwbot")
    private int idMwbot;

    @Column("id_parcheggio")
    private int idParcheggio;

    public int getIdMwbot() {
        return idMwbot;
    }

    public void setIdMwbot(int idMwbot) {
        this.idMwbot = idMwbot;
    }

    public int getIdParcheggio() {
        return idParcheggio;
    }

    public void setIdParcheggio(int idParcheggio) {
        this.idParcheggio = idParcheggio;
    }
}
