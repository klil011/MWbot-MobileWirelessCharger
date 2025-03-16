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
        /*stazione di ricarica*/

        String stazioneLibera  = parcheggioService.getPrimoPostoLibero();
        richiestaRicarica.setTipoServizio(TipoServizio.RICARICA);

        Ricarica ricarica = this.creaOggettoRicarica(richiestaRicarica);
        Ricarica ricaricaSalvata;

        if (stazioneLibera != null) {

            ricarica.setStato(StatoRicarica.CHARGING.ordinal());
            ricaricaSalvata = this.salvaRichiestaRicarica(ricarica);
            richiestaRicarica.setIdRichiesta(ricaricaSalvata.getIdRicarica());

            parcheggioService.occupaPosto(stazioneLibera, richiestaRicarica);

            System.out.println("Veicolo " + richiestaRicarica.getVeicoloId() + " ha occupato la stazione di ricarica " + stazioneLibera + ".");
        } else {

            ricarica.setStato(StatoRicarica.WAITING.ordinal()); /* perchè viene messo in coda di attesa */
            ricaricaSalvata = this.salvaRichiestaRicarica(ricarica);
            richiestaRicarica.setIdRichiesta(ricaricaSalvata.getIdRicarica());

            codaRicaricaService.aggiungiInCoda(richiestaRicarica);
        }

        return ricaricaSalvata; /*TODO: creare l'URI con l'id della nuova ricarica*/
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
            System.err.println("Errore durante l'interazione con il DB: " + e.getMessage());
            throw new RuntimeException("Non è stato possibile completare la richiesta di ricarica", e);
        }
    }

    public String statoStazioniRicarica() {
        return "Stazioni di ricarica libere: " + parcheggioService.getPostiLiberiCount() +
                ", Veicoli in coda per ricarica: " + codaRicaricaService.getCodaRichiesteCount();
    }

}
