package com.TatapCasino.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TGUserModel {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String name;

    @OneToOne(mappedBy = "tgUserAccount", cascade = CascadeType.ALL)
    @JsonManagedReference
    private PlayerModel player;
}
