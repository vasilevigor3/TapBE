package com.TatapCasino.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ScoreModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private int score;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerModel player;
}
