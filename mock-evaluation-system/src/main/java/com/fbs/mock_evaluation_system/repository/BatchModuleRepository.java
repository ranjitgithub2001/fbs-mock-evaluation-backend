package com.fbs.mock_evaluation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fbs.mock_evaluation_system.entity.BatchModule;

import java.util.List;

public interface BatchModuleRepository extends JpaRepository<BatchModule, Long> {

    boolean existsByBatchIdAndCourseModuleId(Long batchId, Long courseModuleId);

    // For get by batch
    List<BatchModule> findByBatchId(Long batchId);

    // For student performance analytics
    long countByBatchId(Long batchId);
}