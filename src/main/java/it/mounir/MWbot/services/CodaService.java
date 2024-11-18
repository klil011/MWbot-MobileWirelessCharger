package it.mounir.MWbot.services;

import it.mounir.MWbot.domain.RichiestaVeicolo;
import it.mounir.MWbot.domain.TipoServizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Service
public class CodaService {

    private final ParcheggioService parcheggioService;
    private final Queue<RichiestaVeicolo> codaRichieste = new LinkedList<>();

    public CodaService(ParcheggioService parcheggioService) {
        this.parcheggioService = parcheggioService;
    }

    public void aggiungiInCoda(int idUtente, String veicoloId, Boolean riceviMessaggio, TipoServizio tipoServizio) {
        RichiestaVeicolo richiesta = new RichiestaVeicolo(idUtente, veicoloId, riceviMessaggio, tipoServizio);
        codaRichieste.add(richiesta);

        System.out.println("Veicolo " + veicoloId + " è stato messo in coda.");
    }

    public RichiestaVeicolo rimuoviDallaCoda() {
        return codaRichieste.poll();
    }

    public void gestisciCoda (String postoLiberoId) {
        if(this.codaNonVuota()) {

            RichiestaVeicolo richiestaVeicolo = this.rimuoviDallaCoda();
            parcheggioService.occupaPosto(postoLiberoId, richiestaVeicolo.isPreferenzaMessaggio());

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
