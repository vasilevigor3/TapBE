package com.TatapCasino.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class RoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    private String roomName;
    @NotNull
    private int maxPlayers;
    @NotNull
    private double bet;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_model_id")
    private GameModel gameModel;

    @NotNull
    @OneToOne
    @JoinColumn(name = "owner_id")
    private PlayerModel owner;

    @OneToMany(mappedBy = "currentRoom", cascade = CascadeType.ALL)
    private List<PlayerModel> players;

    private Boolean isGameStarted;
    private Boolean isGameFinished;
}
