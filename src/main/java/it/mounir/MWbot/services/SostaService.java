package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.RichiestaSosta;
import it.mounir.MWbot.domain.StatoSosta;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Sosta;
import it.mounir.MWbot.repositories.SostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SostaService {

    private final ParcheggioService parcheggioService;
    private final CodaService codaSostaService;
    private final SostaRepositoryService sostaRepositoryService;

    @Autowired
    public SostaService(ParcheggioService parcheggioService, CodaService codaSostaService, SostaRepositoryService sostaRepositoryService) {
        this.parcheggioService = parcheggioService;
        this.codaSostaService = codaSostaService;
        this.sostaRepositoryService = sostaRepositoryService;
    }

    public void richiestaSosta(RichiestaSosta richiestaSosta) {
        String postoLibero = parcheggioService.getPrimoPostoLibero();
        richiestaSosta.setTipoServizio(TipoServizio.SOSTA);

        Sosta sosta = creaOggettoSosta(richiestaSosta);

        if (postoLibero != null) {
            sosta.setStato(StatoSosta.PARKING.ordinal());
            Sosta sostaSalvata = this.salvaSosta(sosta);
            richiestaSosta.setIdRichiesta(sostaSalvata.getIdSosta());

            parcheggioService.occupaPosto(postoLibero, richiestaSosta);

            System.out.println("Veicolo " + richiestaSosta.getVeicoloId() + " ha occupato il posto " + postoLibero + ".");

        } else {
            sosta.setStato(StatoSosta.WAITING.ordinal());
            Sosta sostaSalvata = this.salvaSosta(sosta);
            richiestaSosta.setIdRichiesta(sostaSalvata.getIdSosta());

            codaSostaService.aggiungiInCoda(richiestaSosta);
        }
    }

    private Sosta creaOggettoSosta(RichiestaSosta richiestaSosta) {
        Sosta sosta = new Sosta();
        sosta.setIdUtente(richiestaSosta.getIdUtente());
        sosta.setIdVeicolo(richiestaSosta.getVeicoloId());
        sosta.setTempoSosta(richiestaSosta.getTempoSosta());

        if (richiestaSosta.getIdPrenotazione() != null) {
            sosta.setIdPrenotazione(richiestaSosta.getIdPrenotazione());
        }
        else {
            sosta.setIdPrenotazione(richiestaSosta.getIdPrenotazione());
        }

        return sosta;
    }

    private Sosta salvaSosta(Sosta sosta) {
        try {
            Sosta sostaSalvata = sostaRepositoryService.createOrUpdateSosta(sosta);
            return sostaSalvata;

        } catch(RuntimeException e) {
            System.err.println("Errore durante l'interazione con il DB: " + e.getMessage());
            throw new RuntimeException("Non è stato possibile completare la richiesta di sosta", e);
        }
    }

    public String statoParcheggio() {
        return "Posti liberi: " + parcheggioService.getPostiLiberiCount() +
                ", Veicoli in coda: " + codaSostaService.getCodaRichiesteCount();
    }

}



