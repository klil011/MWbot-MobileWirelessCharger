package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Mwbot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MwbotRepository extends CrudRepository<Mwbot, Long> {
}
