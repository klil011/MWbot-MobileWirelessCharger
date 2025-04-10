package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.domain.StatoRicarica;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Ricarica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RicaricaService {

    private final RicaricaRepositoryService ricaricaRepositoryService;
    private final ParcheggioService parcheggioService;
    private final CodaService codaRicaricaService;

    @Autowired
    public RicaricaService(ParcheggioService parcheggioService,
                           CodaService codaRicaricaService, RicaricaRepositoryService ricaricaRepositoryService) {
        this.parcheggioService = parcheggioService;
        this.codaRicaricaService = codaRicaricaService;
        this.ricaricaRepositoryService = ricaricaRepositoryService;
    }

    public Ricarica richiestaRicarica(RichiestaRicarica richiestaRicarica) {

        String stazioneLibera  = parcheggioService.getPrimoPostoLibero();
        richiestaRicarica.setTipoServizio(TipoServizio.RICARICA);

        Ricarica ricarica = this.creaOggettoRicarica(richiestaRicarica);
        Ricarica ricaricaSalvata;

        /* se ci sono parcheggi liberi viene riservato il primo posto libero per effettuare il servizio di ricarica */
        if (stazioneLibera != null) {

            ricarica.setStato(StatoRicarica.CHARGING.ordinal());
            ricaricaSalvata = this.salvaRichiestaRicarica(ricarica);
            richiestaRicarica.setIdRichiesta(ricaricaSalvata.getIdRicarica());

            parcheggioService.occupaPosto(stazioneLibera, richiestaRicarica);

            System.out.println("[CHARGING] Veicolo " + richiestaRicarica.getVeicoloId() + " ha occupato la stazione di ricarica " + stazioneLibera + ". \n");

            /* se i parcheggi sono tutti occupati viene messa la richista del servizio di ricarica in coda FIFO */
        } else {

            ricarica.setStato(StatoRicarica.WAITING.ordinal()); /* perchè viene messo in coda di attesa */
            ricaricaSalvata = this.salvaRichiestaRicarica(ricarica);
            richiestaRicarica.setIdRichiesta(ricaricaSalvata.getIdRicarica());

            System.out.println("[WAITING] Veicolo " + richiestaRicarica.getVeicoloId() + " e' in coda di attesa. \n");

            codaRicaricaService.aggiungiInCoda(richiestaRicarica);
        }

        return ricaricaSalvata;
    }

    private Ricarica creaOggettoRicarica(RichiestaRicarica richiestaRicarica) {
        Ricarica ricarica = new Ricarica();
        ricarica.setIdMwbot(1);  /* per semplicità si suppone che ci sia un solo MWbot */

        if (richiestaRicarica.getIdPrenotazione() != null) {
            ricarica.setIdPrenotazione(richiestaRicarica.getIdPrenotazione());
        }
        else {
            ricarica.setIdPrenotazione(null);
        }
        ricarica.setIdVeicolo(richiestaRicarica.getVeicoloId());
        ricarica.setIdUtente(richiestaRicarica.getIdUtente());
        ricarica.setNotifica(richiestaRicarica.getRiceviMessaggio());
        ricarica.setPercentualeIniziale(richiestaRicarica.getPercentualeIniziale());
        ricarica.setPercentualeRicaricare(richiestaRicarica.getPercentualeDesiderata());

        return ricarica;
    }

    private Ricarica salvaRichiestaRicarica(Ricarica ricarica) {
        try{
            Ricarica ricaricaSalvata = ricaricaRepositoryService.createOrUpdateRicarica(ricarica);
            return ricaricaSalvata;

        }catch(RuntimeException e){
            System.err.println("[ERROR] Errore durante l'interazione con il DB: " + e.getMessage());
            throw new RuntimeException("Non è stato possibile completare la richiesta di ricarica", e);
        }
    }

    public String statoStazioniRicarica() {
        return "Stazioni di ricarica libere: " + parcheggioService.getPostiLiberiCount() +
                ", Veicoli in coda per ricarica: " + codaRicaricaService.getCodaRichiesteCount();
    }

}
