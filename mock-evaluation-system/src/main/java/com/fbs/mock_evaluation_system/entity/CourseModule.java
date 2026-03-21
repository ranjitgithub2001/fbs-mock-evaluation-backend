package com.fbs.mock_evaluation_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "modules", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class CourseModule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	// Required by JPA
	public CourseModule() {
	}

	public CourseModule(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}