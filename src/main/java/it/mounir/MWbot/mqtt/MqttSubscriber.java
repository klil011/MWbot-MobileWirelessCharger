package it.mounir.MWbot.mqtt;

import it.mounir.MWbot.domain.RichiestaVeicolo;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.services.CodaService;
import it.mounir.MWbot.services.ParcheggioService;
import it.mounir.MWbot.services.RicaricaService;
import it.mounir.MWbot.services.SostaService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttSubscriber implements MqttCallback {

    private final MqttClient mqttClient;
    private final ParcheggioService parcheggioService;
    private final CodaService codaService;

    @Autowired
    public MqttSubscriber(MqttClient mqttClient, ParcheggioService parcheggioService, CodaService codaService) {
        this.mqttClient = mqttClient;
        this.parcheggioService = parcheggioService;
        this.codaService = codaService;
    }

    @PostConstruct
    public void subscribe() throws MqttException {
        mqttClient.setCallback(this);
        mqttClient.subscribe("Parcheggio/Posto/#");
        mqttClient.subscribe("Mwbot/Posto/#");
    }

    /*  metodo per iscriversi dinamicamente al topic */
    public void subscribeToTopic(String topic) throws MqttException {
        mqttClient.subscribe(topic);
        System.out.println("Iscritto dinamicamente al topic: " + topic);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
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
    }

    private void gestisciNotifica(String postoId, String contenuto) {

        System.out.println("Messaggio: Ricarica del veicolo al posto: " + postoId + " completata");
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
