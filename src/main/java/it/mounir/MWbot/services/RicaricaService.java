package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.domain.StatoRicarica;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.mqtt.MqttPublisher;
import it.mounir.MWbot.repositories.RicaricaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.sql.Types.NULL;

@Service
public class RicaricaService {

    private final RicaricaRepository ricaricaRepository;
    private final ParcheggioService parcheggioService;
    private final CodaService codaRicaricaService;

    @Autowired
    public RicaricaService(RicaricaRepository ricaricaRepository, ParcheggioService parcheggioService,
                           CodaService codaRicaricaService, MqttPublisher mqttPublisher) {
        this.ricaricaRepository = ricaricaRepository;
        this.parcheggioService = parcheggioService;
        this.codaRicaricaService = codaRicaricaService;
    }

    public void richiestaRicarica(RichiestaRicarica richiestaRicarica) {
        /*stazione di ricarica*/
        String stazioneLibera  = parcheggioService.getPrimoPostoLibero();
        richiestaRicarica.setTipoServizio(TipoServizio.RICARICA);

        if (stazioneLibera != null) {

            parcheggioService.occupaPosto(stazioneLibera, richiestaRicarica.getRiceviMessaggio());

            try{
                Ricarica ricarica = new Ricarica();
                ricarica.setIdMwbot(1);  /* per semplicità si suppone che ci sia un solo MWbot */
                ricarica.setIdPrenotazione(null); /* per il momento non ho ancora gestito le prenotazioni */
                ricarica.setStato(StatoRicarica.CHARGING.ordinal()); /* bisogna creare un ENUM che rappresenti gli stati possibili delle ricariche */
                ricarica.setIdVeicolo(richiestaRicarica.getVeicoloId());
                ricarica.setIdUtente(richiestaRicarica.getIdUtente());
                ricarica.setPercentualeIniziale(richiestaRicarica.getPercentualeIniziale());
                ricarica.setPercentualeRicaricare(richiestaRicarica.getPercentualeDesiderata());

                Ricarica ricaricaSalvata = this.createOrUpdateRicarica(ricarica);

            }catch(RuntimeException e){
                System.err.println("Errore durante l'interazione con il DB: " + e.getMessage());
                throw new RuntimeException("Non è stato possibile completare la richiesta di ricarica", e);
            }
            System.out.println("Veicolo " + richiestaRicarica.getVeicoloId() + " ha occupato la stazione di ricarica " + stazioneLibera + ".");
        } else {
            richiestaRicarica.setTipoServizio(TipoServizio.RICARICA);
            codaRicaricaService.aggiungiInCoda(richiestaRicarica);
        }
    }

    public String statoStazioniRicarica() {
        return "Stazioni di ricarica libere: " + parcheggioService.getPostiLiberiCount() +
                ", Veicoli in coda per ricarica: " + codaRicaricaService.getCodaRichiesteCount();
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
