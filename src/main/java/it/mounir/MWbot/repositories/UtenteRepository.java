package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Utente;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Long> {

    @Query("SELECT COUNT(*) FROM UTENTE WHERE id_utente = :id AND tipo_utente = 1")
    int isUtentePremium(Long id);
}
