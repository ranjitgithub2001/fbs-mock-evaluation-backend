package com.fbs.mock_evaluation_system.mapper;

import org.springframework.stereotype.Component;

import com.fbs.mock_evaluation_system.dto.EvaluationResponseDTO;
import com.fbs.mock_evaluation_system.entity.Evaluation;

@Component
public class EvaluationMapper {

	public EvaluationResponseDTO toResponseDTO(Evaluation entity) {

		EvaluationResponseDTO dto = new EvaluationResponseDTO();

		dto.setId(entity.getId());

		dto.setStudentId(entity.getStudent().getId());
		dto.setStudentName(entity.getStudent().getName());
		dto.setStudentFrn(entity.getStudent().getFrn());

		dto.setBatchModuleId(entity.getBatchModule().getId());
		dto.setModuleName(entity.getBatchModule().getCourseModule().getName());

		dto.setMockStageName(entity.getMockStage().getName());
		dto.setStageSequence(entity.getStageSequence());
		dto.setAttemptNumber(entity.getAttemptNumber());

		dto.setTechnicalScore(entity.getTechnicalScore());
		dto.setConfidenceScore(entity.getConfidenceScore());
		dto.setCommunicationScore(entity.getCommunicationScore());

		dto.setFinalResult(entity.getFinalResult());
		dto.setMockDate(entity.getMockDate());

		dto.setTrainerId(entity.getTrainer().getId());
		dto.setTrainerName(entity.getTrainer().getFullName());

		dto.setRemark(entity.getRemark());

		return dto;
	}
}