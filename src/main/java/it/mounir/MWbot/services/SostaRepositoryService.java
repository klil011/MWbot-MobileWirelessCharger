package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Sosta;
import it.mounir.MWbot.repositories.SostaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SostaRepositoryService {

    private final SostaRepository sostaRepository;

    public SostaRepositoryService(SostaRepository sostaRepository) {
        this.sostaRepository = sostaRepository;
    }

    public Sosta createOrUpdateSosta (Sosta servizioSosta) {
        return sostaRepository.save(servizioSosta);
    }

    public Optional<Sosta> getSostaById(Long id) {
        return sostaRepository.findById(id);
    }
}
