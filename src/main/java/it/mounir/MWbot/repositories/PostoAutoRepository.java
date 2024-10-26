package it.mounir.MWbot.repositories;

import it.mounir.MWbot.model.PostoAuto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostoAutoRepository extends CrudRepository<PostoAuto, Long> {
}
