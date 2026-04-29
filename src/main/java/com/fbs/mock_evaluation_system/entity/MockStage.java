package com.fbs.mock_evaluation_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mock_stage")
public class MockStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "allow_multiple_attempts", nullable = false)
    private boolean allowMultipleAttempts;

    public MockStage() {
    }

    public MockStage(String name, boolean allowMultipleAttempts) {
        this.name = name;
        this.allowMultipleAttempts = allowMultipleAttempts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // FIX #11 — added missing setters
    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllowMultipleAttempts() {
        return allowMultipleAttempts;
    }

    public void setAllowMultipleAttempts(boolean allowMultipleAttempts) {
        this.allowMultipleAttempts = allowMultipleAttempts;
    }
}