package com.fbs.mock_evaluation_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fbs.mock_evaluation_system.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	
	  Optional<Student> findByFrn(String frn);
	  
	  Page<Student> findByBatchId(Long batchId, Pageable pageable);
	  
	  long countByBatchId(Long batchId);
	  
	  List<Student> findByBatchId(Long batchId);
	  
}
