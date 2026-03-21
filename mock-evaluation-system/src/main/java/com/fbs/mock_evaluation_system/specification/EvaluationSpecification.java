package com.fbs.mock_evaluation_system.specification;

import com.fbs.mock_evaluation_system.dto.EvaluationFilterDTO;
import com.fbs.mock_evaluation_system.entity.Evaluation;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EvaluationSpecification {

    private EvaluationSpecification() {}

    public static Specification<Evaluation> withFilters(EvaluationFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStudentId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("student").get("id"), filter.getStudentId())
                );
            }

            if (filter.getBatchModuleId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("batchModule").get("id"), filter.getBatchModuleId())
                );
            }

            if (filter.getTrainerId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("trainer").get("id"), filter.getTrainerId())
                );
            }

            if (filter.getMockStageId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("mockStage").get("id"), filter.getMockStageId())
                );
            }

            if (filter.getFinalResult() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("finalResult"), filter.getFinalResult())
                );
            }

            if (filter.getMockDateFrom() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("mockDate"), filter.getMockDateFrom())
                );
            }

            if (filter.getMockDateTo() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("mockDate"), filter.getMockDateTo())
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}