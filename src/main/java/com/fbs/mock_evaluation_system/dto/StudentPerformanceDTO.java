package com.fbs.mock_evaluation_system.dto;

import java.util.List;

public class StudentPerformanceDTO {

    private Long studentId;
    private String studentName;
    private String frn;
    private String batchName;

    private int totalModules;
    private int clearedModules;
    private int notClearedModules;
    private int pendingModules;

    // Per module breakdown
    private List<StudentModulePerformanceDTO> modulePerformance;

    public StudentPerformanceDTO() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFrn() {
        return frn;
    }

    public void setFrn(String frn) {
        this.frn = frn;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public int getTotalModules() {
        return totalModules;
    }

    public void setTotalModules(int totalModules) {
        this.totalModules = totalModules;
    }

    public int getClearedModules() {
        return clearedModules;
    }

    public void setClearedModules(int clearedModules) {
        this.clearedModules = clearedModules;
    }

    public int getNotClearedModules() {
        return notClearedModules;
    }

    public void setNotClearedModules(int notClearedModules) {
        this.notClearedModules = notClearedModules;
    }

    public int getPendingModules() {
        return pendingModules;
    }

    public void setPendingModules(int pendingModules) {
        this.pendingModules = pendingModules;
    }

    public List<StudentModulePerformanceDTO> getModulePerformance() {
        return modulePerformance;
    }

    public void setModulePerformance(List<StudentModulePerformanceDTO> modulePerformance) {
        this.modulePerformance = modulePerformance;
    }
}