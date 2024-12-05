package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Parcheggio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcheggioRepository extends CrudRepository<Parcheggio, Long> {

    @Query("SELECT costososta FROM parcheggio WHERE id_parcheggio = :id")
    int getCostoSostaById(Long id);

    @Query("SELECT costoKw FROM parcheggio WHERE id_parcheggio = :id")
    int getCostoKwById(Long id);
}
