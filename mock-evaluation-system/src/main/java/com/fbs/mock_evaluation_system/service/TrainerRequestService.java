package com.fbs.mock_evaluation_system.service;

import com.fbs.mock_evaluation_system.dto.TrainerRequestDTO;
import com.fbs.mock_evaluation_system.dto.TrainerRequestResponseDTO;
import com.fbs.mock_evaluation_system.entity.TrainerRequest;
import com.fbs.mock_evaluation_system.entity.TrainerRequestStatus;
import com.fbs.mock_evaluation_system.entity.User;
import com.fbs.mock_evaluation_system.entity.UserRole;
import com.fbs.mock_evaluation_system.exception.DuplicateResourceException;
import com.fbs.mock_evaluation_system.exception.InvalidInputException;
import com.fbs.mock_evaluation_system.exception.ResourceNotFoundException;
import com.fbs.mock_evaluation_system.repository.TrainerRequestRepository;
import com.fbs.mock_evaluation_system.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TrainerRequestService {

    private static final String DEFAULT_PASSWORD = "Welcome@123";

    private final TrainerRequestRepository trainerRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public TrainerRequestService(
            TrainerRequestRepository trainerRequestRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.trainerRequestRepository = trainerRequestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public TrainerRequestResponseDTO submitRequest(TrainerRequestDTO request) {

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (trainerRequestRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException(
                    "A request with this email already exists");
        }

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException(
                    "An account with this email already exists");
        }

        TrainerRequest trainerRequest = new TrainerRequest();
        trainerRequest.setFullName(request.getFullName().trim());
        trainerRequest.setEmail(normalizedEmail);
        trainerRequest.setPhone(request.getPhone().trim());
        trainerRequest.setQualification(request.getQualification().trim());
        trainerRequest.setExpertise(request.getExpertise().trim());
        trainerRequest.setStatus(TrainerRequestStatus.PENDING);
        trainerRequest.setRequestedAt(LocalDateTime.now());

        TrainerRequest saved = trainerRequestRepository.save(trainerRequest);

        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<TrainerRequestResponseDTO> getRequests(
            TrainerRequestStatus status, Pageable pageable) {

        Page<TrainerRequest> page;

        if (status != null) {
            page = trainerRequestRepository.findByStatus(status, pageable);
        } else {
            page = trainerRequestRepository.findAll(pageable);
        }

        return page.map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public TrainerRequestResponseDTO getRequestById(Long id) {

        TrainerRequest request = trainerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer request not found with id: " + id));

        return toResponseDTO(request);
    }

    @Transactional
    public TrainerRequestResponseDTO approveRequest(Long id) {

        TrainerRequest request = trainerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer request not found with id: " + id));

        if (request.getStatus() != TrainerRequestStatus.PENDING) {
            throw new InvalidInputException(
                    "Only PENDING requests can be approved");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "An account with this email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(UserRole.TRAINER);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

        userRepository.save(user);

        request.setStatus(TrainerRequestStatus.APPROVED);
        request.setReviewedAt(LocalDateTime.now());
        TrainerRequest saved = trainerRequestRepository.save(request);

        emailService.sendApprovalEmail(
                request.getEmail(),
                request.getFullName(),
                DEFAULT_PASSWORD);

        return toResponseDTO(saved);
    }

    @Transactional
    public TrainerRequestResponseDTO rejectRequest(Long id, String reason) {

        TrainerRequest request = trainerRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer request not found with id: " + id));

        if (request.getStatus() != TrainerRequestStatus.PENDING) {
            throw new InvalidInputException(
                    "Only PENDING requests can be rejected");
        }

        request.setStatus(TrainerRequestStatus.REJECTED);
        request.setReviewedAt(LocalDateTime.now());
        request.setRejectionReason(reason);
        TrainerRequest saved = trainerRequestRepository.save(request);

        emailService.sendRejectionEmail(
                request.getEmail(),
                request.getFullName(),
                reason);

        return toResponseDTO(saved);
    }

    private TrainerRequestResponseDTO toResponseDTO(TrainerRequest request) {

        TrainerRequestResponseDTO dto = new TrainerRequestResponseDTO();
        dto.setId(request.getId());
        dto.setFullName(request.getFullName());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        dto.setQualification(request.getQualification());
        dto.setExpertise(request.getExpertise());
        dto.setStatus(request.getStatus());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setReviewedAt(request.getReviewedAt());
        dto.setRejectionReason(request.getRejectionReason());
        return dto;
    }
}