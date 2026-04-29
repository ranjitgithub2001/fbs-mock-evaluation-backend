package com.fbs.mock_evaluation_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fbs.mock_evaluation_system.dto.BatchRequestDTO;
import com.fbs.mock_evaluation_system.dto.BatchResponseDTO;
import com.fbs.mock_evaluation_system.entity.Batch;
import com.fbs.mock_evaluation_system.exception.DuplicateResourceException;
import com.fbs.mock_evaluation_system.exception.InvalidInputException;
import com.fbs.mock_evaluation_system.exception.ResourceNotFoundException;
import com.fbs.mock_evaluation_system.mapper.BatchMapper;
import com.fbs.mock_evaluation_system.repository.BatchRepository;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────

    @Transactional
    public BatchResponseDTO createBatch(BatchRequestDTO requestDTO) {

        String normalizedName = requestDTO.getBatchName().trim();
        String normalizedCode = requestDTO.getFrnBatchCode().trim().toUpperCase();

        if (batchRepository.existsByBatchName(normalizedName)) {
            throw new InvalidInputException(
                    "Batch name already exists: " + normalizedName);
        }

        if (batchRepository.existsByFrnBatchCode(normalizedCode)) {
            throw new InvalidInputException(
                    "FRN batch code already exists: " + normalizedCode);
        }

        Batch batch = new Batch();
        batch.setBatchName(normalizedName);
        batch.setFrnBatchCode(normalizedCode);

        try {
            Batch savedBatch = batchRepository.save(batch);
            return BatchMapper.toDTO(savedBatch);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidInputException("Batch already exists (duplicate detected)");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // GET ALL
    // ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BatchResponseDTO> getAllBatches() {

        List<Batch> batches = batchRepository.findAll();

        List<BatchResponseDTO> response = new ArrayList<>();

        for (Batch batch : batches) {
            response.add(BatchMapper.toDTO(batch));
        }

        return response;
    }

    // ─────────────────────────────────────────────────────────────
    // GET BY ID
    // ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public BatchResponseDTO getBatchById(Long id) {

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Batch not found with id: " + id));

        return BatchMapper.toDTO(batch);
    }

    // ─────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────

    @Transactional
    public BatchResponseDTO updateBatch(Long id, BatchRequestDTO requestDTO) {

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Batch not found with id: " + id));

        String normalizedName = requestDTO.getBatchName().trim();
        String normalizedCode = requestDTO.getFrnBatchCode().trim().toUpperCase();

        // Check name uniqueness — exclude current batch
        if (batchRepository.existsByBatchNameAndIdNot(normalizedName, id)) {
            throw new DuplicateResourceException(
                    "Batch name already exists: " + normalizedName);
        }

        // Check code uniqueness — exclude current batch
        if (batchRepository.existsByFrnBatchCodeAndIdNot(normalizedCode, id)) {
            throw new DuplicateResourceException(
                    "FRN batch code already exists: " + normalizedCode);
        }

        batch.setBatchName(normalizedName);
        batch.setFrnBatchCode(normalizedCode);

        return BatchMapper.toDTO(batchRepository.save(batch));
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────

    @Transactional
    public void deleteBatch(Long id) {

        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Batch not found with id: " + id));

        try {
            batchRepository.delete(batch);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidInputException(
                    "Batch cannot be deleted as it has students or modules assigned");
        }
    }
}