package it.mounir.MWbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("utente")
public class Utente {

    @Id
    @Column("id_utente")
    private Long idUtente;

    private String nome;
    private String cognome;

    @Column("tipo_utente")
    private int tipoUtente;

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public int getTipoUtente() {
        return tipoUtente;
    }

    public void setTipoUtente(int tipoUtente) {
        this.tipoUtente = tipoUtente;
    }
}
