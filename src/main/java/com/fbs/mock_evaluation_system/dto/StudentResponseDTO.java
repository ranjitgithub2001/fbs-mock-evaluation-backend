package com.fbs.mock_evaluation_system.dto;

public class StudentResponseDTO {

    private Long id;
    private String frn;
    private String name;
    private String rollNumber;
    private Long batchId;
    private String batchName;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(Long id,
                              String frn,
                              String name,
                              String rollNumber,
                              Long batchId,
                              String batchName) {

        this.id = id;
        this.frn = frn;
        this.name = name;
        this.rollNumber = rollNumber;
        this.batchId = batchId;
        this.batchName = batchName;
    }

    public Long getId() {
        return id;
    }

    public String getFrn() {
        return frn;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public Long getBatchId() {
        return batchId;
    }

    public String getBatchName() {
        return batchName;
    }
}