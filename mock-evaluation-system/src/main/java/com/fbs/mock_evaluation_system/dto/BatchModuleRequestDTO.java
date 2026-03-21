package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.NotNull;

public class BatchModuleRequestDTO {

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Course Module ID is required")
    private Long courseModuleId;

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Long getCourseModuleId() {
		return courseModuleId;
	}

	public void setCourseModuleId(Long courseModuleId) {
		this.courseModuleId = courseModuleId;
	}

   
}
