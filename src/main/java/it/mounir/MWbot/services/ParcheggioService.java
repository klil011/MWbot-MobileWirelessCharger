package it.mounir.MWbot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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


import java.util.*;

@Service
public class ParcheggioService {

    private final Set<String> postiLiberi;
    private final Map<String, OccupazionePosto> tempoPostiOccupati;
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

        for (int i = 1; i <= maxPosti; i++) {
            postiLiberi.add(String.valueOf(i));
        }
    }

    public boolean occupaPosto(String postoId, Richiesta richiesta) {
        if (postiLiberi.remove(postoId)) {
            tempoPostiOccupati.put(postoId, new OccupazionePosto(richiesta.getIdRichiesta(), richiesta.getIdUtente(),
                    richiesta.getTipoServizio(), System.currentTimeMillis(), richiesta.getInizio(), richiesta.getFine()));

            System.out.println("Posto " + postoId + " è ora occupato.");

            /*considera che il metodo viene già usato quando il veicolo non va in coda e per le richieste di tipo sosta */
            if(richiesta.getTipoServizio().equals(TipoServizio.RICARICA)){  /*potrei usare anche TipoServizio*/
                Optional<Ricarica> ricaricaSalvata = ricaricaRepositoryService.getRicaricaById((long)richiesta.getIdRichiesta());
                if (ricaricaSalvata.isPresent()) {
                    Ricarica ricarica = ricaricaSalvata.get();

                    if(ricarica.getStato() == 0) {
                        ricarica.setStato(StatoRicarica.CHARGING.ordinal());
                        ricaricaRepositoryService.createOrUpdateRicarica(ricarica);
                    }

                } else {
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
                    System.out.println("Sosta non trovata.");
                }
            }

            String topic = "Mwbot/Posto/" + postoId;
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String jsonPayload = objectMapper.writeValueAsString(richiesta);
                mqttPublisher.publish(topic, jsonPayload);
                System.out.println("Payload inviato: " + jsonPayload);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*  [INSERIRE] MQTT_pub al MWbot in cui verrà passato
               Topic: Mwbot/Posto/

            *       -id_richiesta
            *       -n.posto
            *       -tipo_servizio*/



            return true;
        }

        System.out.println("Posto " + postoId + " non è disponibile.");
        return false;
    }

    public void liberaPosto(String postoId) {

        if (Integer.valueOf(postoId) >= 1 && Integer.valueOf(postoId) <= maxPosti) {
            postiLiberi.add(postoId);

            // Calcola il tempo di sosta
            OccupazionePosto occupazionePosto = tempoPostiOccupati.remove(postoId);
            Long timestamp = occupazionePosto.getTempo();

            if (timestamp != null) {
                long tempoTrascorso = (System.currentTimeMillis() - timestamp) / 1000;
                System.out.println("Il veicolo ha sostato per " + " secondi.");

                /*  Da qui richiamo il servizio che gestice il calcolo dell'importo
                da pagare [PagamentoService.calcolaImporto(tempo, servizio, idUtente)]
                che sarà riferito solo alla sosta, mentre per il costo di ricarica va gestito nel servizio MwbotService */

                pagamentoService.calcolaImporto((int) tempoTrascorso, TipoServizio.SOSTA, occupazionePosto.getIdUtente());

                if(occupazionePosto.getTipoServizio().equals(TipoServizio.SOSTA)) {
                    /*  Richiamo SostaRepositoryService e aggiorno lo stato a PARKED    */
                    sostaRepositoryService.updateTempoById((long)occupazionePosto.getIdRichiesta(), tempoTrascorso);
                    sostaRepositoryService.updateStatoById((long)occupazionePosto.getIdRichiesta(), StatoSosta.PARKED.ordinal());
                }
                else {
                    /*  Quindi se è ricarica salverò il tempo di sosta  */
                    ricaricaRepositoryService.updateTempoById((long)occupazionePosto.getIdRichiesta(), tempoTrascorso);
                }
            } else {
                System.out.println("Posto " + postoId + " non era occupato.");
            }

            System.out.println("Posto " + postoId + " è ora libero.");
        } else {
            System.out.println("Operazione non possibile, errore indice parcheggio");
        }
    }

    public String getPrimoPostoLibero() {
        return postiLiberi.stream().findFirst().orElse(null);
    }

    public int getPostiLiberiCount() {
        return postiLiberi.size();
    }
}