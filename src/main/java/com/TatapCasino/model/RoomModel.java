package com.TatapCasino.model;

import jakarta.persistence.*;
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
    private String roomName;
    private int maxPlayers;
    private double bet;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PlayerModel owner;

    @ManyToMany
    @JoinTable(
            name = "room_players",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerModel> players;
}
