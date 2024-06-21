package com.TatapCasino.service;

import com.TatapCasino.converter.RoomConverter;
import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.RoomModel;
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


    public List<RoomDTO> getAllRooms() {
        final List<RoomModel> rooms = roomRepository.findAll();
        final List<RoomDTO> roomDTOS = rooms.stream().map(roomModel -> roomConverter.convertToDTO(roomModel)).toList();
        return roomDTOS;
    }

    public ResponseEntity<RoomDTO> getRoomById(long id) {
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
        final Optional<PlayerModel> player = playerService.getPlayerById(roomDTO.getOwnerId());

        if (existingRoom.isPresent()) {
            throw new RuntimeException("Room with ID " + roomDTO.getId() + " already exists");
        }

        if (player.isPresent()) {
            validatePlayerForRoom(player.get(), roomDTO.getId());

            final RoomModel roomModel = roomConverter.convertToModel(roomDTO);
            saveRoom(roomModel);
            return roomConverter.convertToDTO(roomModel);
        } else {
            throw new RuntimeException("Player with ID " + roomDTO.getOwnerId() + " not found");
        }
    }

    @Transactional
    public ResponseEntity<RoomDTO> joinPlayerToRoom(final RoomDTO roomDTO) {
        final Optional<RoomModel> room = roomRepository.findById(roomDTO.getId());
        final RoomModel roomModel = room.orElseThrow(() -> new RuntimeException("Room not found"));

        final Long playerId = roomDTO.getPlayerIds().get(0);
        final Optional<PlayerModel> player = playerService.getPlayerById(playerId);

        if (player.isPresent()) {
            validatePlayerForRoom(player.get(), roomModel.getId());

            roomModel.getPlayers().add(player.get());
            player.get().setCurrentRoom(roomModel);

            saveRoom(roomModel);
            playerService.savePlayer(player.get());

            return ResponseEntity.ok(roomConverter.convertToDTO(roomModel));
        } else {
            throw new RuntimeException("Player with ID " + playerId + " not found");
        }
    }

    @Transactional
    public ResponseEntity<RoomDTO> exitPlayerFromRoom(final RoomDTO roomDTO) {
        final Optional<RoomModel> room = roomRepository.findById(roomDTO.getId());
        final RoomModel roomModel = room.orElseThrow(() -> new RuntimeException("Room not found"));

        final Long playerId = roomDTO.getPlayerIds().get(0);
        final Optional<PlayerModel> player = playerService.getPlayerById(playerId);

        if (player.isPresent()) {
            final PlayerModel playerModel = player.get();

            if (roomModel.getOwner().equals(playerModel)) {
                // If the player owns the room, remove references and delete the room
                roomModel.getPlayers().forEach(p -> {
                    p.setCurrentRoom(null);
                    playerService.savePlayer(p);
                });
                playerModel.setOwnedRoom(null);
                playerService.savePlayer(playerModel);
                roomRepository.delete(roomModel);
            } else if (roomModel.getPlayers().contains(playerModel)) {
                // If the player is in the room, remove the player from the room
                roomModel.getPlayers().remove(playerModel);
                playerModel.setCurrentRoom(null); // Remove the player's current room
                saveRoom(roomModel);
                playerService.savePlayer(playerModel); // Save the player to update their room status
            } else {
                throw new RuntimeException("Player with ID " + playerId + " is not in the room");
            }

            return ResponseEntity.ok(roomConverter.convertToDTO(roomModel));
        } else {
            throw new RuntimeException("Player with ID " + playerId + " not found");
        }
    }

    private void validatePlayerForRoom(final PlayerModel playerModel, final Long roomId) {
        final RoomModel currentRoom = playerModel.getCurrentRoom();
        final RoomModel ownedRoom = playerModel.getOwnedRoom();

        if (currentRoom != null) {
            throw new RuntimeException("Player with ID " + playerModel.getId() + " is already in a room");
        }

        if (ownedRoom != null && ownedRoom.getId().equals(roomId)) {
            throw new RuntimeException("Player with ID " + playerModel.getId() + " is the owner of the room");
        }
    }
}
