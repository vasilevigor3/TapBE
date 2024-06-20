package com.TatapCasino.service;

import com.TatapCasino.model.GameModel;
import com.TatapCasino.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    public GameModel saveGame(GameModel game) {
        return gameRepository.save(game);
    }
}
