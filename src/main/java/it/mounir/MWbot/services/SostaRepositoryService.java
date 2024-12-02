package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Sosta;
import it.mounir.MWbot.repositories.SostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SostaRepositoryService {

    private final SostaRepository sostaRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SostaRepositoryService(SostaRepository sostaRepository, JdbcTemplate jdbcTemplate) {
        this.sostaRepository = sostaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Sosta createOrUpdateSosta (Sosta servizioSosta) {
        return sostaRepository.save(servizioSosta);
    }

    public Optional<Sosta> getSostaById(Long id) {
        return sostaRepository.findById(id);
    }

    public int updateStatoById(Long id, int nuovoValore) {
        String sql = "UPDATE \"sosta\" SET \"stato\" = ? WHERE \"id_sosta\" = ?";
        return jdbcTemplate.update(sql, nuovoValore, id);
    }

    public int updateTempoById(Long id, long nuovoValore) {
        String sql = "UPDATE sosta SET tempo_sosta = ? WHERE id_sosta = ?";
        return jdbcTemplate.update(sql, nuovoValore, id);
    }
}
