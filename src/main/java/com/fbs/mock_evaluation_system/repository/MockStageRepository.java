package com.fbs.mock_evaluation_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fbs.mock_evaluation_system.entity.MockStage;

public interface MockStageRepository extends JpaRepository<MockStage, Long>{
	Optional<MockStage> findByName(String name);

}
