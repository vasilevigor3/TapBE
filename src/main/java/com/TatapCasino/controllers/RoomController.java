package com.TatapCasino.controllers;

import com.TatapCasino.model.RoomModel;
import com.TatapCasino.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomModel> getAllRooms(){
        return roomService.getAllRooms();
    }

    @PostMapping("/create-room")
    public RoomModel createRoom(@RequestBody RoomModel roomData) {
        RoomModel room = new RoomModel();
        room.setRoomName(roomData.getRoomName());
        room.setMaxPlayers(roomData.getMaxPlayers());
        room.setBet(roomData.getBet());

        return roomService.saveRoom(room);
    }
}
