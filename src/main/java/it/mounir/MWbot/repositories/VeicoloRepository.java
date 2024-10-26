package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Veicolo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeicoloRepository extends CrudRepository<VeicoloRepository, Long> {
}
