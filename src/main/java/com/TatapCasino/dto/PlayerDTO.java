package com.TatapCasino.dto;

import com.TatapCasino.model.RoomModel;
import com.TatapCasino.model.TGUserModel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;


@Setter
@Getter
public class PlayerDTO {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    private double balance;
    @NotNull
    private long tgUserAccountId;

    private Long ownedRoomId;
    private Long currentRoomId;
    private List<Long> wonGamesIds;
}
