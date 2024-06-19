package com.TatapCasino.service;

import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<PlayerModel> getAllPlayers() {
        return playerRepository.findAll();
    }

    public PlayerModel savePlayer(PlayerModel player) {
        return playerRepository.save(player);
    }

    public List<PlayerModel> saveAllPlayers(List<PlayerModel> players) {
        return playerRepository.saveAll(players);
    }
}
