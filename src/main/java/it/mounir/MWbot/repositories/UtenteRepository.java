package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Utente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Long> {

}
