package com.fbs.mock_evaluation_system.entity;

import java.util.List;

import jakarta.persistence.*;

// FIX #7 — removed @JsonIgnore and its import
// Entities are never serialized directly — DTO + Mapper pattern handles all responses

@Entity
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String batchName;

    @Column(nullable = false, unique = true, length = 7)
    private String frnBatchCode;

    @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
    private List<Student> students;

    public Batch() {
    }

    public Batch(String batchName, String frnBatchCode) {
        this.batchName = batchName;
        this.frnBatchCode = frnBatchCode;
    }

    public Long getId() {
        return id;
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}