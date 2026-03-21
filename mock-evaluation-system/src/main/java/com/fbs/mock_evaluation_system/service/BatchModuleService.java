package com.fbs.mock_evaluation_system.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fbs.mock_evaluation_system.dto.BatchModuleRequestDTO;
import com.fbs.mock_evaluation_system.dto.BatchModuleResponseDTO;
import com.fbs.mock_evaluation_system.entity.Batch;
import com.fbs.mock_evaluation_system.entity.BatchModule;
import com.fbs.mock_evaluation_system.entity.CourseModule;
import com.fbs.mock_evaluation_system.exception.DuplicateResourceException;
import com.fbs.mock_evaluation_system.exception.InvalidInputException;
import com.fbs.mock_evaluation_system.exception.ResourceNotFoundException;
import com.fbs.mock_evaluation_system.mapper.BatchModuleMapper;
import com.fbs.mock_evaluation_system.repository.BatchModuleRepository;
import com.fbs.mock_evaluation_system.repository.BatchRepository;
import com.fbs.mock_evaluation_system.repository.CourseModuleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchModuleService {

    private final BatchRepository batchRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final BatchModuleRepository batchModuleRepository;
    private final BatchModuleMapper batchModuleMapper;

    public BatchModuleService(
            BatchRepository batchRepository,
            CourseModuleRepository courseModuleRepository,
            BatchModuleRepository batchModuleRepository,
            BatchModuleMapper batchModuleMapper) {
        this.batchRepository = batchRepository;
        this.courseModuleRepository = courseModuleRepository;
        this.batchModuleRepository = batchModuleRepository;
        this.batchModuleMapper = batchModuleMapper;
    }

    // ─────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────

    @Transactional
    public BatchModuleResponseDTO create(BatchModuleRequestDTO request) {

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Batch not found"));

        CourseModule courseModule = courseModuleRepository
                .findById(request.getCourseModuleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course Module not found"));

        boolean exists = batchModuleRepository
                .existsByBatchIdAndCourseModuleId(
                        batch.getId(), courseModule.getId());

        if (exists) {
            throw new DuplicateResourceException(
                    "Batch already mapped to this Course Module");
        }

        BatchModule batchModule = new BatchModule(batch, courseModule);

        BatchModule saved = batchModuleRepository.save(batchModule);

        return batchModuleMapper.toResponseDTO(saved);
    }

    // ─────────────────────────────────────────────────────────────
    // GET ALL
    // ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BatchModuleResponseDTO> getAll() {

        List<BatchModule> batchModules = batchModuleRepository.findAll();

        List<BatchModuleResponseDTO> response = new ArrayList<>();

        for (BatchModule batchModule : batchModules) {
            response.add(batchModuleMapper.toResponseDTO(batchModule));
        }

        return response;
    }

    // ─────────────────────────────────────────────────────────────
    // GET BY BATCH ID
    // ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BatchModuleResponseDTO> getByBatchId(Long batchId) {

        batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Batch not found with id: " + batchId));

        List<BatchModule> batchModules =
                batchModuleRepository.findByBatchId(batchId);

        List<BatchModuleResponseDTO> response = new ArrayList<>();

        for (BatchModule batchModule : batchModules) {
            response.add(batchModuleMapper.toResponseDTO(batchModule));
        }

        return response;
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {

        BatchModule batchModule = batchModuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BatchModule not found with id: " + id));

        try {
            batchModuleRepository.delete(batchModule);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidInputException(
                    "BatchModule cannot be deleted as it has evaluations linked to it");
        }
    }
}