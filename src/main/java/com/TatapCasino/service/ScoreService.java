package com.TatapCasino.service;

import com.TatapCasino.model.ScoreModel;
import com.TatapCasino.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    public ScoreModel saveScore(ScoreModel scoreModel) {
        return scoreRepository.save(scoreModel);
    }
}
