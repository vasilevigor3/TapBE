package com.TatapCasino.repository;

import com.TatapCasino.model.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameModel, Long> {
}
