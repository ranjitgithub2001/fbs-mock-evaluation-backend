package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class StudentRequestDTO {

	@NotBlank(message = "FRN cannot be empty")
	@Pattern(regexp = "^FRN-\\d{2}[A-Z]\\d{4}/\\d{3}$", message = "FRN must follow format: FRN-23J1225/001")
	private String frn;
	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotNull(message = "Batch ID cannot be null")
	private Long batchId;

	public StudentRequestDTO() {
	}

	public String getFrn() {
		return frn;
	}

	public void setFrn(String frn) {
		this.frn = frn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
}
