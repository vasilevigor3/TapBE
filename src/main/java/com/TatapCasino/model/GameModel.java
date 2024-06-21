package com.TatapCasino.model;

import com.TatapCasino.enums.GameType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GameModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private PlayerModel winner;

    @OneToOne(mappedBy = "gameModel", cascade = CascadeType.ALL)
    private RoomModel room;
}
