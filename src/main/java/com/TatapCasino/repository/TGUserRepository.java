package com.TatapCasino.repository;

import com.TatapCasino.model.TGUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TGUserRepository extends JpaRepository<TGUserModel, Long> {
}
