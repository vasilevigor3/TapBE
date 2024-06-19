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
    @OneToMany
    private List<PlayerModel> players;
    private int maxPlayers;
    private double bet;

}
