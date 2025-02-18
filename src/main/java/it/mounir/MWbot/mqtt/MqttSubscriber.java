package it.mounir.MWbot.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mounir.MWbot.DTO.Richiesta;
import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.services.CodaService;
import it.mounir.MWbot.services.MwbotService;
import it.mounir.MWbot.services.ParcheggioService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttSubscriber implements MqttCallback {

    private final MqttClient mqttClient;
    private final ParcheggioService parcheggioService;
    private final CodaService codaService;
    private final MwbotService mwbotService;

    @Autowired
    public MqttSubscriber(MqttClient mqttClient, ParcheggioService parcheggioService, CodaService codaService, MwbotService mwbotService) {
        this.mqttClient = mqttClient;
        this.parcheggioService = parcheggioService;
        this.codaService = codaService;
        this.mwbotService = mwbotService;
    }

    @PostConstruct
    public void subscribe() throws MqttException {
        mqttClient.setCallback(this);
        mqttClient.subscribe("Parcheggio/Posto/#");
        mqttClient.subscribe("Parcheggio/Messaggio/Posto/#");
        mqttClient.subscribe("Mwbot/Posto/#");
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String contenuto = new String(message.getPayload());
        String postoId = topic.substring(topic.lastIndexOf('/') + 1);

        if(topic.equals("Parcheggio/Posto/" + postoId)) {
            gestisciStatoPosto(postoId, contenuto);
        }

        if(topic.equals("Parcheggio/Messaggio/Posto/" + postoId)){
            gestisciNotifica(postoId, contenuto);
        }

        if(topic.equals("Mwbot/Posto/" + postoId)) {
            gestisciRichiesta(postoId, contenuto);
        }
    }

    /*[RAGIONAMENTO]
    *
    * da capire se è necessario creare un metodo per le ricariche e un'altro per le soste,
    * perchè ci potrebbe essere un thread per ogni veicolo all'interno del parcheggio oppure fare un thread
    * solo per la singola ricarica.
    *
    * */

    private void gestisciRichiesta(String postoId, String contenuto) {


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Aggiunge il supporto per LocalDateTime

        Richiesta richiesta = null;
        try {
            richiesta = objectMapper.readValue(contenuto, Richiesta.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /*  Qua bisognerebbe far partire un thread che si occupa della ricarica del veicolo
        *   e dell'aggiornamento dello stato all'interno del DB*/
        if (richiesta.isRicarica(richiesta)){
            mwbotService.avviaRicarica(postoId, (RichiestaRicarica) richiesta);
        }


        /*1. Mwbot calcola in base alla parcentuale da ricaricare il tempo di attesa per ricaricare,
        * oltre al quale la ricarica sarà terminata e aggiornerà lo stato nel DB.*/

        /*2. Quando servizio completo if messaggio true -> inviaNotifica()*/

        /*3. e per la sosta come gestiamo la situa ?*/
    }

    private void gestisciNotifica(String postoId, String contenuto) {

        System.out.println(contenuto);
    }

    private void gestisciStatoPosto(String postoId, String contenuto) {
        if(contenuto.equalsIgnoreCase("LIBERO")) {
            parcheggioService.liberaPosto(postoId);
            codaService.gestisciCoda(postoId);

        }

        /*  [MODIFICA]
        Possiamo eliminarlo da qui e creare un controller che si occupa di fornire l'informazione   */
        System.out.println("Posti liberi: " + parcheggioService.getPostiLiberiCount());
    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
