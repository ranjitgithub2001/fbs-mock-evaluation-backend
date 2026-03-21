package com.fbs.mock_evaluation_system.mapper;

// FIX #6 — removed unused: import org.hibernate.annotations.Comment

import org.springframework.stereotype.Component;

import com.fbs.mock_evaluation_system.dto.BatchModuleResponseDTO;
import com.fbs.mock_evaluation_system.entity.BatchModule;

@Component
public class BatchModuleMapper {

    public BatchModuleResponseDTO toResponseDTO(BatchModule entity) {

        BatchModuleResponseDTO dto = new BatchModuleResponseDTO();

        dto.setId(entity.getId());

        dto.setBatchId(entity.getBatch().getId());
        dto.setBatchName(entity.getBatch().getBatchName());

        dto.setCourseModuleId(entity.getCourseModule().getId());
        dto.setCourseModuleName(entity.getCourseModule().getName());

        return dto;
    }
}