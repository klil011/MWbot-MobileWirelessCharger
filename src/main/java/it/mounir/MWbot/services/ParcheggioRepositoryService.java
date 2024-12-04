package it.mounir.MWbot.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ParcheggioRepositoryService {

    public final JdbcTemplate jdbcTemplate;

    public ParcheggioRepositoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
