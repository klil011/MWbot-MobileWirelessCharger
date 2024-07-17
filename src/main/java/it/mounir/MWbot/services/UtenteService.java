package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Utente;
import it.mounir.MWbot.repositories.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente createOrUpdateAccount(Utente account) {
        return utenteRepository.save(account);
    }

    public Optional<Utente> getAccount(Long id) {
        return utenteRepository.findById(id);
    }

    public Iterable<Utente> getAllUsers() {
        return utenteRepository.findAll();
    }

    public void deleteAccount(Long id) {
        utenteRepository.deleteById(id);
    }
}
