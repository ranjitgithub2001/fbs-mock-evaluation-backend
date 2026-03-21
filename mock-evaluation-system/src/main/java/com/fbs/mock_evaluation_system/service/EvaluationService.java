package com.fbs.mock_evaluation_system.service;

import com.fbs.mock_evaluation_system.dto.EvaluationRequestDTO;
import com.fbs.mock_evaluation_system.dto.EvaluationResponseDTO;
import com.fbs.mock_evaluation_system.dto.EvaluationUpdateRequestDTO;
import com.fbs.mock_evaluation_system.entity.*;
import com.fbs.mock_evaluation_system.exception.*;
import com.fbs.mock_evaluation_system.mapper.EvaluationMapper;
import com.fbs.mock_evaluation_system.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fbs.mock_evaluation_system.dto.EvaluationFilterDTO;
import com.fbs.mock_evaluation_system.specification.EvaluationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
public class EvaluationService {

	private final StudentRepository studentRepository;
	private final BatchModuleRepository batchModuleRepository;
	private final MockStageRepository mockStageRepository;
	private final EvaluationRepository evaluationRepository;
	private final UserRepository userRepository; // FIX #1 — added final
	private final EvaluationMapper evaluationMapper; // FIX #1 — added final

	public EvaluationService(StudentRepository studentRepository, BatchModuleRepository batchModuleRepository,
			MockStageRepository mockStageRepository, EvaluationRepository evaluationRepository,
			UserRepository userRepository, EvaluationMapper evaluationMapper) {

		this.studentRepository = studentRepository;
		this.batchModuleRepository = batchModuleRepository;
		this.mockStageRepository = mockStageRepository;
		this.evaluationRepository = evaluationRepository;
		this.userRepository = userRepository;
		this.evaluationMapper = evaluationMapper;
	}

	@Transactional
	public EvaluationResponseDTO create(EvaluationRequestDTO request) {

		// STEP 1 – Fetch Entities
		Student student = studentRepository.findById(request.getStudentId())
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		BatchModule batchModule = batchModuleRepository.findById(request.getBatchModuleId())
				.orElseThrow(() -> new ResourceNotFoundException("BatchModule not found"));

		MockStage mockStage = mockStageRepository.findById(request.getMockStageId())
				.orElseThrow(() -> new ResourceNotFoundException("MockStage not found"));

		// STEP 2 – Batch Integrity Check
		if (!student.getBatch().getId().equals(batchModule.getBatch().getId())) {
			throw new InvalidInputException("Student does not belong to the batch of this module");
		}

		// STEP 3 – Stage Sequence Validation
		if (mockStage.getName().equals("PRACTICE")) {

			if (request.getStageSequence() == null || request.getStageSequence() <= 0) {
				throw new InvalidInputException("Stage sequence is required for PRACTICE stage");
			}

			boolean exists = evaluationRepository.existsByStudentIdAndBatchModuleIdAndMockStageIdAndStageSequence(
					student.getId(), batchModule.getId(), mockStage.getId(), request.getStageSequence());

			if (exists) {
				throw new DuplicateResourceException("Evaluation already exists for this practice sequence");
			}

		} else {

			if (request.getStageSequence() != null) {
				throw new InvalidInputException("Stage sequence should be null for non-practice stage");
			}
		}

		// STEP 4 – Result vs Score Validation
		EvaluationResult result = request.getFinalResult();

		boolean scoresProvided = request.getTechnicalScore() != null && request.getConfidenceScore() != null
				&& request.getCommunicationScore() != null;

		if (result == EvaluationResult.CLEARED || result == EvaluationResult.NOT_CLEARED) {

			if (!scoresProvided) {
				throw new InvalidInputException("Scores are required when result is CLEARED or NOT_CLEARED");
			}

		} else {

			if (request.getTechnicalScore() != null || request.getConfidenceScore() != null
					|| request.getCommunicationScore() != null) {

				throw new InvalidInputException("Scores must be null when result is ABSENT or RESCHEDULED");
			}
		}

		// STEP 5 – Attempt Logic
		Integer attemptNumber = null;

		if (result != EvaluationResult.RESCHEDULED) {

			long previousAttempts = evaluationRepository
					.countByStudentIdAndBatchModuleIdAndMockStageIdAndFinalResultNot(student.getId(),
							batchModule.getId(), mockStage.getId(), EvaluationResult.RESCHEDULED);

			attemptNumber = (int) previousAttempts + 1;
		}

		// STEP 5b – Trainer Validation
		User trainer = userRepository.findById(request.getTrainerId())
				.orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));

		if (trainer.getRole() != UserRole.TRAINER) {
			throw new InvalidInputException("User is not a trainer");
		}

		// FIX #9 — validate trainer is active
		if (!trainer.isActive()) {
			throw new InvalidInputException("Trainer is inactive and cannot conduct evaluations");
		}

		// STEP 5c – Mock Date Validation
		if (request.getMockDate().isAfter(LocalDate.now())) {
			throw new InvalidInputException("Mock date cannot be in the future");
		}
		if (request.getMockDate().isBefore(LocalDate.now().minusDays(30))) {
			throw new InvalidInputException("Mock date cannot be more than 30 days in the past");
		}

		// STEP 6 – Create Entity
		Evaluation evaluation = new Evaluation();

		evaluation.setStudent(student);
		evaluation.setBatchModule(batchModule);
		evaluation.setMockStage(mockStage);
		evaluation.setStageSequence(request.getStageSequence());
		evaluation.setAttemptNumber(attemptNumber);
		evaluation.setTechnicalScore(request.getTechnicalScore());
		evaluation.setConfidenceScore(request.getConfidenceScore());
		evaluation.setCommunicationScore(request.getCommunicationScore());
		evaluation.setFinalResult(result);
		evaluation.setMockDate(request.getMockDate());
		evaluation.setTrainer(trainer);
		evaluation.setRemark(request.getRemark());

		Evaluation saved = evaluationRepository.save(evaluation);

		return evaluationMapper.toResponseDTO(saved);
	}

	@Transactional(readOnly = true)
	public List<EvaluationResponseDTO> getByStudent(Long studentId) {

		if (!studentRepository.existsById(studentId)) {
			throw new ResourceNotFoundException("Student not found");
		}

		List<Evaluation> evaluations = evaluationRepository.findByStudentId(studentId);

		// FIX #2 — replaced lambda with explicit loop
		List<EvaluationResponseDTO> result = new ArrayList<>();
		for (Evaluation evaluation : evaluations) {
			result.add(evaluationMapper.toResponseDTO(evaluation));
		}
		return result;
	}

	@Transactional
	public EvaluationResponseDTO update(Long id, EvaluationUpdateRequestDTO request) {

		Evaluation evaluation = evaluationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Evaluation not found"));

		// 1 – Future Date Validation
		if (request.getMockDate().isAfter(LocalDate.now())) {
			throw new InvalidInputException("Mock date cannot be in the future");
		}
		if (request.getMockDate().isBefore(LocalDate.now().minusDays(30))) {
			throw new InvalidInputException("Mock date cannot be more than 30 days in the past");
		}

		// 2 – StageSequence Rules
		String stageName = evaluation.getMockStage().getName();

		if ("PRACTICE".equals(stageName)) {

			if (!Objects.equals(request.getStageSequence(), evaluation.getStageSequence())) {
				throw new InvalidInputException("Stage sequence cannot be modified for PRACTICE stage");
			}

		} else {

			if (!Objects.equals(request.getStageSequence(), evaluation.getStageSequence())) {

				if (request.getStageSequence() == null || request.getStageSequence() <= 0) {
					throw new InvalidInputException("Stage sequence must be greater than 0");
				}

				boolean exists = evaluationRepository
						.existsByStudentIdAndBatchModuleIdAndMockStageIdAndStageSequenceAndIdNot(
								evaluation.getStudent().getId(), evaluation.getBatchModule().getId(),
								evaluation.getMockStage().getId(), request.getStageSequence(), evaluation.getId());

				if (exists) {
					throw new DuplicateResourceException("Stage sequence already exists for this student and stage");
				}

				evaluation.setStageSequence(request.getStageSequence());
			}
		}

		// 3 – Result vs Score Validation
		EvaluationResult newResult = request.getFinalResult();

		boolean scoresProvided = request.getTechnicalScore() != null && request.getConfidenceScore() != null
				&& request.getCommunicationScore() != null;

		if (newResult == EvaluationResult.CLEARED || newResult == EvaluationResult.NOT_CLEARED) {

			if (!scoresProvided) {
				throw new InvalidInputException("Scores are required when result is CLEARED or NOT_CLEARED");
			}

		} else {

			if (request.getTechnicalScore() != null || request.getConfidenceScore() != null
					|| request.getCommunicationScore() != null) {

				throw new InvalidInputException("Scores must be null when result is ABSENT or RESCHEDULED");
			}
		}

		// 4 – Attempt Recalculation Logic
		if (newResult == EvaluationResult.RESCHEDULED) {

			evaluation.setAttemptNumber(null);

		} else {

			long previousAttempts = evaluationRepository
					.countByStudentIdAndBatchModuleIdAndMockStageIdAndFinalResultNotAndIdNot(
							evaluation.getStudent().getId(), evaluation.getBatchModule().getId(),
							evaluation.getMockStage().getId(), EvaluationResult.RESCHEDULED, evaluation.getId());

			evaluation.setAttemptNumber((int) previousAttempts + 1);
		}

		// 5 – Apply Editable Fields
		evaluation.setTechnicalScore(request.getTechnicalScore());
		evaluation.setConfidenceScore(request.getConfidenceScore());
		evaluation.setCommunicationScore(request.getCommunicationScore());
		evaluation.setFinalResult(newResult);
		evaluation.setMockDate(request.getMockDate());
		evaluation.setRemark(request.getRemark());

		Evaluation saved = evaluationRepository.save(evaluation);

		return evaluationMapper.toResponseDTO(saved);
	}

	@Transactional(readOnly = true)
	public List<EvaluationResponseDTO> getEvaluationsByBatchModule(Long batchModuleId) {

		batchModuleRepository.findById(batchModuleId)
				.orElseThrow(() -> new ResourceNotFoundException("BatchModule not found with id: " + batchModuleId));

		EvaluationFilterDTO filter = new EvaluationFilterDTO();
		filter.setBatchModuleId(batchModuleId);

		Specification<Evaluation> spec = EvaluationSpecification.withFilters(filter);

		List<Evaluation> evaluations = evaluationRepository.findAll(spec);

		List<EvaluationResponseDTO> result = new ArrayList<>();
		for (Evaluation evaluation : evaluations) {
			result.add(evaluationMapper.toResponseDTO(evaluation));
		}
		return result;
	}

	@Transactional(readOnly = true)
	public Page<EvaluationResponseDTO> getEvaluations(EvaluationFilterDTO filter, Pageable pageable) {

		// Validate date range
		if (filter.getMockDateFrom() != null && filter.getMockDateTo() != null) {
			if (filter.getMockDateFrom().isAfter(filter.getMockDateTo())) {
				throw new InvalidInputException("mockDateFrom cannot be after mockDateTo");
			}
		}

		if (filter.getStudentId() != null) {
			studentRepository.findById(filter.getStudentId()).orElseThrow(
					() -> new ResourceNotFoundException("Student not found with id: " + filter.getStudentId()));
		}

		if (filter.getTrainerId() != null) {
			userRepository.findById(filter.getTrainerId()).orElseThrow(
					() -> new ResourceNotFoundException("Trainer not found with id: " + filter.getTrainerId()));
		}

		if (filter.getBatchModuleId() != null) {
			batchModuleRepository.findById(filter.getBatchModuleId()).orElseThrow(
					() -> new ResourceNotFoundException("BatchModule not found with id: " + filter.getBatchModuleId()));
		}

		if (filter.getMockStageId() != null) {
			mockStageRepository.findById(filter.getMockStageId()).orElseThrow(
					() -> new ResourceNotFoundException("MockStage not found with id: " + filter.getMockStageId()));
		}

		Specification<Evaluation> spec = EvaluationSpecification.withFilters(filter);

		Page<Evaluation> evaluationPage = evaluationRepository.findAll(spec, pageable);

		// FIX #3 — method reference instead of lambda
		return evaluationPage.map(evaluationMapper::toResponseDTO);
	}

	@Transactional
	public void delete(Long id) {

		Evaluation evaluation = evaluationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + id));

		evaluationRepository.delete(evaluation);
	}
}