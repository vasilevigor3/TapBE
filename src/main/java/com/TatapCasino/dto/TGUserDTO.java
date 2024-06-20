package com.TatapCasino.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TGUserDTO {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    private Long playerId;
}
