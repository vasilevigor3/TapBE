package com.TatapCasino.controllers;

import com.TatapCasino.dto.PlayerDTO;
import com.TatapCasino.model.GameModel;
import com.TatapCasino.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    GameService gameService;

    @GetMapping("/games")
    public List<GameModel> getAllPlayers() {
        return gameService.findAllGames();
    }
}
