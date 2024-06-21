package com.TatapCasino.converter;
import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.enums.GameType;
import com.TatapCasino.model.GameModel;
import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.RoomModel;
import com.TatapCasino.service.GameService;
import com.TatapCasino.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomConverter {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameService gameService;

    public RoomModel convertToModel(RoomDTO roomDTO) {

        RoomModel roomModel = new RoomModel();

        roomModel.setRoomName(roomDTO.getRoomName());
        roomModel.setMaxPlayers(roomDTO.getMaxPlayers());
        roomModel.setBet(roomDTO.getBet());

        PlayerModel owner = playerService.getOrCreatePlayer(roomDTO.getOwnerId());
        roomModel.setOwner(owner);

        GameModel gameModel = new GameModel();
        gameModel.setGameType(GameType.valueOf(roomDTO.getGameType()));
        gameModel.setRoom(roomModel);
        gameService.saveGame(gameModel);

        roomModel.setGameModel(gameModel);

        List<PlayerModel> players;
        if (roomDTO.getPlayerIds() == null) {
            players = playerService.getPlayersByIds(List.of(owner.getId()));
        } else {
            players = playerService.getPlayersByIds(roomDTO.getPlayerIds());
        }
        roomModel.setPlayers(players);

        return roomModel;
    }

    public RoomDTO convertToDTO(RoomModel roomModel) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(roomModel.getId());
        roomDTO.setRoomName(roomModel.getRoomName());
        roomDTO.setMaxPlayers(roomModel.getMaxPlayers());
        roomDTO.setBet(roomModel.getBet());
        roomDTO.setGameType(roomModel.getGameModel().getGameType().toString());
        if (roomModel.getGameModel().getWinner() != null) {
            roomDTO.setWinnerId(roomModel.getGameModel().getWinner().getId());
        } else {
            roomDTO.setWinnerId(null);
        }
        roomDTO.setOwnerId(roomModel.getOwner().getId());
        final List<Long> playersIds = roomModel
                .getPlayers()
                .stream()
                .map(PlayerModel::getId).toList();
        roomDTO.setPlayerIds(playersIds);
        roomDTO.setIsGameStarted(roomModel.getIsGameStarted());
        roomDTO.setIsGameFinished(roomModel.getIsGameFinished());
        return roomDTO;
    }
}
