package com.fbs.mock_evaluation_system.dto;

import com.fbs.mock_evaluation_system.entity.UserRole;

public class UserFilterDTO {

    private UserRole role;
    private Boolean active;

    public UserFilterDTO() {
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}