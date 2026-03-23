package com.fbs.mock_evaluation_system.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EmailService {

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private void sendEmail(String toEmail, String toName, String subject, String textContent) {
        try {
            String body = "{"
                + "\"sender\":{\"name\":\"FirstBit Solutions\",\"email\":\"" + fromEmail + "\"},"
                + "\"to\":[{\"email\":\"" + toEmail + "\",\"name\":\"" + toName + "\"}],"
                + "\"subject\":\"" + subject + "\","
                + "\"textContent\":\"" + textContent.replace("\"", "\\\"").replace("\n", "\\n") + "\""
                + "}";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BREVO_API_URL))
                .header("Content-Type", "application/json")
                .header("api-key", brevoApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                throw new RuntimeException("Brevo API error: " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendApprovalEmail(String toEmail, String fullName, String password) {
        String text =
            "Dear " + fullName + ",\n\n" +
            "Your request to join FirstBit Solutions Mock Evaluation System " +
            "has been approved.\n\n" +
            "Your login credentials are:\n" +
            "Email:    " + toEmail + "\n" +
            "Password: " + password + "\n\n" +
            "Please log in and change your password after your first login.\n\n" +
            "Regards,\nFirstBit Solutions Admin Team";

        sendEmail(toEmail, fullName,
            "FirstBit Solutions — Your Trainer Account is Approved", text);
    }

    public void sendRejectionEmail(String toEmail, String fullName, String reason) {
        String text =
            "Dear " + fullName + ",\n\n" +
            "Thank you for your interest in joining FirstBit Solutions " +
            "Mock Evaluation System.\n\n" +
            "After reviewing your request, we regret to inform you that " +
            "your request has not been approved at this time.\n\n" +
            (reason != null && !reason.isBlank() ? "Reason: " + reason + "\n\n" : "") +
            "You may reapply in the future.\n\n" +
            "Regards,\nFirstBit Solutions Admin Team";

        sendEmail(toEmail, fullName,
            "FirstBit Solutions — Trainer Request Update", text);
    }

    @Transactional
    public void sendStudentReport(String toEmail, String studentName,
            String batchName, String frn, String aiSummary,
            List<com.fbs.mock_evaluation_system.entity.Evaluation> evaluations) {

        StringBuilder evalTable = new StringBuilder();
        evalTable.append("Evaluation Records:\n");
        evalTable.append("═══════════════════════════════════════════════════════════════════\n");

        for (com.fbs.mock_evaluation_system.entity.Evaluation ev : evaluations) {
            String date = ev.getMockDate() != null ? ev.getMockDate().toString() : "—";
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

        String cleanSummary = aiSummary
            .replace("**", "")
            .replace("* ", "• ")
            .replace("*", "");

        String text =
            "Dear " + studentName + ",\n\n" +
            "Please find below your mock evaluation performance report.\n\n" +
            "Student Details:\n" +
            "Name:  " + studentName + "\n" +
            "FRN:   " + frn + "\n" +
            "Batch: " + batchName + "\n\n" +
            "Performance Summary:\n" +
            "─────────────────────────────────\n" +
            cleanSummary + "\n" +
            "─────────────────────────────────\n\n" +
            evalTable +
            "\nRegards,\nFirstBit Solutions\nPune | firstbitsolutions.com";

        sendEmail(toEmail, studentName,
            "FirstBit Solutions — Mock Evaluation Report for " + studentName, text);
    }

    public void sendOtpEmail(String toEmail, String fullName, String otp) {
        String text =
            "Dear " + fullName + ",\n\n" +
            "Your OTP for password reset is:\n\n" +
            "    " + otp + "\n\n" +
            "This OTP is valid for 10 minutes.\n\n" +
            "If you did not request a password reset, please ignore this email.\n\n" +
            "Regards,\nFirstBit Solutions";

        sendEmail(toEmail, fullName,
            "FirstBit Solutions — Password Reset OTP", text);
    }
}