package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.Richiesta;
import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.domain.StatoRicarica;
import it.mounir.MWbot.domain.StatoSosta;
import it.mounir.MWbot.domain.TipoServizio;
import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.model.Sosta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class ParcheggioService {

    private final Set<String> postiLiberi;
    private final Map<String, Long> tempoPostiOccupati;
    private final int maxPosti = 5;

    private final RicaricaRepositoryService ricaricaRepositoryService;
    private final SostaRepositoryService sostaRepositoryService;

    @Autowired
    public ParcheggioService(RicaricaRepositoryService ricaricaRepositoryService, SostaRepositoryService sostaRepositoryService) {
        this.ricaricaRepositoryService = ricaricaRepositoryService;
        this.sostaRepositoryService = sostaRepositoryService;

        this.postiLiberi = new HashSet<>();
        this.tempoPostiOccupati = new HashMap<>();

        for (int i = 1; i <= maxPosti; i++) {
            postiLiberi.add(String.valueOf(i));
        }
    }

    public boolean occupaPosto(String postoId, Richiesta richiesta) {
        if (postiLiberi.remove(postoId)) {
            tempoPostiOccupati.put(postoId, System.currentTimeMillis());
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