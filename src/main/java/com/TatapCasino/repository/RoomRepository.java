package com.TatapCasino.repository;

import com.TatapCasino.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomModel, Long> {
}
