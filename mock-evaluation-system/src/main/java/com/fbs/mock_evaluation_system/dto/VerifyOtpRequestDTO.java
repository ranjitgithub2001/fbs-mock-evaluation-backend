package com.fbs.mock_evaluation_system.dto;

import jakarta.validation.constraints.NotBlank;

public class VerifyOtpRequestDTO {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "OTP is required")
    private String otp;

    public VerifyOtpRequestDTO() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}