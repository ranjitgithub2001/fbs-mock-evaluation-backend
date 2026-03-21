package com.fbs.mock_evaluation_system.mapper;

import com.fbs.mock_evaluation_system.dto.BatchResponseDTO;
import com.fbs.mock_evaluation_system.entity.Batch;

public class BatchMapper {

    // FIX #4 — removed broken toEntity() method (dead code, missing frnBatchCode)

    public static BatchResponseDTO toDTO(Batch batch) {
        return new BatchResponseDTO(
                batch.getId(),
                batch.getBatchName(),
                batch.getFrnBatchCode());
    }
}