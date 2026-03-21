package com.fbs.mock_evaluation_system.service;

import com.fbs.mock_evaluation_system.dto.BatchOverviewDTO;
import com.fbs.mock_evaluation_system.dto.BatchProgressReportDTO;
import com.fbs.mock_evaluation_system.dto.ModulePerformanceDTO;
import com.fbs.mock_evaluation_system.dto.ModuleProgressDTO;
import com.fbs.mock_evaluation_system.dto.StudentModulePerformanceDTO;
import com.fbs.mock_evaluation_system.dto.StudentPerformanceDTO;
import com.fbs.mock_evaluation_system.dto.StudentProgressDTO;
import com.fbs.mock_evaluation_system.dto.TrainerPerformanceDTO;
import com.fbs.mock_evaluation_system.entity.Batch;
import com.fbs.mock_evaluation_system.entity.BatchModule;
import com.fbs.mock_evaluation_system.entity.Evaluation;
import com.fbs.mock_evaluation_system.entity.EvaluationResult;
import com.fbs.mock_evaluation_system.entity.Student;
import com.fbs.mock_evaluation_system.exception.ResourceNotFoundException;
import com.fbs.mock_evaluation_system.repository.AnalyticsRepository;
import com.fbs.mock_evaluation_system.repository.BatchModuleRepository;
import com.fbs.mock_evaluation_system.repository.BatchRepository;
import com.fbs.mock_evaluation_system.repository.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

	private final AnalyticsRepository analyticsRepository;
	private final BatchRepository batchRepository;
	private final StudentRepository studentRepository;
	private final BatchModuleRepository batchModuleRepository;

	public AnalyticsService(AnalyticsRepository analyticsRepository, BatchRepository batchRepository,
			StudentRepository studentRepository, BatchModuleRepository batchModuleRepository) {
		this.analyticsRepository = analyticsRepository;
		this.batchRepository = batchRepository;
		this.studentRepository = studentRepository;
		this.batchModuleRepository = batchModuleRepository;
	}

	// ─────────────────────────────────────────────────────────────
	// 1. BATCH OVERVIEW
	// ─────────────────────────────────────────────────────────────

	@Transactional(readOnly = true)
	public BatchOverviewDTO getBatchOverview(Long batchId) {

		Batch batch = batchRepository.findById(batchId)
				.orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + batchId));

		// Total students in batch
		long totalStudents = studentRepository.countByBatchId(batchId);

		// Total evaluations
		long totalEvaluations = analyticsRepository.countEvaluationsByBatchId(batchId);

		// Students who have at least one evaluation
		long evaluatedStudents = analyticsRepository.countEvaluatedStudentsByBatchId(batchId);

		// Students with no evaluations yet
		long pendingStudents = totalStudents - evaluatedStudents;

		// CLEARED and NOT_CLEARED counts for pass rate
		long cleared = analyticsRepository.countByBatchIdAndResult(batchId, EvaluationResult.CLEARED);

		long notCleared = analyticsRepository.countByBatchIdAndResult(batchId, EvaluationResult.NOT_CLEARED);

		double passRate = 0.0;
		long decisive = cleared + notCleared;
		if (decisive > 0) {
			passRate = Math.round(((double) cleared / decisive) * 100.0 * 10.0) / 10.0;
		}

		BatchOverviewDTO dto = new BatchOverviewDTO();
		dto.setBatchId(batch.getId());
		dto.setBatchName(batch.getBatchName());
		dto.setFrnBatchCode(batch.getFrnBatchCode());
		dto.setTotalStudents((int) totalStudents);
		dto.setTotalEvaluations((int) totalEvaluations);
		dto.setEvaluatedStudents((int) evaluatedStudents);
		dto.setPendingStudents((int) pendingStudents);
		dto.setPassRate(passRate);

		return dto;
	}

	// ─────────────────────────────────────────────────────────────
	// 2. STUDENT PERFORMANCE
	// ─────────────────────────────────────────────────────────────

	@Transactional(readOnly = true)
	public StudentPerformanceDTO getStudentPerformance(Long studentId) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

		// Total modules assigned to this student's batch
		long totalModules = batchModuleRepository.countByBatchId(student.getBatch().getId());

		// Latest evaluation per module
		List<Evaluation> latestEvaluations = analyticsRepository.findLatestEvaluationPerModuleForStudent(studentId);

		// Attempt counts per batchModule
		List<Object[]> attemptRows = analyticsRepository.countAttemptsPerModuleForStudent(studentId);

		Map<Long, Long> attemptMap = new HashMap<>();
		for (Object[] row : attemptRows) {
			Long batchModuleId = (Long) row[0];
			Long count = (Long) row[1];
			attemptMap.put(batchModuleId, count);
		}

		// Build per-module performance
		List<StudentModulePerformanceDTO> modulePerformance = new ArrayList<>();
		int clearedCount = 0;
		int notClearedCount = 0;

		for (Evaluation eval : latestEvaluations) {

			StudentModulePerformanceDTO moduleDTO = new StudentModulePerformanceDTO();

			moduleDTO.setBatchModuleId(eval.getBatchModule().getId());
			moduleDTO.setModuleName(eval.getBatchModule().getCourseModule().getName());
			moduleDTO.setLatestResult(eval.getFinalResult());
			moduleDTO.setLatestTechnicalScore(eval.getTechnicalScore());
			moduleDTO.setLatestConfidenceScore(eval.getConfidenceScore());
			moduleDTO.setLatestCommunicationScore(eval.getCommunicationScore());

			Long attempts = attemptMap.get(eval.getBatchModule().getId());
			moduleDTO.setTotalAttempts(attempts != null ? attempts.intValue() : 0);

			if (eval.getFinalResult() == EvaluationResult.CLEARED) {
				clearedCount++;
			} else if (eval.getFinalResult() == EvaluationResult.NOT_CLEARED) {
				notClearedCount++;
			}

			modulePerformance.add(moduleDTO);
		}

		int pendingModules = (int) totalModules - modulePerformance.size();

		StudentPerformanceDTO dto = new StudentPerformanceDTO();
		dto.setStudentId(student.getId());
		dto.setStudentName(student.getName());
		dto.setFrn(student.getFrn());
		dto.setBatchName(student.getBatch().getBatchName());
		dto.setTotalModules((int) totalModules);
		dto.setClearedModules(clearedCount);
		dto.setNotClearedModules(notClearedCount);
		dto.setPendingModules(Math.max(pendingModules, 0));
		dto.setModulePerformance(modulePerformance);

		return dto;
	}

	// ─────────────────────────────────────────────────────────────
	// 3. MODULE PERFORMANCE
	// ─────────────────────────────────────────────────────────────

	@Transactional(readOnly = true)
	public List<ModulePerformanceDTO> getModulePerformance() {

		List<Object[]> rows = analyticsRepository.findModulePerformanceStats();

		List<ModulePerformanceDTO> result = new ArrayList<>();

		for (Object[] row : rows) {

			Long courseModuleId = (Long) row[0];
			String moduleName = (String) row[1];
			long total = (Long) row[2];
			long cleared = (Long) row[3];
			long notCleared = (Long) row[4];
			long absent = (Long) row[5];
			long rescheduled = (Long) row[6];
			Double avgTech = (Double) row[7];
			Double avgConf = (Double) row[8];
			Double avgComm = (Double) row[9];

			double passRate = 0.0;
			long decisive = cleared + notCleared;
			if (decisive > 0) {
				passRate = Math.round(((double) cleared / decisive) * 100.0 * 10.0) / 10.0;
			}

			ModulePerformanceDTO dto = new ModulePerformanceDTO();
			dto.setCourseModuleId(courseModuleId);
			dto.setModuleName(moduleName);
			dto.setTotalEvaluations((int) total);
			dto.setCleared((int) cleared);
			dto.setNotCleared((int) notCleared);
			dto.setAbsent((int) absent);
			dto.setRescheduled((int) rescheduled);
			dto.setPassRate(passRate);
			dto.setAvgTechnicalScore(avgTech != null ? Math.round(avgTech * 10.0) / 10.0 : null);
			dto.setAvgConfidenceScore(avgConf != null ? Math.round(avgConf * 10.0) / 10.0 : null);
			dto.setAvgCommunicationScore(avgComm != null ? Math.round(avgComm * 10.0) / 10.0 : null);

			result.add(dto);
		}

		// Sort by pass rate ascending — hardest modules first
		result.sort((a, b) -> Double.compare(a.getPassRate(), b.getPassRate()));

		return result;
	}

	// ─────────────────────────────────────────────────────────────
	// 4. TRAINER PERFORMANCE
	// ─────────────────────────────────────────────────────────────

	@Transactional(readOnly = true)
	public List<TrainerPerformanceDTO> getTrainerPerformance() {

		List<Object[]> rows = analyticsRepository.findTrainerPerformanceStats();

		List<TrainerPerformanceDTO> result = new ArrayList<>();

		for (Object[] row : rows) {

			Long trainerId = (Long) row[0];
			String name = (String) row[1];
			String email = (String) row[2];
			long total = (Long) row[3];
			long cleared = (Long) row[4];
			long notCleared = (Long) row[5];
			long absent = (Long) row[6];
			long rescheduled = (Long) row[7];
			Double avgTech = (Double) row[8];
			Double avgConf = (Double) row[9];
			Double avgComm = (Double) row[10];

			double passRate = 0.0;
			long decisive = cleared + notCleared;
			if (decisive > 0) {
				passRate = Math.round(((double) cleared / decisive) * 100.0 * 10.0) / 10.0;
			}

			TrainerPerformanceDTO dto = new TrainerPerformanceDTO();
			dto.setTrainerId(trainerId);
			dto.setTrainerName(name);
			dto.setEmail(email);
			dto.setTotalEvaluationsCount((int) total);
			dto.setCleared((int) cleared);
			dto.setNotCleared((int) notCleared);
			dto.setAbsent((int) absent);
			dto.setRescheduled((int) rescheduled);
			dto.setPassRate(passRate);
			dto.setAvgTechnicalScore(avgTech != null ? Math.round(avgTech * 10.0) / 10.0 : null);
			dto.setAvgConfidenceScore(avgConf != null ? Math.round(avgConf * 10.0) / 10.0 : null);
			dto.setAvgCommunicationScore(avgComm != null ? Math.round(avgComm * 10.0) / 10.0 : null);

			result.add(dto);
		}

		return result;
	}

	// ─────────────────────────────────────────────────────────────
	// 5. BATCH PROGRESS REPORT
	// ─────────────────────────────────────────────────────────────

	@Transactional(readOnly = true)
	public BatchProgressReportDTO getBatchProgressReport(Long batchId) {

		Batch batch = batchRepository.findById(batchId)
				.orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + batchId));

		// All modules assigned to this batch
		List<BatchModule> batchModules = batchModuleRepository.findByBatchId(batchId);
		int totalModules = batchModules.size();

		// All students in this batch
		List<Student> students = studentRepository.findByBatchId(batchId);

		// Latest evaluations per student per module in this batch
		List<Evaluation> latestEvaluations = analyticsRepository
				.findLatestEvaluationsPerModulePerStudentInBatch(batchId);

		// Attempt counts per student per batchModule
		// Map key: studentId + "_" + batchModuleId
		// Attempt counts per student per batchModule
		Map<String, Integer> attemptMap = new HashMap<>();
		List<Object[]> attemptRows = analyticsRepository
		        .countAttemptsPerModulePerStudentInBatch(batchId);
		for (Object[] row : attemptRows) {
		    Long studentId = (Long) row[0];
		    Long batchModuleId = (Long) row[1];
		    Long count = (Long) row[2];
		    String key = studentId + "_" + batchModuleId;
		    attemptMap.put(key, count.intValue());
		}

		// Group latest evaluations by studentId
		Map<Long, List<Evaluation>> evalsByStudent = new LinkedHashMap<>();
		for (Evaluation eval : latestEvaluations) {
			Long studentId = eval.getStudent().getId();
			if (!evalsByStudent.containsKey(studentId)) {
				evalsByStudent.put(studentId, new ArrayList<>());
			}
			evalsByStudent.get(studentId).add(eval);
		}

		// Build per-student progress
		List<StudentProgressDTO> studentProgressList = new ArrayList<>();

		for (Student student : students) {

			List<Evaluation> studentEvals = evalsByStudent.getOrDefault(student.getId(), new ArrayList<>());

			// Map of batchModuleId -> latest evaluation for quick lookup
			Map<Long, Evaluation> evalByModule = new HashMap<>();
			for (Evaluation eval : studentEvals) {
				evalByModule.put(eval.getBatchModule().getId(), eval);
			}

			// Build module breakdown
			List<ModuleProgressDTO> moduleBreakdown = new ArrayList<>();
			int clearedCount = 0;

			for (BatchModule batchModule : batchModules) {

				ModuleProgressDTO moduleDTO = new ModuleProgressDTO();
				moduleDTO.setBatchModuleId(batchModule.getId());
				moduleDTO.setModuleName(batchModule.getCourseModule().getName());

				Evaluation eval = evalByModule.get(batchModule.getId());

				if (eval != null) {
					moduleDTO.setLatestResult(eval.getFinalResult());
					String key = student.getId() + "_" + batchModule.getId();
					moduleDTO.setTotalAttempts(attemptMap.getOrDefault(key, 0));
					if (eval.getFinalResult() == EvaluationResult.CLEARED) {
						clearedCount++;
					}
				} else {
					moduleDTO.setLatestResult(null); // pending
					moduleDTO.setTotalAttempts(0);
				}

				moduleBreakdown.add(moduleDTO);
			}

			double progressPercentage = totalModules > 0
					? Math.round(((double) clearedCount / totalModules) * 100.0 * 10.0) / 10.0
					: 0.0;

			StudentProgressDTO studentDTO = new StudentProgressDTO();
			studentDTO.setStudentId(student.getId());
			studentDTO.setStudentName(student.getName());
			studentDTO.setFrn(student.getFrn());
			studentDTO.setClearedModules(clearedCount);
			studentDTO.setTotalModules(totalModules);
			studentDTO.setProgressPercentage(progressPercentage);
			studentDTO.setModuleBreakdown(moduleBreakdown);

			studentProgressList.add(studentDTO);
		}

		BatchProgressReportDTO report = new BatchProgressReportDTO();
		report.setBatchId(batch.getId());
		report.setBatchName(batch.getBatchName());
		report.setFrnBatchCode(batch.getFrnBatchCode());
		report.setTotalModules(totalModules);
		report.setStudents(studentProgressList);

		return report;
	}

}