package com.TatapCasino.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TGUserModel {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
}
