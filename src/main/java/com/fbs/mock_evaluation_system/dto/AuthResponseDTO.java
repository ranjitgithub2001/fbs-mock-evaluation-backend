package com.fbs.mock_evaluation_system.dto;

import com.fbs.mock_evaluation_system.entity.UserRole;

public class AuthResponseDTO {

    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private UserRole role;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, Long userId, String fullName,
            String email, UserRole role) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}