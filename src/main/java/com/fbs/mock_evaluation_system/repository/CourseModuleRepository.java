package com.fbs.mock_evaluation_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fbs.mock_evaluation_system.entity.CourseModule;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

    boolean existsByName(String name);

    // For update — exclude current record
    boolean existsByNameAndIdNot(String name, Long id);
}