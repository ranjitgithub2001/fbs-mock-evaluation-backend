package com.fbs.mock_evaluation_system.mapper;

import com.fbs.mock_evaluation_system.dto.CourseModuleResponseDTO;

import com.fbs.mock_evaluation_system.entity.CourseModule;

public class CourseModuleMapper {

    public static CourseModule toEntity(String normalizedName) {
        return new CourseModule(normalizedName);
    }

    public static CourseModuleResponseDTO toDTO(CourseModule module) {
        return new CourseModuleResponseDTO(
                module.getId(),
                module.getName()
        );
    }
}