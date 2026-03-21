package com.fbs.mock_evaluation_system.controller;

import com.fbs.mock_evaluation_system.dto.AuthRequestDTO;
import com.fbs.mock_evaluation_system.dto.AuthResponseDTO;
import com.fbs.mock_evaluation_system.service.AuthService;

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

    public AuthController(AuthService authService) {
        this.authService = authService;
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
}