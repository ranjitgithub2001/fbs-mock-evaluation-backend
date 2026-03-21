package com.fbs.mock_evaluation_system.mapper;

import com.fbs.mock_evaluation_system.dto.StudentResponseDTO;
import com.fbs.mock_evaluation_system.entity.Batch;
import com.fbs.mock_evaluation_system.entity.Student;

public class StudentMapper {
	public static StudentResponseDTO toDTO(Student student) {

		return new StudentResponseDTO(student.getId(), student.getFrn(), student.getName(), student.getRollNumber(),
				student.getBatch().getId(), student.getBatch().getBatchName());
	}

	public static Student toEntity(String frn, String name, String rollNumber, Batch batch) {
		Student student = new Student();
		student.setFrn(frn);
		student.setName(name);
		student.setRollNumber(rollNumber);
		student.setBatch(batch);

		return student;
	}

}
