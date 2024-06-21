package com.TatapCasino.repository;

import com.TatapCasino.model.ScoreModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<ScoreModel, Long> {
}
