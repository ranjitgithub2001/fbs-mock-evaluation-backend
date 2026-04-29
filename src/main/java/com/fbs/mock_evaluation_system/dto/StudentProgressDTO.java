package com.fbs.mock_evaluation_system.dto;

import java.util.List;

public class StudentProgressDTO {

    private Long studentId;
    private String studentName;
    private String frn;
    private int clearedModules;
    private int totalModules;
    private double progressPercentage;
    private List<ModuleProgressDTO> moduleBreakdown;

    public StudentProgressDTO() {
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getFrn() { return frn; }
    public void setFrn(String frn) { this.frn = frn; }

    public int getClearedModules() { return clearedModules; }
    public void setClearedModules(int clearedModules) { this.clearedModules = clearedModules; }

    public int getTotalModules() { return totalModules; }
    public void setTotalModules(int totalModules) { this.totalModules = totalModules; }

    public double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }

    public List<ModuleProgressDTO> getModuleBreakdown() { return moduleBreakdown; }
    public void setModuleBreakdown(List<ModuleProgressDTO> moduleBreakdown) { this.moduleBreakdown = moduleBreakdown; }
}