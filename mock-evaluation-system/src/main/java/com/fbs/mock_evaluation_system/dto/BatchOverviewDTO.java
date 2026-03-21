package com.fbs.mock_evaluation_system.dto;

public class BatchOverviewDTO {

    private Long batchId;
    private String batchName;
    private String frnBatchCode;

    private int totalStudents;
    private int totalEvaluations;

    // Students who have at least one evaluation
    private int evaluatedStudents;

    // Students with zero evaluations
    private int pendingStudents;

    // CLEARED / (CLEARED + NOT_CLEARED) — excludes ABSENT and RESCHEDULED
    private double passRate;

    public BatchOverviewDTO() {
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getFrnBatchCode() {
        return frnBatchCode;
    }

    public void setFrnBatchCode(String frnBatchCode) {
        this.frnBatchCode = frnBatchCode;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalEvaluations() {
        return totalEvaluations;
    }

    public void setTotalEvaluations(int totalEvaluations) {
        this.totalEvaluations = totalEvaluations;
    }

    public int getEvaluatedStudents() {
        return evaluatedStudents;
    }

    public void setEvaluatedStudents(int evaluatedStudents) {
        this.evaluatedStudents = evaluatedStudents;
    }

    public int getPendingStudents() {
        return pendingStudents;
    }

    public void setPendingStudents(int pendingStudents) {
        this.pendingStudents = pendingStudents;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }
}