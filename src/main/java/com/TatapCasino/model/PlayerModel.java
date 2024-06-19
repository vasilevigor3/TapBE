package com.TatapCasino.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
//todo CHECK RELATIONS WITH ROOMS
public class PlayerModel {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private double balance;

    @OneToOne
    private TGUserModel tgUserAccount;

    @OneToMany(mappedBy = "owner")
    private List<RoomModel> ownedRooms;

    @ManyToMany(mappedBy = "players")
    private List<RoomModel> rooms;
}
