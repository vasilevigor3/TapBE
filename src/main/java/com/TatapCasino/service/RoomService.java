package com.TatapCasino.service;

import com.TatapCasino.model.RoomModel;
import com.TatapCasino.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<RoomModel> getAllRooms() {
        return roomRepository.findAll();
    }

    public RoomModel saveRoom(RoomModel room) {
        return roomRepository.save(room);
    }
}
