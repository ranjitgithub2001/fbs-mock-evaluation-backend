package com.fbs.mock_evaluation_system.dto;

public class BatchResponseDTO {

	private Long id;
	private String batchName;
	private String frnBatchCode;

	public BatchResponseDTO(Long id, String batchName, String frnBatchCode) {
		this.id = id;
		this.batchName = batchName;
		this.frnBatchCode = frnBatchCode;
	}

	public Long getId() {
		return id;
	}

	public String getBatchName() {
		return batchName;
	}

	public String getFrnBatchCode() {
		return frnBatchCode;
	}
}
