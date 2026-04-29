package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.NotNull;

public class BatchModuleRequestDTO {

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Course Module ID is required")
    private Long courseModuleId;
    
    @NotNull(message = "Practice count is required")
    private Integer practiceCount = 2;

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
	public Integer getPracticeCount() {
	    return practiceCount;
	}

	public void setPracticeCount(Integer practiceCount) {
	    this.practiceCount = practiceCount;
	}
   
}
