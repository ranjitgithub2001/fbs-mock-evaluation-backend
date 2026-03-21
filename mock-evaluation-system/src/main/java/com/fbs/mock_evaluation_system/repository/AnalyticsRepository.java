package com.fbs.mock_evaluation_system.repository;

import com.fbs.mock_evaluation_system.entity.EvaluationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fbs.mock_evaluation_system.entity.Evaluation;

import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Evaluation, Long> {

	// ─────────────────────────────────────────────────────────────
	// BATCH OVERVIEW
	// ─────────────────────────────────────────────────────────────

	// Total evaluations for a batch
	@Query("SELECT COUNT(e) FROM Evaluation e " + "WHERE e.student.batch.id = :batchId")
	long countEvaluationsByBatchId(@Param("batchId") Long batchId);

	// Count of distinct students who have at least one evaluation in a batch
	@Query("SELECT COUNT(DISTINCT e.student.id) FROM Evaluation e " + "WHERE e.student.batch.id = :batchId")
	long countEvaluatedStudentsByBatchId(@Param("batchId") Long batchId);

	// Count CLEARED evaluations for a batch
	@Query("SELECT COUNT(e) FROM Evaluation e " + "WHERE e.student.batch.id = :batchId "
			+ "AND e.finalResult = :result")
	long countByBatchIdAndResult(@Param("batchId") Long batchId, @Param("result") EvaluationResult result);

	// ─────────────────────────────────────────────────────────────
	// STUDENT PERFORMANCE
	// ─────────────────────────────────────────────────────────────

	// All evaluations for a student ordered by batchModule and attemptNumber
	@Query("SELECT e FROM Evaluation e " + "WHERE e.student.id = :studentId "
			+ "ORDER BY e.batchModule.id ASC, e.attemptNumber ASC NULLS LAST")
	List<Evaluation> findEvaluationsByStudentIdOrdered(@Param("studentId") Long studentId);

	// Latest evaluation per batchModule for a student
	// (highest attemptNumber, excluding RESCHEDULED)
	@Query("SELECT e FROM Evaluation e " + "WHERE e.student.id = :studentId " + "AND e.attemptNumber = ("
			+ "  SELECT MAX(e2.attemptNumber) FROM Evaluation e2 " + "  WHERE e2.student.id = :studentId "
			+ "  AND e2.batchModule.id = e.batchModule.id "
			+ "  AND e2.finalResult <> com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED" + ") "
			+ "AND e.finalResult <> com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED")
	List<Evaluation> findLatestEvaluationPerModuleForStudent(@Param("studentId") Long studentId);

	// Count attempts per batchModule for a student (excluding RESCHEDULED)
	@Query("SELECT e.batchModule.id, COUNT(e) FROM Evaluation e " + "WHERE e.student.id = :studentId "
			+ "AND e.finalResult <> com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED "
			+ "GROUP BY e.batchModule.id")
	List<Object[]> countAttemptsPerModuleForStudent(@Param("studentId") Long studentId);

	// ─────────────────────────────────────────────────────────────
	// MODULE PERFORMANCE
	// ─────────────────────────────────────────────────────────────

	@Query("SELECT " + "  e.batchModule.courseModule.id, " + "  e.batchModule.courseModule.name, " + "  COUNT(e), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.CLEARED THEN 1 ELSE 0 END), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.NOT_CLEARED THEN 1 ELSE 0 END), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.ABSENT THEN 1 ELSE 0 END), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED THEN 1 ELSE 0 END), "
			+ "  AVG(e.technicalScore), " + "  AVG(e.confidenceScore), " + "  AVG(e.communicationScore) "
			+ "FROM Evaluation e " + "GROUP BY e.batchModule.courseModule.id, e.batchModule.courseModule.name "
			+ "ORDER BY e.batchModule.courseModule.name ASC")
	List<Object[]> findModulePerformanceStats();

	// ─────────────────────────────────────────────────────────────
	// TRAINER PERFORMANCE
	// ─────────────────────────────────────────────────────────────

	@Query("SELECT " + "  e.trainer.id, " + "  e.trainer.fullName, " + "  e.trainer.email, " + "  COUNT(e), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.CLEARED THEN 1 ELSE 0 END), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.NOT_CLEARED THEN 1 ELSE 0 END), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.ABSENT THEN 1 ELSE 0 END), "
			+ "  SUM(CASE WHEN e.finalResult = com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED THEN 1 ELSE 0 END), "
			+ "  AVG(e.technicalScore), " + "  AVG(e.confidenceScore), " + "  AVG(e.communicationScore) "
			+ "FROM Evaluation e " + "GROUP BY e.trainer.id, e.trainer.fullName, e.trainer.email "
			+ "ORDER BY COUNT(e) DESC")
	List<Object[]> findTrainerPerformanceStats();

	// ─────────────────────────────────────────────────────────────
	// BATCH PROGRESS REPORT
	// ─────────────────────────────────────────────────────────────

	// Latest evaluation per batchModule per student in a batch
	@Query("SELECT e FROM Evaluation e " + "WHERE e.student.batch.id = :batchId " + "AND e.attemptNumber = ("
			+ "  SELECT MAX(e2.attemptNumber) FROM Evaluation e2 " + "  WHERE e2.student.id = e.student.id "
			+ "  AND e2.batchModule.id = e.batchModule.id "
			+ "  AND e2.finalResult <> com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED" + ") "
			+ "AND e.finalResult <> com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED "
			+ "ORDER BY e.student.id ASC, e.batchModule.id ASC")
	List<Evaluation> findLatestEvaluationsPerModulePerStudentInBatch(@Param("batchId") Long batchId);

	@Query("SELECT e.student.id, e.batchModule.id, COUNT(e) " + "FROM Evaluation e "
			+ "WHERE e.student.batch.id = :batchId "
			+ "AND e.finalResult <> com.fbs.mock_evaluation_system.entity.EvaluationResult.RESCHEDULED "
			+ "GROUP BY e.student.id, e.batchModule.id")
	List<Object[]> countAttemptsPerModulePerStudentInBatch(@Param("batchId") Long batchId);
}