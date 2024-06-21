package com.TatapCasino.service;

import com.TatapCasino.controllers.ScoreRequest;
import com.TatapCasino.converter.PlayerConverter;
import com.TatapCasino.converter.RoomConverter;
import com.TatapCasino.dto.PlayerDTO;
import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.model.GameModel;
import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.RoomModel;
import com.TatapCasino.model.ScoreModel;
import com.TatapCasino.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomConverter roomConverter;
    @Autowired
    private PlayerService playerService;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private PlayerConverter playerConverter;
    @Autowired
    private GameService gameService;


    public List<RoomDTO> getAllRooms() {
        final List<RoomModel> rooms = roomRepository.findAll();
        final List<RoomDTO> roomDTOS = rooms.stream().map(roomModel -> roomConverter.convertToDTO(roomModel)).toList();
        return roomDTOS;
    }

    public ResponseEntity<RoomDTO> getRoomDTOById(long id) {
        final Optional<RoomModel> roomModelOptional = roomRepository.findById(id);
        return roomModelOptional.map(roomModel -> ResponseEntity.ok(roomConverter.convertToDTO(roomModel)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public Optional<RoomModel> getRoomById(long id) {
        return roomRepository.findById(id);
    }


    public RoomModel saveRoom(RoomModel room) {
        final RoomModel savedRoom = roomRepository.save(room);

        List<PlayerModel> players = room.getPlayers();
        players.forEach(player -> player.setCurrentRoom(savedRoom));
        playerService.saveAllPlayers(players);

        return savedRoom;
    }

    public void deleteRoom(RoomModel roomModel) {
        roomRepository.delete(roomModel);
    }

    public PlayerModel addScoreModelToPlayer(final PlayerModel playerModel, final GameModel gameModel) {
        final ScoreModel scoreModel = new ScoreModel();
        scoreModel.setGameId(gameModel.getId());
        playerModel.getScores().add(scoreModel);

        return playerService.savePlayer(playerModel);
    }

    public RoomDTO createRoom(final RoomDTO roomDTO) {

        final Optional<RoomModel> existingRoom = roomRepository.findById(roomDTO.getId());
        final Optional<PlayerModel> player = playerService.getPlayerById(roomDTO.getOwnerId());

        if (existingRoom.isPresent()) {
            throw new RuntimeException("Room with ID " + roomDTO.getId() + " already exists");
        }

        if (player.isPresent()) {
            final PlayerModel playerModel = player.get();
            validatePlayerForRoom(playerModel, roomDTO.getId());

            final RoomModel roomModel = roomConverter.convertToModel(roomDTO);
            addScoreModelToPlayer(playerModel, roomModel.getGameModel());
            roomModel.setIsGameStarted(false);
            roomModel.setIsGameFinished(false);
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
            final PlayerModel playerModel = player.get();

            validatePlayerForRoom(playerModel, roomModel.getId());

            roomModel.getPlayers().add(playerModel);
            playerModel.setCurrentRoom(roomModel);

            addScoreModelToPlayer(playerModel, roomModel.getGameModel());

            if (roomModel.getMaxPlayers() == roomModel.getPlayers().size()) {
                roomModel.setIsGameStarted(true);
            }

            saveRoom(roomModel);
            playerService.savePlayer(playerModel);

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

                roomModel.getPlayers().forEach(p -> {
                    p.setCurrentRoom(null);
                    playerService.savePlayer(p);
                });
                playerModel.setOwnedRoom(null);
                playerService.savePlayer(playerModel);
                roomRepository.delete(roomModel);
            } else if (roomModel.getPlayers().contains(playerModel)) {

                roomModel.getPlayers().remove(playerModel);
                playerModel.setCurrentRoom(null);
                saveRoom(roomModel);
                playerService.savePlayer(playerModel);
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

    @Transactional
    public PlayerDTO finishGame(ScoreRequest scoreRequest) {
        final RoomModel roomModel = getRoomModel(scoreRequest.getRoomId());
        if (roomModel.getIsGameStarted()) {
            final Long gameModelId = roomModel.getGameModel().getId();
            final Map<PlayerModel, Long> playersCurrentRoomWithScores = updateScores(scoreRequest, gameModelId);
            final PlayerModel playerWithMaxScore = getPlayerWithMaxScore(playersCurrentRoomWithScores);
            processWinner(roomModel, gameModelId, playerWithMaxScore);
            return playerConverter.convertToDTO(playerWithMaxScore);
        } else {
            throw new RuntimeException("Game in room with id: " + roomModel.getId() + "wasn't started yet");
        }
    }

    private RoomModel getRoomModel(String roomId) {
        return getRoomById(Long.parseLong(roomId))
                .orElseThrow(() -> new RuntimeException("Room with id: " + roomId + " doesn't exist"));
    }

    private Map<PlayerModel, Long> updateScores(ScoreRequest scoreRequest, Long gameModelId) {
        final Map<PlayerModel, Long> playersCurrentRoomWithScores = new HashMap<>();
        for (Map<String, String> playerScore : scoreRequest.getPlayersScores()) {
            for (Map.Entry<String, String> entry : playerScore.entrySet()) {
                PlayerModel playerModel = getPlayerModel(entry.getKey());
                updatePlayerScore(playerModel, gameModelId, Integer.parseInt(entry.getValue()));
                playersCurrentRoomWithScores.put(playerModel, Long.parseLong(entry.getValue()));
                playerService.savePlayer(playerModel);
            }
        }
        return playersCurrentRoomWithScores;
    }

    private PlayerModel getPlayerModel(String playerIdStr) {
        final long playerId = Long.parseLong(playerIdStr);
        return playerService.getPlayerById(playerId)
                .orElseThrow(() -> new RuntimeException("Player with id: " + playerId + " doesn't exist"));
    }

    private void updatePlayerScore(PlayerModel playerModel, Long gameModelId, int score) {
        playerModel.getScores().stream()
                .filter(scoreModel -> scoreModel.getGameId().equals(gameModelId))
                .findFirst()
                .ifPresent(scoreModel -> {
                    scoreModel.setScore(score);
                    scoreService.saveScore(scoreModel);
                });
    }

    private PlayerModel getPlayerWithMaxScore(Map<PlayerModel, Long> playersCurrentRoomWithScores) {
        return playersCurrentRoomWithScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("No winner found"))
                .getKey();
    }

    private void processWinner(RoomModel roomModel, Long gameModelId, PlayerModel playerWithMaxScore) {
        final GameModel gameModel = gameService.findGameById(gameModelId)
                .orElseThrow(() -> new RuntimeException("Game with id: " + gameModelId + " doesn't exist"));
        updateBalanceToWinner(playerWithMaxScore, roomModel);
        playerWithMaxScore.getWonGames().add(gameModel);
        gameModel.setWinner(playerWithMaxScore);
        gameModel.setRoom(null);
        playerService.savePlayer(playerWithMaxScore);
        gameService.saveGame(gameModel);
        roomModel.getPlayers().forEach(player -> {
            player.setCurrentRoom(null);
            playerService.savePlayer(player);
        });
        deleteRoom(roomModel);
    }

    private void updateBalanceToWinner(final PlayerModel playerWithMaxScore, RoomModel roomModel){
        final double bet = roomModel.getBet();
        final double awward = roomModel.getMaxPlayers() * bet;

        playerWithMaxScore.setBalance(playerWithMaxScore.getBalance() + awward);
    }
}
