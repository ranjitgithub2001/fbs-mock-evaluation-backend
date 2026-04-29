package com.fbs.mock_evaluation_system.dto;

public class ModulePerformanceDTO {

    private Long courseModuleId;
    private String moduleName;

    private int totalEvaluations;
    private int cleared;
    private int notCleared;
    private int absent;
    private int rescheduled;

    // CLEARED / (CLEARED + NOT_CLEARED) * 100
    private double passRate;

    private Double avgTechnicalScore;
    private Double avgConfidenceScore;
    private Double avgCommunicationScore;

    public ModulePerformanceDTO() {
    }

    public Long getCourseModuleId() {
        return courseModuleId;
    }

    public void setCourseModuleId(Long courseModuleId) {
        this.courseModuleId = courseModuleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getTotalEvaluations() {
        return totalEvaluations;
    }

    public void setTotalEvaluations(int totalEvaluations) {
        this.totalEvaluations = totalEvaluations;
    }

    public int getCleared() {
        return cleared;
    }

    public void setCleared(int cleared) {
        this.cleared = cleared;
    }

    public int getNotCleared() {
        return notCleared;
    }

    public void setNotCleared(int notCleared) {
        this.notCleared = notCleared;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getRescheduled() {
        return rescheduled;
    }

    public void setRescheduled(int rescheduled) {
        this.rescheduled = rescheduled;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    public Double getAvgTechnicalScore() {
        return avgTechnicalScore;
    }

    public void setAvgTechnicalScore(Double avgTechnicalScore) {
        this.avgTechnicalScore = avgTechnicalScore;
    }

    public Double getAvgConfidenceScore() {
        return avgConfidenceScore;
    }

    public void setAvgConfidenceScore(Double avgConfidenceScore) {
        this.avgConfidenceScore = avgConfidenceScore;
    }

    public Double getAvgCommunicationScore() {
        return avgCommunicationScore;
    }

    public void setAvgCommunicationScore(Double avgCommunicationScore) {
        this.avgCommunicationScore = avgCommunicationScore;
    }
}