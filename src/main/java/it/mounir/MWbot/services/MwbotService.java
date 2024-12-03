package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.domain.StatoRicarica;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Mwbot;
import it.mounir.MWbot.mqtt.MqttPublisher;
import it.mounir.MWbot.repositories.MwbotRepository;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class MwbotService {

    private Thread ricaricaThread; // Unico thread per la ricarica
    private boolean isRicaricaInCorso = false; // Stato della ricarica

    private final MwbotRepository mwbotRepository;
    private final MqttPublisher mqttPublisher;
    private final RicaricaRepositoryService ricaricaRepositoryService;
    private final Queue<RichiestaRicarica> codaRicarica = new LinkedList<>();
    private final PagamentoService pagamentoService;

    @Autowired
    public MwbotService(MwbotRepository mwbotRepository, MqttPublisher mqttPublisher, RicaricaRepositoryService ricaricaRepositoryService, PagamentoService pagamentoService) {
        this.mwbotRepository = mwbotRepository;
        this.mqttPublisher = mqttPublisher;
        this.ricaricaRepositoryService = ricaricaRepositoryService;
        this.pagamentoService = pagamentoService;
    }

    private void aggiungiRicaricaCoda(RichiestaRicarica richiestaRicarica) {
        codaRicarica.add(richiestaRicarica);
        System.out.println("Veicolo " + richiestaRicarica.getVeicoloId() + " è stato messo nella coda di ricarica.");
    }

    private RichiestaRicarica rimuoviRicaricaCoda() {
        /* Restituisce null se la coda è vuota  */
        return codaRicarica.poll();
    }

    public void avviaRicarica(String postoId, RichiestaRicarica ricarica) {
        aggiungiRicaricaCoda(ricarica);

        if (isRicaricaInCorso) {
            System.out.println("Ricarica già in corso. Veicolo aggiunto alla coda.");
            return;
        }

        isRicaricaInCorso = true;

        ricaricaThread = new Thread(() -> {
            try {
                while (!codaRicarica.isEmpty()) {
                    RichiestaRicarica richiestaCorrente = rimuoviRicaricaCoda();
                    if (richiestaCorrente != null) {
                        System.out.println("Inizio ricarica per il veicolo: " + richiestaCorrente.getVeicoloId());
                        int percentuale = richiestaCorrente.getPercentualeDesiderata() - richiestaCorrente.getPercentualeIniziale();
                        int tempoDiRicarica = calcolaTempoRicarica(percentuale);

                        Thread.sleep(tempoDiRicarica); // Simula il tempo di ricarica

                        System.out.println("Ricarica completata per il veicolo: " + richiestaCorrente.getVeicoloId());
                        ricaricaRepositoryService.updateStatoById((long) richiestaCorrente.getIdRichiesta(), StatoRicarica.COMPLETED.ordinal());
                        pagamentoService.calcolaImporto((int) tempoDiRicarica/1000, TipoServizio.RICARICA, richiestaCorrente.getIdUtente());

                        /*  Qua si fa partire il service che si occupa di gestire il pagamento per la ricarica   */
                        if (richiestaCorrente.getRiceviMessaggio()) {
                            String topic = "Parcheggio/Messaggio/Posto/" + postoId;
                            mqttPublisher.publish(topic, "Notifica: ricarica del veicolo con targa "
                                    + richiestaCorrente.getVeicoloId() + " terminata");
                        }
                    }
                }
            } catch (InterruptedException | MqttException e) {
                System.out.println("Errore durante la ricarica: " + e.getMessage());
            } finally {
                isRicaricaInCorso = false;
                System.out.println("Coda di ricarica vuota. Nessuna ricarica in corso.");
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
