package com.fbs.mock_evaluation_system.repository;

import com.fbs.mock_evaluation_system.entity.Evaluation;
import com.fbs.mock_evaluation_system.entity.EvaluationResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>,
        JpaSpecificationExecutor<Evaluation> {

    // Already existing — keep as is
    List<Evaluation> findByStudentId(Long studentId);

    // For attemptNumber calculation — already existing
    long countByStudentIdAndBatchModuleIdAndMockStageIdAndFinalResultNot(
            Long studentId, Long batchModuleId, Long mockStageId,
            com.fbs.mock_evaluation_system.entity.EvaluationResult result
    );

    // For stageSequence duplicate check — already existing
    boolean existsByStudentIdAndBatchModuleIdAndMockStageIdAndStageSequence(
            Long studentId, Long batchModuleId, Long mockStageId, Integer stageSequence
    );

    boolean existsByStudentIdAndBatchModuleIdAndMockStageIdAndStageSequenceAndIdNot(
            Long studentId, Long batchModuleId, Long mockStageId, Integer stageSequence, Long id
    );
    long countByStudentIdAndBatchModuleIdAndMockStageIdAndFinalResultNotAndIdNot(
            Long studentId,
            Long batchModuleId,
            Long mockStageId,
            EvaluationResult finalResult,
            Long id
    );
    @Query("SELECT e FROM Evaluation e " +
    	       "LEFT JOIN FETCH e.batchModule bm " +
    	       "LEFT JOIN FETCH bm.courseModule " +
    	       "WHERE e.student.id = :studentId " +
    	       "ORDER BY e.mockDate ASC")
    	List<Evaluation> findByStudentIdWithModules(@Param("studentId") Long studentId);
    
    
    
}