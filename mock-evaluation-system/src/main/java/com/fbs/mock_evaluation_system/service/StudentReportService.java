package com.fbs.mock_evaluation_system.service;

import com.fbs.mock_evaluation_system.dto.StudentReportEmailRequestDTO;
import com.fbs.mock_evaluation_system.entity.Evaluation;
import com.fbs.mock_evaluation_system.entity.Student;
import com.fbs.mock_evaluation_system.exception.ResourceNotFoundException;
import com.fbs.mock_evaluation_system.repository.EvaluationRepository;
import com.fbs.mock_evaluation_system.repository.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentReportService {

    private final StudentRepository studentRepository;
    private final EvaluationRepository evaluationRepository;
    private final EmailService emailService;

    public StudentReportService(StudentRepository studentRepository,
            EvaluationRepository evaluationRepository,
            EmailService emailService) {
        this.studentRepository = studentRepository;
        this.evaluationRepository = evaluationRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void sendReport(StudentReportEmailRequestDTO request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + request.getStudentId()));

        List<Evaluation> evaluations = evaluationRepository
                .findByStudentIdWithModules(request.getStudentId());

        emailService.sendStudentReport(
                request.getRecipientEmail(),
                student.getName(),
                student.getBatch().getBatchName(),
                student.getFrn(),
                request.getAiSummary(),
                evaluations
        );
    }
}