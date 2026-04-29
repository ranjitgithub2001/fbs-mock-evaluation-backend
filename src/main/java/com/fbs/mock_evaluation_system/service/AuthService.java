package com.fbs.mock_evaluation_system.service;

import com.fbs.mock_evaluation_system.dto.AuthRequestDTO;
import com.fbs.mock_evaluation_system.dto.AuthResponseDTO;
import com.fbs.mock_evaluation_system.entity.User;
import com.fbs.mock_evaluation_system.exception.InvalidInputException;
import com.fbs.mock_evaluation_system.repository.UserRepository;
import com.fbs.mock_evaluation_system.security.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(AuthRequestDTO request) {

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        // 1 — Find user by email
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new InvalidInputException(
                        "Invalid email or password"));

        // 2 — Check active status
        if (!user.isActive()) {
            throw new InvalidInputException(
                    "Your account has been deactivated. Please contact admin.");
        }

        // 3 — Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidInputException("Invalid email or password");
        }

        // 4 — Generate JWT token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name());

        return new AuthResponseDTO(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}