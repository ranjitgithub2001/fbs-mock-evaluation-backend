package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseModuleRequestDTO {

    @NotBlank(message = "Module name cannot be empty")
    @Size(max = 100, message = "Module name cannot exceed 100 characters")
    private String name;

    public CourseModuleRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}