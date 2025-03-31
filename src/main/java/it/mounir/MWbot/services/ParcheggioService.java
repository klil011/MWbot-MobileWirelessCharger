package it.mounir.MWbot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mounir.MWbot.DTO.Richiesta;
import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.domain.OccupazionePosto;
import it.mounir.MWbot.domain.StatoRicarica;
import it.mounir.MWbot.domain.StatoSosta;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Pagamento;
import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.model.Sosta;
import it.mounir.MWbot.mqtt.MqttPublisher;
import it.mounir.MWbot.repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParcheggioService {

    private final Set<String> postiLiberi;

    /*  Hash map di liste, ogni lista rappresenta richieste di ricarica/sosta e prenotazioni,
    "tempoPostiOccupati" perchè al suo interno vengono salvati i timestamp del tempo di occupazione del singolo posto */
    private final Map<String, List<OccupazionePosto>> tempoPostiOccupati;
    private final int maxPosti = 5;

    private final RicaricaRepositoryService ricaricaRepositoryService;
    private final SostaRepositoryService sostaRepositoryService;
    private final PagamentoService pagamentoService;
    private final MqttPublisher mqttPublisher;

    @Autowired
    public ParcheggioService(RicaricaRepositoryService ricaricaRepositoryService, SostaRepositoryService sostaRepositoryService, PagamentoService pagamentoService, MqttPublisher mqttPublisher) {
        this.ricaricaRepositoryService = ricaricaRepositoryService;
        this.sostaRepositoryService = sostaRepositoryService;
        this.pagamentoService = pagamentoService;
        this.mqttPublisher = mqttPublisher;

        this.postiLiberi = new HashSet<>();
        this.tempoPostiOccupati = new HashMap<>();

        /*  Inizializzazione delle strutture dati  */
        for (int i = 1; i <= maxPosti; i++) {
            postiLiberi.add(String.valueOf(i));
            tempoPostiOccupati.put(String.valueOf(i), new ArrayList<>());
        }
    }


    public boolean occupaPosto(String postoId, Richiesta richiesta) {
        if (postiLiberi.remove(postoId)) {

            tempoPostiOccupati.get(postoId).add(creaOggettoOccupazionePosto(richiesta));

            System.out.println("Posto " + postoId + " è ora occupato.");

            aggiornaStato(richiesta);

            informaMwbot(richiesta, postoId);

            return true;
        }

        System.out.println("Posto " + postoId + " non è disponibile.");
        return false;
    }


    /* MQTT publisher per informare il bot del servizo che dovrà compiere */
    private void informaMwbot(Richiesta richiesta, String postoId) {
        String topic = "Mwbot/Posto/" + postoId;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Registriamo il modulo per Java 8 Date/Time
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Evitiamo la serializzazione come timestamp


        try {
            String jsonPayload = objectMapper.writeValueAsString(richiesta);
            mqttPublisher.publish(topic, jsonPayload);
            System.out.println("Payload inviato: " + jsonPayload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OccupazionePosto creaOggettoOccupazionePosto(Richiesta richiesta) {
        return new OccupazionePosto(richiesta.getIdRichiesta(), richiesta.getIdUtente(),
                richiesta.getTipoServizio(), System.currentTimeMillis(), richiesta.getInizio(), richiesta.getFine());
    }

    /* Aggiorna lo stato del servizio all'interna del DB, quando un veicolo passa dalla coda di attesa all'erogazione
    * del servizo di ricarica lo stato passa da WAITING ---> CHARGING
    *
    * Stessa cosa per il servizio di sosta, quando passa dalla coda di attesa all'erogazione del servizio
    * WAITING ---> PARKING*/
    private void aggiornaStato(Richiesta richiesta) {
        /*  Aggiornamento dello stato della ricarica da WAITING a CHARGING  */
        if(richiesta.getTipoServizio().equals(TipoServizio.RICARICA)){
            Optional<Ricarica> ricaricaSalvata = ricaricaRepositoryService.getRicaricaById((long)richiesta.getIdRichiesta());
            if (ricaricaSalvata.isPresent()) {
                Ricarica ricarica = ricaricaSalvata.get();

                if(ricarica.getStato() == 0) {
                    ricarica.setStato(StatoRicarica.CHARGING.ordinal());
                    ricaricaRepositoryService.createOrUpdateRicarica(ricarica);
                }

            } else {
                /*  FIXME: dovrebbe lanciare un eccezzione perchè la ricarica non viene trovata  */
                System.out.println("Ricarica non trovata.");
            }
        }
        else {
            Optional<Sosta> sostaSalvata = sostaRepositoryService.getSostaById((long)richiesta.getIdRichiesta());
            if(sostaSalvata.isPresent()) {
                Sosta sosta = sostaSalvata.get();

                if(sosta.getStato() == 0) {
                    sosta.setStato(StatoSosta.PARKING.ordinal());
                    sostaRepositoryService.createOrUpdateSosta(sosta);
                }
            } else {
                /*  FIXME: dovrebbe lanciare un eccezzione perchè la sosta non viene trovata  */
                System.out.println("Sosta non trovata.");
            }
        }
    }

    public void liberaPosto(String postoId) {

        if (Integer.valueOf(postoId) >= 1 && Integer.valueOf(postoId) <= maxPosti) {
            postiLiberi.add(postoId);

            // Calcola il tempo di sosta
            OccupazionePosto occupazionePosto = tempoPostiOccupati.get(postoId).removeFirst();
            Long timestamp = occupazionePosto.getTempo();

            if (timestamp != null) {
                long tempoTrascorso = (System.currentTimeMillis() - timestamp) / 1000;
                System.out.println("Il veicolo ha sostato per " + tempoTrascorso + " secondi.");

                /*  Da qui richiamo il servizio che gestice il calcolo dell'importo
                da pagare [PagamentoService.calcolaImporto(tempo, servizio, idUtente)]
                che sarà riferito solo alla sosta, mentre per il costo di ricarica va gestito nel servizio MwbotService

                Al costo di ricarica va aggiunto il costo della sosta all'interno del parcheggio  */

                pagamentoService.calcolaImporto((int) tempoTrascorso, TipoServizio.SOSTA, occupazionePosto.getIdUtente());

                if(occupazionePosto.getTipoServizio().equals(TipoServizio.SOSTA)) {
                    /*  Richiamo SostaRepositoryService e aggiorno lo stato a PARKED    */
                    sostaRepositoryService.updateTempoById((long)occupazionePosto.getIdRichiesta(), tempoTrascorso);
                    sostaRepositoryService.updateStatoById((long)occupazionePosto.getIdRichiesta(), StatoSosta.PARKED.ordinal());
                }
                else {
                    /*  Quindi se è ricarica salverò il tempo di sosta  */
                    ricaricaRepositoryService.updateTempoById((long)occupazionePosto.getIdRichiesta(), tempoTrascorso);
                /* perché il servizio di ricarica è composto anche dalla sosta */
                }
            } else {
                System.out.println("Posto " + postoId + " non era occupato.");
            }

            System.out.println("Posto " + postoId + " è ora libero.");
        } else {
            System.out.println("Operazione non possibile, errore indice parcheggio");
        }
    }

    public boolean disponibilitaPrenotazione(LocalDateTime inizio, LocalDateTime fine) {
        if (inizio == null || fine == null) {
            return false;
        }

        for(List<OccupazionePosto> occupazioni : tempoPostiOccupati.values()) {
            for (int i = 0; i < occupazioni.size(); i++) {
                OccupazionePosto occupazione = occupazioni.get(i);

                // Controllo solo i parcheggi con prenotazione
                if (occupazione.isPrenotazione()) {

                    // Se il nuovo intervallo NON si sovrappone all'attuale prenotazione
                    if (fine.isBefore(occupazione.getInizio()) || inizio.isAfter(occupazione.getFine())) {

                        // Se siamo all'ultima prenotazione, allora lo slot è libero
                        if (i == occupazioni.size() - 1) {
                            return true;
                        }

                        // Se c'è una prenotazione successiva, verifico che il periodo termini prima di essa
                        OccupazionePosto next = occupazioni.get(i + 1);
                        if (fine.isBefore(next.getInizio())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    public String getPrimoPostoLibero() {
        return postiLiberi.stream().findFirst().orElse(null);
    }

    public int getPostiLiberiCount() {
        return postiLiberi.size();
    }
}