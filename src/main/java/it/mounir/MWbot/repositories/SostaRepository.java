package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.Sosta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SostaRepository extends CrudRepository<Sosta, Long> {

}
