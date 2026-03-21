package com.fbs.mock_evaluation_system.dto;

import java.time.LocalDate;

import com.fbs.mock_evaluation_system.entity.EvaluationResult;

public class EvaluationResponseDTO {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long batchModuleId;
    private String moduleName;

    private String mockStageName;
    private Integer stageSequence;
    private Integer attemptNumber;

    private Integer technicalScore;
    private Integer confidenceScore;
    private Integer communicationScore;

    private EvaluationResult finalResult;

    private LocalDate mockDate;

    private Long trainerId;
    private String trainerName;

    private String remark;
    private String studentFrn;

    public String getStudentFrn() {
        return studentFrn;
    }

    public void setStudentFrn(String studentFrn) {
        this.studentFrn = studentFrn;
    }

    public EvaluationResponseDTO() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMockStageName() {
		return mockStageName;
	}

	public void setMockStageName(String mockStageName) {
		this.mockStageName = mockStageName;
	}

	public Integer getStageSequence() {
		return stageSequence;
	}

	public void setStageSequence(Integer stageSequence) {
		this.stageSequence = stageSequence;
	}

	public Integer getAttemptNumber() {
		return attemptNumber;
	}

	public void setAttemptNumber(Integer attemptNumber) {
		this.attemptNumber = attemptNumber;
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

	public Long getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(Long trainerId) {
		this.trainerId = trainerId;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    
}