package com.fbs.mock_evaluation_system.dto;

import com.fbs.mock_evaluation_system.entity.EvaluationResult;

public class StudentModulePerformanceDTO {

    private Long batchModuleId;
    private String moduleName;

    private int totalAttempts;
    private EvaluationResult latestResult;

    private Integer latestTechnicalScore;
    private Integer latestConfidenceScore;
    private Integer latestCommunicationScore;

    public StudentModulePerformanceDTO() {
    }

    public Long getBatchModuleId() {
        return batchModuleId;
    }

    public void setBatchModuleId(Long batchModuleId) {
        this.batchModuleId = batchModuleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(int totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public EvaluationResult getLatestResult() {
        return latestResult;
    }

    public void setLatestResult(EvaluationResult latestResult) {
        this.latestResult = latestResult;
    }

    public Integer getLatestTechnicalScore() {
        return latestTechnicalScore;
    }

    public void setLatestTechnicalScore(Integer latestTechnicalScore) {
        this.latestTechnicalScore = latestTechnicalScore;
    }

    public Integer getLatestConfidenceScore() {
        return latestConfidenceScore;
    }

    public void setLatestConfidenceScore(Integer latestConfidenceScore) {
        this.latestConfidenceScore = latestConfidenceScore;
    }

    public Integer getLatestCommunicationScore() {
        return latestCommunicationScore;
    }

    public void setLatestCommunicationScore(Integer latestCommunicationScore) {
        this.latestCommunicationScore = latestCommunicationScore;
    }
}