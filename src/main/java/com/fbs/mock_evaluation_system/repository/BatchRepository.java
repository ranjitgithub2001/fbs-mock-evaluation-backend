package com.fbs.mock_evaluation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fbs.mock_evaluation_system.entity.Batch;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    boolean existsByBatchName(String batchName);

    boolean existsByFrnBatchCode(String frnBatchCode);

    // For update — exclude current record
    boolean existsByBatchNameAndIdNot(String batchName, Long id);

    boolean existsByFrnBatchCodeAndIdNot(String frnBatchCode, Long id);
}