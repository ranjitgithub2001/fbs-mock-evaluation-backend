package com.fbs.mock_evaluation_system.controller;

import com.fbs.mock_evaluation_system.dto.AuthRequestDTO;
import com.fbs.mock_evaluation_system.dto.AuthResponseDTO;
import com.fbs.mock_evaluation_system.dto.ForgotPasswordRequestDTO;
import com.fbs.mock_evaluation_system.dto.ResetPasswordRequestDTO;
import com.fbs.mock_evaluation_system.dto.VerifyOtpRequestDTO;
import com.fbs.mock_evaluation_system.service.AuthService;
import com.fbs.mock_evaluation_system.service.ForgotPasswordService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final ForgotPasswordService forgotPasswordService;

	public AuthController(AuthService authService,
	        ForgotPasswordService forgotPasswordService) {
	    this.authService = authService;
	    this.forgotPasswordService = forgotPasswordService;
	}

    // POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody AuthRequestDTO request) {

        AuthResponseDTO response = authService.login(request);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/test-hash")
    public String testHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode("Welcome@123");
    }
 // POST /auth/forgot-password
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO request) {
        forgotPasswordService.sendOtp(request);
        return ResponseEntity.ok().build();
    }

    // POST /auth/verify-otp
    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(
            @Valid @RequestBody VerifyOtpRequestDTO request) {
        boolean valid = forgotPasswordService.verifyOtp(request);
        return ResponseEntity.ok(valid);
    }

    // POST /auth/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {
        forgotPasswordService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}