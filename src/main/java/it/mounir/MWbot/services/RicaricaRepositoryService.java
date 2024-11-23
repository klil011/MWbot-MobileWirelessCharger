package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.repositories.RicaricaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RicaricaRepositoryService {

    private final RicaricaRepository ricaricaRepository;

    public RicaricaRepositoryService(RicaricaRepository ricaricaRepository) {
        this.ricaricaRepository = ricaricaRepository;
    }

    public Ricarica createOrUpdateRicarica(Ricarica ricarica) {
        return ricaricaRepository.save(ricarica);
    }

    public Optional<Ricarica> getRicaricaById(Long id) {
        return ricaricaRepository.findById(id);
    }

    public Iterable<Ricarica> getAllRicariche() {
        return ricaricaRepository.findAll();
    }
}
