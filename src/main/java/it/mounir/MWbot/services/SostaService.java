package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.RichiestaSosta;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Sosta;
import it.mounir.MWbot.repositories.SostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SostaService {

    private final ParcheggioService parcheggioService;
    private final CodaService codaSostaService;
    private final SostaRepository sostaRepository;

    @Autowired
    public SostaService(ParcheggioService parcheggioService, CodaService codaSostaService, SostaRepository sostaRepository) {
        this.parcheggioService = parcheggioService;
        this.codaSostaService = codaSostaService;
        this.sostaRepository = sostaRepository;
    }

    public void richiestaSosta(RichiestaSosta richiestaSosta) {
        String postoLibero = parcheggioService.getPrimoPostoLibero();
        if (postoLibero != null) {
            parcheggioService.occupaPosto(postoLibero, false);

            try {
                Sosta sosta = new Sosta();
                sosta.setIdUtente(richiestaSosta.getIdUtente());
                sosta.setIdVeicolo(richiestaSosta.getVeicoloId());
                sosta.setTempoSosta(richiestaSosta.getTempoSosta());

                Sosta sostaSalvata = this.createOrUpdateSosta(sosta);

            } catch(RuntimeException e) {
                System.err.println("Errore durante l'interazione con il DB: " + e.getMessage());
                throw new RuntimeException("Non è stato possibile completare la richiesta di sosta", e);
            }

            System.out.println("Veicolo " + richiestaSosta.getVeicoloId() + " ha occupato il posto " + postoLibero + ".");

        } else {
            codaSostaService.aggiungiInCoda(richiestaSosta.getIdUtente(), richiestaSosta.getVeicoloId(), false, TipoServizio.SOSTA);
        }
    }

    public String statoParcheggio() {
        return "Posti liberi: " + parcheggioService.getPostiLiberiCount() +
                ", Veicoli in coda: " + codaSostaService.getCodaRichiesteCount();
    }

    public Sosta createOrUpdateSosta (Sosta servizioSosta) {
        return sostaRepository.save(servizioSosta);
    }
}



