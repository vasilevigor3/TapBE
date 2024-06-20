package com.TatapCasino.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @OneToOne
    private GameModel gameModel;

    @NotNull
    @OneToOne
    @JoinColumn(name = "owner_id")
    private PlayerModel owner;

    @NotNull
    @OneToMany(mappedBy = "currentRoom")
    private List<PlayerModel> players;
}
