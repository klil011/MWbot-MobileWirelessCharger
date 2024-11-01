package it.mounir.MWbot.services;

import it.mounir.MWbot.domain.RichiestaVeicolo;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.mqtt.MqttPublisher;
import it.mounir.MWbot.repositories.RicaricaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void richiestaRicarica(String veicoloId, Boolean riceviMessaggio) {
        /*stazione di ricarica*/
        String stazioneLibera  = parcheggioService.getPrimoPostoLibero();

        if (stazioneLibera != null) {

            parcheggioService.occupaPosto(stazioneLibera, riceviMessaggio);
            System.out.println("Veicolo " + veicoloId + " ha occupato la stazione di ricarica " + stazioneLibera + ".");
        } else {
            codaRicaricaService.aggiungiInCoda(veicoloId, riceviMessaggio, TipoServizio.RICARICA);
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
