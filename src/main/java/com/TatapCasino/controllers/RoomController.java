package com.TatapCasino.controllers;

import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.service.GameService;
import com.TatapCasino.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private GameService gameService;


    @GetMapping("/getAllRooms")
    public List<RoomDTO> getAllRooms(){
        return roomService.getAllRooms();
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
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
    //TODO:
    // MAKE LOGIC WITH EXIT FROM ROOM
    // ALSO CHECK WATS HAPPEN WITH GAME OBJECT SCORE ETC
    public ResponseEntity<RoomDTO> exitRoom(@RequestBody RoomDTO roomDTO) {
        return roomService.exitPlayerFromRoom(roomDTO);
    }
}
