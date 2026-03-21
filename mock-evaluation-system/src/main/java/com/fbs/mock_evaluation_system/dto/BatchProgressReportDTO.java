package com.fbs.mock_evaluation_system.dto;

import java.util.List;

public class BatchProgressReportDTO {

    private Long batchId;
    private String batchName;
    private String frnBatchCode;
    private int totalModules;
    private List<StudentProgressDTO> students;

    public BatchProgressReportDTO() {
    }

    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public String getFrnBatchCode() { return frnBatchCode; }
    public void setFrnBatchCode(String frnBatchCode) { this.frnBatchCode = frnBatchCode; }

    public int getTotalModules() { return totalModules; }
    public void setTotalModules(int totalModules) { this.totalModules = totalModules; }

    public List<StudentProgressDTO> getStudents() { return students; }
    public void setStudents(List<StudentProgressDTO> students) { this.students = students; }
}