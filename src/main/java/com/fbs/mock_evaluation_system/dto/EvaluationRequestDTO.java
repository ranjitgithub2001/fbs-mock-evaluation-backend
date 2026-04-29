package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import com.fbs.mock_evaluation_system.entity.EvaluationResult;

public class EvaluationRequestDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "BatchModule ID is required")
    private Long batchModuleId;

    @NotNull(message = "MockStage ID is required")
    private Long mockStageId;

    // FIX #13 — added @Min(1) for DTO-level defense
    @Min(value = 1, message = "Stage sequence must be at least 1")
    private Integer stageSequence;

    @Min(value = 0, message = "Technical score must be between 0 and 5")
    @Max(value = 5, message = "Technical score must be between 0 and 5")
    private Integer technicalScore;

    @Min(value = 0, message = "Confidence score must be between 0 and 5")
    @Max(value = 5, message = "Confidence score must be between 0 and 5")
    private Integer confidenceScore;

    @Min(value = 0, message = "Communication score must be between 0 and 5")
    @Max(value = 5, message = "Communication score must be between 0 and 5")
    private Integer communicationScore;

    @NotNull(message = "Final result is required")
    private EvaluationResult finalResult;

    @NotNull(message = "Mock date is required")
    private LocalDate mockDate;

    @NotBlank(message = "Remark is mandatory")
    private String remark;

    @NotNull(message = "Trainer ID is required")
    private Long trainerId;

    public EvaluationRequestDTO() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getBatchModuleId() {
        return batchModuleId;
    }

    public void setBatchModuleId(Long batchModuleId) {
        this.batchModuleId = batchModuleId;
    }

    public Long getMockStageId() {
        return mockStageId;
    }

    public void setMockStageId(Long mockStageId) {
        this.mockStageId = mockStageId;
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

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }
}