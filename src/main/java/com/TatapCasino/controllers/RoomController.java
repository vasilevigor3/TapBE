package com.TatapCasino.controllers;

import com.TatapCasino.dto.PlayerDTO;
import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.service.PlayerService;
import com.TatapCasino.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private PlayerService playerService;



    @GetMapping("/getAllRooms")
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return roomService.getRoomDTOById(id);
    }


    @PostMapping("/create-room")
    public RoomDTO createRoom(@Validated @RequestBody RoomDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @PostMapping("/join-room")
    public ResponseEntity<RoomDTO> joinRoom(@RequestBody RoomDTO roomDTO) {
        return roomService.joinPlayerToRoom(roomDTO);
    }

    @PostMapping("/exit-room")
    public ResponseEntity<RoomDTO> exitRoom(@RequestBody RoomDTO roomDTO) {
        return roomService.exitPlayerFromRoom(roomDTO);
    }

    @PostMapping("/finish-game")
    //TODO dangerous method, anyone could call it
    // try catch as player controller
    @Transactional
    public ResponseEntity<PlayerDTO> finishGame(@RequestBody ScoreRequest scoreRequest) {
        try {
            final PlayerDTO winner = roomService.finishGame(scoreRequest);
            return ResponseEntity.ok(winner);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
