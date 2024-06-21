package com.TatapCasino.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Entity
@Getter
@Setter
public class PlayerModel {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String name;

    private double balance;

    @OneToOne
    @JoinColumn(name = "tgUserAccount_id")
    private TGUserModel tgUserAccount;

    @Nullable
    @OneToOne(mappedBy = "owner")
    private RoomModel ownedRoom;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "current_room_id")
    private RoomModel currentRoom;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoreModel> scores;

    @OneToMany(mappedBy = "winner")
    private List<GameModel> wonGames;
}
