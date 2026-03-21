package com.fbs.mock_evaluation_system.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationErrorResponse {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private List<String> errors;

	public ValidationErrorResponse(LocalDateTime timestamp, int status, String error, List<String> errors) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.errors = errors;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public List<String> getErrors() {
		return errors;
	} 
}
