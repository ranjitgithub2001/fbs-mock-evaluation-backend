package com.fbs.mock_evaluation_system.dto;

import com.fbs.mock_evaluation_system.entity.EvaluationResult;
import java.time.LocalDate;

public class EvaluationFilterDTO {

    private Long studentId;
    private Long batchModuleId;
    private Long trainerId;
    private Long mockStageId;
    private EvaluationResult finalResult;
    private LocalDate mockDateFrom;
    private LocalDate mockDateTo;

    public EvaluationFilterDTO() {}

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

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public Long getMockStageId() {
        return mockStageId;
    }

    public void setMockStageId(Long mockStageId) {
        this.mockStageId = mockStageId;
    }

    public EvaluationResult getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(EvaluationResult finalResult) {
        this.finalResult = finalResult;
    }

    public LocalDate getMockDateFrom() {
        return mockDateFrom;
    }

    public void setMockDateFrom(LocalDate mockDateFrom) {
        this.mockDateFrom = mockDateFrom;
    }

    public LocalDate getMockDateTo() {
        return mockDateTo;
    }

    public void setMockDateTo(LocalDate mockDateTo) {
        this.mockDateTo = mockDateTo;
    }
}