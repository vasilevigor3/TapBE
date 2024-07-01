package com.TatapCasino.controllers;

import com.TatapCasino.converter.PlayerConverter;
import com.TatapCasino.dto.PlayerDTO;
import com.TatapCasino.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerConverter playerConverter;

    @GetMapping("/players")
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayersDTO();
    }
    @PostMapping("/players")
    public List<PlayerDTO> getPlayersByIds(@RequestBody final Map<String, List<Long>> requestData) {
        List<Long> ids = requestData.get("ids");
        return playerService.getPlayersDtoByIds(ids);
    }
    @PostMapping("/getOrCreatePlayer")
    public ResponseEntity<?> getOrCreatePlayer(@RequestBody final Map<String, String> requestData) {
        final long id = Long.parseLong(requestData.get("id"));
        try {
            final PlayerDTO playerDTO = playerService.getOrCreatePlayerDTO(id);
            return ResponseEntity.ok(playerDTO);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
