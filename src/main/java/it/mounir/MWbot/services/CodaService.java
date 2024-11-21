package it.mounir.MWbot.services;

import it.mounir.MWbot.DTO.Richiesta;
import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.domain.TipoServizio;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class CodaService {

    private final ParcheggioService parcheggioService;
    private final Queue<Richiesta> codaRichieste = new LinkedList<>();

    public CodaService(ParcheggioService parcheggioService) {
        this.parcheggioService = parcheggioService;
    }

    public void aggiungiInCoda(Richiesta richiesta) {
        codaRichieste.add(richiesta);

        System.out.println("Veicolo " + richiesta.getVeicoloId() + " è stato messo in coda.");
    }

    public Richiesta rimuoviDallaCoda() {
        return codaRichieste.poll();
    }

    public void gestisciCoda (String postoLiberoId) {
        if(this.codaNonVuota()) {

            Richiesta richiestaVeicolo = this.rimuoviDallaCoda();
            System.out.println(richiestaVeicolo.toString());

            if(richiestaVeicolo instanceof RichiestaRicarica) {
                parcheggioService.occupaPosto(postoLiberoId, ((RichiestaRicarica) richiestaVeicolo).getRiceviMessaggio());
            }
            else {
                parcheggioService.occupaPosto(postoLiberoId, false);
            }


            if(richiestaVeicolo.getTipoServizio().equals(TipoServizio.RICARICA)) {
                System.out.println("Veicolo " + richiestaVeicolo.getVeicoloId() + " ha occupato la stazione di ricarica "
                        + postoLiberoId + " dalla coda.");
            }
            else {
                System.out.println("Veicolo " + richiestaVeicolo.getVeicoloId() + " ha occupato il posto "
                        + postoLiberoId + " dalla coda.");
            }
        }
    }

    public int getCodaRichiesteCount() {
        return codaRichieste.size();
    }

    public boolean codaNonVuota() {
        return !codaRichieste.isEmpty();
    }
}
