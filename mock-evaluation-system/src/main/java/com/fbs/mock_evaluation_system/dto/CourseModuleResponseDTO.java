package com.fbs.mock_evaluation_system.dto;

public class CourseModuleResponseDTO {

    private Long id;
    private String name;

    public CourseModuleResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}