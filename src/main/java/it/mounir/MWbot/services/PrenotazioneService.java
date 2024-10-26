package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Prenotazione;
import it.mounir.MWbot.repositories.PrenotazioneRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public Prenotazione createOrUpdatePrenotazione(Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }

    public boolean deletePrenotazioneById(Long id) {
        if(prenotazioneRepository.existsById(id)) {
            prenotazioneRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Prenotazione> getPrenotazioneById(Long id) {
        return prenotazioneRepository.findById(id);
    }

    public Iterable<Prenotazione> getAllPrenotazioni() {
        return prenotazioneRepository.findAll();
    }

    public List<Prenotazione> findPrenotazioniByUtente(Long id) {
        return prenotazioneRepository.findPrenotazioniByUtente(id);
    }
}
