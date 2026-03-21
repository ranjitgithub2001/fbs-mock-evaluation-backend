package com.fbs.mock_evaluation_system.dto;

public class BatchModuleResponseDTO {

	private Long id;
	private Long batchId;
	private String batchName;

	private Long courseModuleId;
	private String courseModuleName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public Long getCourseModuleId() {
		return courseModuleId;
	}
	public void setCourseModuleId(Long courseModuleId) {
		this.courseModuleId = courseModuleId;
	}
	public String getCourseModuleName() {
		return courseModuleName;
	}
	public void setCourseModuleName(String courseModuleName) {
		this.courseModuleName = courseModuleName;
	}
	
	
	

}
