package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Parcheggio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcheggioRepository extends CrudRepository<Parcheggio, Long> {
}
