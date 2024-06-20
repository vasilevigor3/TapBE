package com.TatapCasino.converter;

import com.TatapCasino.dto.TGUserDTO;
import com.TatapCasino.model.TGUserModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TGUserConverter {

    public TGUserModel convertToModel(TGUserDTO tgUserDTO) {
        TGUserModel tgUserModel = new TGUserModel();
        tgUserModel.setId(tgUserDTO.getId());
        tgUserModel.setName(tgUserDTO.getName());
        return tgUserModel;
    }

    public TGUserDTO convertToDTO(TGUserModel tgUserModel) {
        TGUserDTO tgUserDTO = new TGUserDTO();
        tgUserDTO.setId(tgUserModel.getId());
        tgUserDTO.setName(tgUserModel.getName());
        if (tgUserModel.getPlayer() != null) {
            tgUserDTO.setPlayerId(tgUserModel.getPlayer().getId());
        }
        return tgUserDTO;
    }
}

