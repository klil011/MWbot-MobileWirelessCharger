package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Prenotazione;
import it.mounir.MWbot.repositories.ParcheggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParcheggioRepositoryService {

    public final JdbcTemplate jdbcTemplate;
    public final ParcheggioRepository parcheggioRepository;

    @Autowired
    public ParcheggioRepositoryService(JdbcTemplate jdbcTemplate, ParcheggioRepository parcheggioRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.parcheggioRepository = parcheggioRepository;
    }

    public int getCostoSostaById(Long id) {
        return parcheggioRepository.getCostoSostaById(id);
    }

    public int getCostoKwById(Long id) {
        return parcheggioRepository.getCostoKwById(id);
    }

    public int updateCostoSostaById(long id, int nuovoValore) {
        String sql = "UPDATE parcheggio SET costososta = ? WHERE id_parcheggio = ?";
        return jdbcTemplate.update(sql, nuovoValore, id);
    }

    public int updateCostoKwById(long id, int nuovoValore) {
        String sql = "UPDATE parcheggio SET costokw = ? WHERE id_parcheggio = ?";
        return jdbcTemplate.update(sql, nuovoValore, id);
    }
}
