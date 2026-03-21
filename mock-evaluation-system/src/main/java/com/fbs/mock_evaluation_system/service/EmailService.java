package com.fbs.mock_evaluation_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendApprovalEmail(String toEmail, String fullName,
            String password) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("FirstBit Solutions — Your Trainer Account is Approved");
        message.setText(
            "Dear " + fullName + ",\n\n" +
            "Your request to join FirstBit Solutions Mock Evaluation System " +
            "has been approved.\n\n" +
            "Your login credentials are:\n" +
            "Email:    " + toEmail + "\n" +
            "Password: " + password + "\n\n" +
            "Please log in and change your password after your first login.\n\n" +
            "Portal: http://localhost:3000\n\n" +
            "Regards,\n" +
            "FirstBit Solutions Admin Team"
        );

        mailSender.send(message);
    }

    public void sendRejectionEmail(String toEmail, String fullName,
            String reason) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("FirstBit Solutions — Trainer Request Update");
        message.setText(
            "Dear " + fullName + ",\n\n" +
            "Thank you for your interest in joining FirstBit Solutions " +
            "Mock Evaluation System.\n\n" +
            "After reviewing your request, we regret to inform you that " +
            "your request has not been approved at this time.\n\n" +
            (reason != null && !reason.isBlank()
                ? "Reason: " + reason + "\n\n"
                : "") +
            "You may reapply in the future.\n\n" +
            "Regards,\n" +
            "FirstBit Solutions Admin Team"
        );

        mailSender.send(message);
    }
    @Transactional
    public void sendStudentReport(String toEmail, String studentName,
            String batchName, String frn, String aiSummary,
            List<com.fbs.mock_evaluation_system.entity.Evaluation> evaluations) {

    	StringBuilder evalTable = new StringBuilder();
    	evalTable.append("Evaluation Records:\n");
    	evalTable.append("═══════════════════════════════════════════════════════════════════\n");

    	for (com.fbs.mock_evaluation_system.entity.Evaluation ev : evaluations) {
    	    String date = ev.getMockDate() != null
    	            ? ev.getMockDate().toString() : "—";
    	    String feedback = (ev.getRemark() != null && !ev.getRemark().isBlank())
    	            ? ev.getRemark() : "—";
    	    String moduleName = ev.getBatchModule().getCourseModule().getName();
    	    String result = ev.getFinalResult() != null ? ev.getFinalResult().toString() : "—";
    	    String tech = ev.getTechnicalScore() != null ? ev.getTechnicalScore().toString() : "—";
    	    String conf = ev.getConfidenceScore() != null ? ev.getConfidenceScore().toString() : "—";
    	    String comm = ev.getCommunicationScore() != null ? ev.getCommunicationScore().toString() : "—";
    	    String attempt = ev.getAttemptNumber() != null ? ev.getAttemptNumber().toString() : "—";

    	    String trainerName = ev.getTrainer() != null ? ev.getTrainer().getFullName() : "—";

    	    evalTable.append("Module  : ").append(moduleName).append("\n");
    	    evalTable.append("Trainer : ").append(trainerName).append("\n");
    	    evalTable.append("Attempt : ").append(attempt)
    	             .append("   |   Technical: ").append(tech)
    	             .append("  Confidence: ").append(conf)
    	             .append("  Communication: ").append(comm).append("\n");
    	    evalTable.append("Result  : ").append(result)
    	             .append("   |   Date: ").append(date).append("\n");
    	    evalTable.append("Feedback: ").append(feedback).append("\n");
    	    evalTable.append("───────────────────────────────────────────────────────────────\n");
    	}

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("FirstBit Solutions — Mock Evaluation Report for " + studentName);
        String cleanSummary = aiSummary
        	    .replace("**", "")
        	    .replace("* ", "• ")
        	    .replace("*", "");
        message.setText(
            "Dear " + studentName + ",\n\n" +
            "Please find below your mock evaluation performance report from FirstBit Solutions.\n\n" +
            "Student Details:\n" +
            "Name:  " + studentName + "\n" +
            "FRN:   " + frn + "\n" +
            "Batch: " + batchName + "\n\n" +
            "Performance Summary:\n" +
            "─────────────────────────────────\n" +
            cleanSummary + "\n" +
            "─────────────────────────────────\n\n" +
            evalTable +
            "\nFor any queries, please contact your trainer or visit FirstBit Solutions.\n\n" +
            "Regards,\n" +
            "FirstBit Solutions\n" +
            "Pune | firstbitsolutions.com"
        );

        mailSender.send(message);
    }}
