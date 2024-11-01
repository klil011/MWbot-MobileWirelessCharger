package it.mounir.MWbot.services;

import it.mounir.MWbot.domain.RichiestaVeicolo;
import it.mounir.MWbot.domain.TipoServizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SostaService {

    private final ParcheggioService parcheggioService;
    private final CodaService codaSostaService;

    @Autowired
    public SostaService(ParcheggioService parcheggioService, CodaService codaSostaService) {
        this.parcheggioService = parcheggioService;
        this.codaSostaService = codaSostaService;
    }

    public void richiestaSosta(String veicoloId) {
        String postoLibero = parcheggioService.getPrimoPostoLibero();
        if (postoLibero != null) {
            parcheggioService.occupaPosto(postoLibero, false);
            System.out.println("Veicolo " + veicoloId + " ha occupato il posto " + postoLibero + ".");
        } else {
            codaSostaService.aggiungiInCoda(veicoloId, false, TipoServizio.SOSTA);
        }
    }

    public String statoParcheggio() {
        return "Posti liberi: " + parcheggioService.getPostiLiberiCount() +
                ", Veicoli in coda: " + codaSostaService.getCodaRichiesteCount();
    }
}



