package it.mounir.MWbot.services;

import it.mounir.MWbot.mqtt.MqttSubscriber;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class ParcheggioService {

    @Lazy
    @Autowired
    private MqttSubscriber mqttSubscriber;

    private final Set<String> postiLiberi;
    private final Map<String, Long> tempoPostiOccupati;
    private final int maxPosti = 5;

    @Autowired
    public ParcheggioService() {

        this.postiLiberi = new HashSet<>();
        this.tempoPostiOccupati = new HashMap<>();

        for (int i = 1; i <= maxPosti; i++) {
            postiLiberi.add(String.valueOf(i));
        }
    }

    public boolean occupaPosto(String postoId, Boolean riceviMessaggio) {
        if (postiLiberi.remove(postoId)) {

            /*  Se la procedura viene invocata a seguito di una
            richiesta di sosta non viene inoltrata nessun notifica  */
            if(riceviMessaggio == null)
                riceviMessaggio = false;

            if (riceviMessaggio) {
                try {
                    String topic = "Parcheggio/Messaggio/Posto/" + postoId;
                    mqttSubscriber.subscribeToTopic(topic);  // Iscrizione dinamica al topic
                } catch (MqttException e) {
                    e.printStackTrace();
                    System.out.println("Errore durante l'iscrizione al topic per posto " + postoId);
                }
            }

            tempoPostiOccupati.put(postoId, System.currentTimeMillis());
            System.out.println("Posto " + postoId + " è ora occupato.");
            return true;
        }
        System.out.println("Posto " + postoId + " non è disponibile.");
        return false;
    }

    public void liberaPosto(String postoId) {

        /*
        *   Qua richiamo la funzione gestisci messaggio e controllo se postoId
        *   è un posto di cui bisogna inviare un messaggio di fine ricarica
        * */
        if (Integer.valueOf(postoId) >= 1 && Integer.valueOf(postoId) <= maxPosti) {
            postiLiberi.add(postoId);

            // Calcola il tempo di sosta
            Long timestamp = tempoPostiOccupati.remove(postoId);
            if (timestamp != null) {
                long tempoTrascorso = System.currentTimeMillis() - timestamp;
                System.out.println("Il veicolo ha sostato per " + tempoTrascorso / 1000 + " secondi.");
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