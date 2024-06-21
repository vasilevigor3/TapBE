package com.TatapCasino.controllers;

import com.TatapCasino.converter.PlayerConverter;
import com.TatapCasino.dto.PlayerDTO;
import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.model.GameModel;
import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.RoomModel;
import com.TatapCasino.model.ScoreModel;
import com.TatapCasino.service.GameService;
import com.TatapCasino.service.PlayerService;
import com.TatapCasino.service.RoomService;
import com.TatapCasino.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private PlayerConverter playerConverter;


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

//    @PostMapping("/start-game")
//    //TODO dangerous method, anyone could call it
//    // -add to RoomModel/RoomDto gameStarted
//    // don't call it, only for testing, game starts after 10/10 (joinRoom)
//    public ResponseEntity<RoomDTO> startGame(@RequestBody RoomDTO roomDTO) {
//        return roomService.startGame(roomDTO);
//    }

    @PostMapping("/finish-game")
    //TODO dangerous method, anyone could call it
    // TODO VOID
    @Transactional
    public PlayerDTO finishGame(@RequestBody ScoreRequest scoreRequest) {
        String roomId = scoreRequest.getRoomId();
        final Optional<RoomModel> roomById = roomService.getRoomById(Long.parseLong(roomId));
        final RoomModel roomModel = roomById.orElseThrow(() -> new RuntimeException("Room with id: " + roomById + "don't exist"));
        final Long gameModelId = roomModel.getGameModel().getId();

        List<Map<String, String>> playersScores = scoreRequest.getPlayersScores();

        HashMap<PlayerModel, Long> playersCurrentRoomWithScores = new HashMap<>();

        for (Map<String, String> playerScore : playersScores) {
            for (Map.Entry<String, String> entry : playerScore.entrySet()) {
                final String playerId = entry.getKey();
                final String score = entry.getValue();

                final Optional<PlayerModel> playerById = playerService.getPlayerById(Long.parseLong(playerId));

                if (playerById.isPresent()) {
                    final PlayerModel playerModel = playerById.get();
                    final Optional<ScoreModel> currentScoreModel = playerModel.getScores().stream()
                            .filter(scoreModel -> scoreModel.getGameId().equals(gameModelId))
                            .findFirst();

                    currentScoreModel.ifPresent(scoreModel -> {
                        scoreModel.setScore(Integer.parseInt(score));
                        scoreService.saveScore(scoreModel);
                    });
                    playersCurrentRoomWithScores.put(playerModel, Long.parseLong(score));
                    playerService.savePlayer(playerModel);
                }
            }
        }

        Optional<Map.Entry<PlayerModel, Long>> maxEntry = playersCurrentRoomWithScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent()) {
            PlayerModel playerWithMaxScore = maxEntry.get().getKey();

            final Optional<GameModel> gameById = gameService.findGameById(gameModelId);

            final GameModel gameModel = gameById.get();
            playerWithMaxScore.getWonGames().add(gameModel);

            playerService.savePlayer(playerWithMaxScore);

            gameModel.setWinner(playerWithMaxScore);
            gameService.saveGame(gameModel);

            roomModel.setIsGameFinished(true);

            roomService.saveRoom(roomModel);

            gameModel.setRoom(null);
            gameService.saveGame(gameModel);

            roomModel.getPlayers().forEach(player -> {
                player.setCurrentRoom(null);
                playerService.savePlayer(player);
            });

            roomService.deleteRoom(roomModel);

            return playerConverter.convertToDTO(playerWithMaxScore);
        }
        return null;
    }
}
