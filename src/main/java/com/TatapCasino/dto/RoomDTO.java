
package com.TatapCasino.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RoomDTO {

    private Long id;
    @NotNull
    @NotBlank
    private String roomName;
    @NotNull
    private int maxPlayers;
    private double bet;
    @NotNull
    private Long ownerId;
    @NotNull
    @NotBlank
    private String gameType;

    private List<Long> playerIds;
}
