package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Ricarica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RicaricaRepository extends CrudRepository<Ricarica, Long> {
}
