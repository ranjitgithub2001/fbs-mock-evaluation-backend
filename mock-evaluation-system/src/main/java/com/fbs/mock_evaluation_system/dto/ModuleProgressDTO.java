package com.fbs.mock_evaluation_system.dto;

import com.fbs.mock_evaluation_system.entity.EvaluationResult;

public class ModuleProgressDTO {

    private Long batchModuleId;
    private String moduleName;
    private EvaluationResult latestResult;  // null = pending
    private int totalAttempts;

    public ModuleProgressDTO() {
    }

    public Long getBatchModuleId() { return batchModuleId; }
    public void setBatchModuleId(Long batchModuleId) { this.batchModuleId = batchModuleId; }

    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }

    public EvaluationResult getLatestResult() { return latestResult; }
    public void setLatestResult(EvaluationResult latestResult) { this.latestResult = latestResult; }

    public int getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }
}