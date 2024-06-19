package com.TatapCasino.repository;

import com.TatapCasino.model.PlayerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerModel, Long> {
}
