package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.Richiesta;
import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.DTO.RichiestaSosta;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Prenotazione;
import it.mounir.MWbot.repositories.PrenotazioneRepository;
import it.mounir.MWbot.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteService utenteService;

    private final ParcheggioService parcheggioService;
    private final RicaricaService ricaricaService;
    private final SostaService sostaService;

    @Autowired
    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, JdbcTemplate jdbcTemplate, UtenteService utenteService, ParcheggioService parcheggioService, RicaricaService ricaricaService, SostaService sostaService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.utenteService = utenteService;
        this.parcheggioService = parcheggioService;
        this.ricaricaService = ricaricaService;
        this.sostaService = sostaService;
    }

    public void gestisciPrenotazione (Prenotazione prenotazione) {

        Richiesta richiesta = prenotazione.getRichiesta();
        
        richiesta.setInizio(prenotazione.getOrarioInizio());
        richiesta.setFine(prenotazione.getOrarioFine());

        if (richiesta instanceof RichiestaRicarica) {
            ricaricaService.richiestaRicarica((RichiestaRicarica) richiesta);
        } else if (richiesta instanceof RichiestaSosta) {
            sostaService.richiestaSosta((RichiestaSosta) richiesta);
        } else {
            throw new IllegalArgumentException("Tipo di richiesta non supportato");
        }

    }

}
