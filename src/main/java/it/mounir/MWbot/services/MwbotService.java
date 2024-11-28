package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.domain.StatoRicarica;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Mwbot;
import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.mqtt.MqttPublisher;
import it.mounir.MWbot.repositories.MwbotRepository;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
public class MwbotService {

    private Thread ricaricaThread; // Unico thread per la ricarica
    private boolean isRicaricaInCorso = false; // Stato della ricarica

    private final MwbotRepository mwbotRepository;
    private final MqttPublisher mqttPublisher;
    private final RicaricaRepositoryService ricaricaRepositoryService;

    public MwbotService(MwbotRepository mwbotRepository, MqttPublisher mqttPublisher, RicaricaRepositoryService ricaricaRepositoryService) {
        this.mwbotRepository = mwbotRepository;
        this.mqttPublisher = mqttPublisher;
        this.ricaricaRepositoryService = ricaricaRepositoryService;
    }

    public void avviaRicarica(String postoId, RichiestaRicarica ricarica) {

        if (isRicaricaInCorso) {
            System.out.println("Ricarica già in corso. Non è possibile avviare una nuova ricarica.");
            return;
        }


        isRicaricaInCorso = true; // Imposta lo stato
        ricaricaThread = new Thread(() -> {
            try {
                int percentuale = ricarica.getPercentualeDesiderata() - ricarica.getPercentualeIniziale();
                System.out.println("Inizio ricarica per il veicolo: " + ricarica.getVeicoloId());
                int tempoDiRicarica = calcolaTempoRicarica(percentuale);
                System.out.println("Tempo stimato di ricarica: " + tempoDiRicarica + " ms");

                Thread.sleep(tempoDiRicarica); // Simula la ricarica

                System.out.println("Ricarica completata per il veicolo: " + ricarica.getVeicoloId());
                ricaricaRepositoryService.updateColumnById((long)ricarica.getIdRichiesta(), StatoRicarica.COMPLETED.ordinal());

                if (ricarica.getRiceviMessaggio().equals(true)) {
                    /*  TODO inviare notifica se riceviMessaggio == true   */
                    String topic = "Parcheggio/Messaggio/Posto/" + postoId;
                    mqttPublisher.publish(topic, "Notifica: ricarica del veicolo con targa "
                            + ricarica.getVeicoloId() + " terminata");
                }
            } catch (InterruptedException | MqttException e) {
                System.out.println("Ricarica interrotta per il veicolo: " + ricarica.getVeicoloId());
                //aggiornaStatoDB(veicoloId, "interrotto");
            } finally {
                // Rilascia lo stato
                synchronized (this) {
                    isRicaricaInCorso = false;
                }
            }
        });

        ricaricaThread.start(); // Avvia il thread

    }

    private int calcolaTempoRicarica(int percentuale) {
        /* Si suppone che servano 100 ms per ogni 1% di ricarica  */
        return percentuale * 100;
    }

    public Mwbot createOrUpdateMwbot(Mwbot mwbot) {
        return mwbotRepository.save(mwbot);
    }

    public Iterable<Mwbot> getAllMwbot() {
        return mwbotRepository.findAll();
    }

    public void deleteMwBot(Mwbot mwbot) {
        mwbotRepository.delete(mwbot);
    }
}
