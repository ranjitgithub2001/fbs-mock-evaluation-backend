package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.fbs.mock_evaluation_system.entity.EvaluationResult;

public class EvaluationUpdateRequestDTO {

    private Integer stageSequence;

    @PositiveOrZero(message = "Technical score cannot be negative")
    private Integer technicalScore;

    @PositiveOrZero(message = "Confidence score cannot be negative")
    private Integer confidenceScore;

    @PositiveOrZero(message = "Communication score cannot be negative")
    private Integer communicationScore;

    @NotNull(message = "Final result is required")
    private EvaluationResult finalResult;

    @NotNull(message = "Mock date is required")
    private LocalDate mockDate;

    @NotBlank(message = "Remark is mandatory")
    private String remark;

    public EvaluationUpdateRequestDTO() {
    }

	public Integer getStageSequence() {
		return stageSequence;
	}

	public void setStageSequence(Integer stageSequence) {
		this.stageSequence = stageSequence;
	}

	public Integer getTechnicalScore() {
		return technicalScore;
	}

	public void setTechnicalScore(Integer technicalScore) {
		this.technicalScore = technicalScore;
	}

	public Integer getConfidenceScore() {
		return confidenceScore;
	}

	public void setConfidenceScore(Integer confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	public Integer getCommunicationScore() {
		return communicationScore;
	}

	public void setCommunicationScore(Integer communicationScore) {
		this.communicationScore = communicationScore;
	}

	public EvaluationResult getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(EvaluationResult finalResult) {
		this.finalResult = finalResult;
	}

	public LocalDate getMockDate() {
		return mockDate;
	}

	public void setMockDate(LocalDate mockDate) {
		this.mockDate = mockDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    
}