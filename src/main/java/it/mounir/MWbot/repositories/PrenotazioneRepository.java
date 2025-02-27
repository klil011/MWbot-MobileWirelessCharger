package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Prenotazione;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Long> {

    @Query("SELECT * FROM prenotazione WHERE id_utente = :id")
    List<Prenotazione> findPrenotazioniByUtente(Long id);

}
