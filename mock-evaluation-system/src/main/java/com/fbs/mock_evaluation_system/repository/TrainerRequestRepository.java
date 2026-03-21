package com.fbs.mock_evaluation_system.repository;

import com.fbs.mock_evaluation_system.entity.TrainerRequest;
import com.fbs.mock_evaluation_system.entity.TrainerRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRequestRepository extends JpaRepository<TrainerRequest, Long> {

    boolean existsByEmail(String email);

    Page<TrainerRequest> findByStatus(TrainerRequestStatus status, Pageable pageable);
}