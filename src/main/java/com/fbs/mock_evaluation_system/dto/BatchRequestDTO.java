package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BatchRequestDTO {
	@NotBlank(message = "Batch name cannot be empty")
	private String batchName;

	@NotBlank(message = "FRN batch code cannot be empty")
	@Pattern(
	    regexp = "^[0-9]{2}[A-Z][0-9]{4}$",
	    message = "FRN batch code must follow format: 23J1225"
	)
	private String frnBatchCode;

	public BatchRequestDTO() {
	}

	

	public String getFrnBatchCode() {
		return frnBatchCode;
	}



	public void setFrnBatchCode(String frnBatchCode) {
		this.frnBatchCode = frnBatchCode;
	}



	public String getBatchName() {
		return batchName;
	}


	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
}
