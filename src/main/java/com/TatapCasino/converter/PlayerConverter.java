package com.TatapCasino.converter;

import com.TatapCasino.dto.PlayerDTO;
import com.TatapCasino.model.PlayerModel;
import org.springframework.stereotype.Service;

@Service
public class PlayerConverter {
    public PlayerDTO convertToDTO(PlayerModel playerModel) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(playerModel.getId());
        playerDTO.setName(playerModel.getName());
        playerDTO.setBalance(playerModel.getBalance());

        if (playerModel.getTgUserAccount() != null) {
            playerDTO.setTgUserAccountId(playerModel.getTgUserAccount().getId());
        }

        if (playerModel.getOwnedRoom() != null) {
            playerDTO.setOwnedRoomId(playerModel.getOwnedRoom().getId());
        }

        if (playerModel.getCurrentRoom() != null) {
            playerDTO.setCurrentRoomId(playerModel.getCurrentRoom().getId());
        }

        return playerDTO;
    }

}
