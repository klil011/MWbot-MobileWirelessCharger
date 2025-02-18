package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.Richiesta;
import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.DTO.RichiestaSosta;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Prenotazione;
import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.repositories.PrenotazioneRepository;
import it.mounir.MWbot.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrenotazioneService {

    private final RicaricaService ricaricaService;
    private final SostaService sostaService;
    private final PrenotazioneRepositoryService prenotazioneRepositoryService;

    @Autowired
    public PrenotazioneService(JdbcTemplate jdbcTemplate, RicaricaService ricaricaService, SostaService sostaService, PrenotazioneRepositoryService prenotazioneRepositoryService) {
        this.ricaricaService = ricaricaService;
        this.sostaService = sostaService;
        this.prenotazioneRepositoryService = prenotazioneRepositoryService;
    }

    public void gestisciPrenotazione (Prenotazione prenotazione) {

        Richiesta richiesta = prenotazione.getRichiesta();
        
        richiesta.setInizio(prenotazione.getOrarioInizio());
        richiesta.setFine(prenotazione.getOrarioFine());

        Prenotazione prenotazioneDaSalvare = this.creaOggettoPrenotazione(prenotazione);
        Prenotazione prenotazioneSalvata = salvaPrenotazione(prenotazioneDaSalvare);

        if (richiesta instanceof RichiestaRicarica) {
            richiesta.setIdPrenotazione(prenotazioneSalvata.getIdPrenotazione());
            ricaricaService.richiestaRicarica((RichiestaRicarica) richiesta);
        } else if (richiesta instanceof RichiestaSosta) {
            /*FIXME: bisogna inserire l'ID della prenotazione nella richiesta di sosta*/
            richiesta.setIdPrenotazione(prenotazioneSalvata.getIdPrenotazione());
            sostaService.richiestaSosta((RichiestaSosta) richiesta);
        } else {
            throw new IllegalArgumentException("Tipo di richiesta non supportato");
        }

    }

    private Prenotazione creaOggettoPrenotazione(Prenotazione prenotazione) {
        Prenotazione nuovaPrenotazione = new Prenotazione();
        nuovaPrenotazione.setIdUtente(prenotazione.getIdUtente());
        nuovaPrenotazione.setOrarioInizio(prenotazione.getOrarioInizio());
        nuovaPrenotazione.setOrarioFine(prenotazione.getOrarioFine());

        return nuovaPrenotazione;
    }

    private Prenotazione salvaPrenotazione(Prenotazione prenotazione) {
        try {
            return prenotazioneRepositoryService.createOrUpdatePrenotazione(prenotazione);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
