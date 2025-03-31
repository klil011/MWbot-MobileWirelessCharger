package it.mounir.MWbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mounir.MWbot.DTO.RichiestaRicarica;
import it.mounir.MWbot.DTO.RichiestaSosta;
import it.mounir.MWbot.domain.TipoServizio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MWbotApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	@DirtiesContext
	public void testEffettuaSosta() throws Exception {

		RichiestaSosta richiesta = new RichiestaSosta();
		richiesta.setIdUtente(7);
		richiesta.setVeicoloId("Targa7");
		richiesta.setTipoServizio(TipoServizio.SOSTA);

		this.mockMvc
				.perform(post("/sosta")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(richiesta)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Richiesta di sosta ricevuta per veicolo Targa7")));

		this.monitor(4);
	}

	/* TODO: effettuare i test che verifichino gli ID restituiti negli header delle richieste POST */

	@Test
	@DirtiesContext
	public void testEffettuaRicarica() throws Exception {

		RichiestaRicarica richiesta = new RichiestaRicarica();
		richiesta.setIdUtente(6);
		richiesta.setVeicoloId("Targa6");
		richiesta.setPercentualeIniziale(0);
		richiesta.setPercentualeDesiderata(100);
		richiesta.setTipoServizio(TipoServizio.RICARICA);
		richiesta.setRiceviMessaggio(false);

		this.mockMvc
				.perform(post("/ricarica")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(richiesta)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Richiesta di ricarica ricevuta per veicolo Targa6")));

		this.monitor(4);
	}

	@Test
	@DirtiesContext
	public void testMonitor() throws Exception {
		this.monitor(5);
	}


	private void monitor (int parcheggiLiberi) throws Exception{
		this.mockMvc
				.perform(get("/monitor"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Parcheggi disponibili: " + parcheggiLiberi)));
	}

}
