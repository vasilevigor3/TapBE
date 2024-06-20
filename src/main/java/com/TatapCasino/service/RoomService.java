package com.TatapCasino.service;

import com.TatapCasino.converter.RoomConverter;
import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.RoomModel;
import com.TatapCasino.repository.GameRepository;
import com.TatapCasino.repository.PlayerRepository;
import com.TatapCasino.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomConverter roomConverter;
    @Autowired
    private PlayerService playerService;


    public List<RoomModel> getAllRooms() {
        return roomRepository.findAll();
    }

    public ResponseEntity<RoomDTO> getRoomById(long id){
        Optional<RoomModel> roomModelOptional = roomRepository.findById(id);
        return roomModelOptional.map(roomModel -> ResponseEntity.ok(roomConverter.convertToDTO(roomModel)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    public RoomModel saveRoom(RoomModel room) {
        final RoomModel savedRoom = roomRepository.save(room);

        List<PlayerModel> players = room.getPlayers();
        players.forEach(player -> player.setCurrentRoom(savedRoom));
        playerService.saveAllPlayers(players);

        return savedRoom;
    }

    public RoomDTO createRoom(final RoomDTO roomDTO) {
        final Optional<RoomModel> existingRoom = roomRepository.findById(roomDTO.getId());
        playerService.getPlayerById(roomDTO.getOwnerId());

        if (existingRoom.isPresent()) {
            throw new RuntimeException("Room with ID " + roomDTO.getId() + " already exists");
        }

        final RoomModel roomModel = roomConverter.convertToModel(roomDTO);
        saveRoom(roomModel);
        return roomConverter.convertToDTO(roomModel);
    }

    @Transactional
    public
    ResponseEntity<RoomDTO> joinPlayerToRoom(final RoomDTO roomDTO) {
        final Optional<RoomModel> roomModelOptional = roomRepository.findById(roomDTO.getId());
        final RoomModel roomModel = roomModelOptional.orElseThrow(() -> new RuntimeException("Room not found"));
        roomModel.getPlayers().addAll(playerService.getPlayersByIds(roomDTO.getPlayerIds()));
        saveRoom(roomModel);
        return roomModelOptional.map(roomModel2 -> ResponseEntity.ok(roomConverter.convertToDTO(roomModel2)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
