package com.TatapCasino.converter;

import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.TGUserModel;
import org.springframework.stereotype.Service;

@Service
public class TGUserToPlayerConverter {
    public PlayerModel convert(TGUserModel tgUser) {
        PlayerModel player = new PlayerModel();
        player.setId(tgUser.getId());
        player.setName(tgUser.getName());
        player.setTgUserAccount(tgUser);
        return player;
    }
}

