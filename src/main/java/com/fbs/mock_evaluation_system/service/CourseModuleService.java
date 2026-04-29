package com.fbs.mock_evaluation_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fbs.mock_evaluation_system.dto.CourseModuleRequestDTO;
import com.fbs.mock_evaluation_system.dto.CourseModuleResponseDTO;
import com.fbs.mock_evaluation_system.entity.CourseModule;
import com.fbs.mock_evaluation_system.exception.DuplicateResourceException;
import com.fbs.mock_evaluation_system.exception.InvalidInputException;
import com.fbs.mock_evaluation_system.exception.ResourceNotFoundException;
import com.fbs.mock_evaluation_system.mapper.CourseModuleMapper;
import com.fbs.mock_evaluation_system.repository.CourseModuleRepository;

@Service
public class CourseModuleService {

    private final CourseModuleRepository moduleRepository;

    public CourseModuleService(CourseModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    public CourseModuleResponseDTO createModule(CourseModuleRequestDTO request) {

        String normalizedName = request.getName().trim().toUpperCase();

        if (moduleRepository.existsByName(normalizedName)) {
            throw new InvalidInputException("Module already exists: " + normalizedName);
        }

        CourseModule module = CourseModuleMapper.toEntity(normalizedName);

        try {
            CourseModule savedModule = moduleRepository.save(module);
            return CourseModuleMapper.toDTO(savedModule);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidInputException("Module already exists: " + normalizedName);
        }
    }

    @Transactional(readOnly = true)
    public List<CourseModuleResponseDTO> getAllModules() {

        List<CourseModule> modules = moduleRepository.findAll();
        List<CourseModuleResponseDTO> response = new ArrayList<>();

        for (CourseModule module : modules) {
            response.add(CourseModuleMapper.toDTO(module));
        }

        return response;
    }

    @Transactional(readOnly = true)
    public CourseModuleResponseDTO getModuleById(Long id) {

        CourseModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module not found with id: " + id));

        return CourseModuleMapper.toDTO(module);
    }

    @Transactional
    public CourseModuleResponseDTO updateModule(Long id,
            CourseModuleRequestDTO request) {

        CourseModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module not found with id: " + id));

        String normalizedName = request.getName().trim().toUpperCase();

        if (moduleRepository.existsByNameAndIdNot(normalizedName, id)) {
            throw new DuplicateResourceException(
                    "Module already exists with name: " + normalizedName);
        }

        module.setName(normalizedName);

        return CourseModuleMapper.toDTO(moduleRepository.save(module));
    }

    @Transactional
    public void deleteModule(Long id) {

        CourseModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module not found with id: " + id));

        try {
            moduleRepository.delete(module);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidInputException(
                    "Module is already assigned and cannot be deleted");
        }
    }
}