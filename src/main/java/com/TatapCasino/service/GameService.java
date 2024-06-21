package com.TatapCasino.service;

import com.TatapCasino.model.GameModel;
import com.TatapCasino.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    public GameModel saveGame(final GameModel game) {
        return gameRepository.save(game);
    }

    public Optional<GameModel> findGameById(final Long id){
        return gameRepository.findById(id);
    }

    public List<GameModel> findAllGames(){
        return gameRepository.findAll();
    }
}
