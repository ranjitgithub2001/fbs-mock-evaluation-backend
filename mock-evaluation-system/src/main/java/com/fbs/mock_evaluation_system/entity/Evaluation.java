package com.fbs.mock_evaluation_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_evaluation_student")
    )
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "batch_module_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_evaluation_batch_module")
    )
    private BatchModule batchModule;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "mock_stage_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_evaluation_mock_stage")
    )
    private MockStage mockStage;

   

    private Integer stageSequence;     
    private Integer attemptNumber;     

    // ===============================
    // Scores (Dynamic Range)
    // ===============================

    private Integer technicalScore;
    private Integer confidenceScore;
    private Integer communicationScore;

    // ===============================
    // Result
    // ===============================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationResult finalResult;

    // ===============================
    // Metadata
    // ===============================

    @Column(nullable = false)
    private LocalDate mockDate;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "trainer_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_evaluation_trainer")
    )
    private User trainer;

    @Column(nullable = false, length = 500)
    private String remark;

    public Evaluation() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public BatchModule getBatchModule() {
		return batchModule;
	}

	public void setBatchModule(BatchModule batchModule) {
		this.batchModule = batchModule;
	}

	public MockStage getMockStage() {
		return mockStage;
	}

	public void setMockStage(MockStage mockStage) {
		this.mockStage = mockStage;
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

	

	public User getTrainer() {
		return trainer;
	}

	public void setTrainer(User trainer) {
		this.trainer = trainer;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    

}