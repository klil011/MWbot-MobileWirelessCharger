package it.mounir.MWbot.services;

import it.mounir.MWbot.model.Mwbot;
import it.mounir.MWbot.repositories.MwbotRepository;
import org.springframework.stereotype.Service;

@Service
public class MwbotService {

    private final MwbotRepository mwbotRepository;

    public MwbotService(MwbotRepository mwbotRepository) {
        this.mwbotRepository = mwbotRepository;
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
