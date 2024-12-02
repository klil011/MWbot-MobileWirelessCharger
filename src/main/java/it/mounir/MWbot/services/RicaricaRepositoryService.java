package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Ricarica;
import it.mounir.MWbot.repositories.RicaricaRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RicaricaRepositoryService {

    private final RicaricaRepository ricaricaRepository;
    private final JdbcTemplate jdbcTemplate;

    public RicaricaRepositoryService(RicaricaRepository ricaricaRepository, JdbcTemplate jdbcTemplate) {
        this.ricaricaRepository = ricaricaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Ricarica createOrUpdateRicarica(Ricarica ricarica) {
        return ricaricaRepository.save(ricarica);
    }

    public Optional<Ricarica> getRicaricaById(Long id) {
        return ricaricaRepository.findById(id);
    }

    public Iterable<Ricarica> getAllRicariche() {
        return ricaricaRepository.findAll();
    }

    public int updateStatoById(Long id, int nuovoValore) {
        String sql = "UPDATE \"ricarica\" SET \"stato\" = ? WHERE \"id_ricarica\" = ?";
        return jdbcTemplate.update(sql, nuovoValore, id);
    }

    public int updateTempoById(Long id, long nuovoValore) {
        String sql = "UPDATE ricarica SET tempo_sosta = ? WHERE id_ricarica = ?";
        return jdbcTemplate.update(sql, nuovoValore, id);
    }
}
