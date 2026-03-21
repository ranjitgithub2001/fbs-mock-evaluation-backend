package com.fbs.mock_evaluation_system.mapper;

import com.fbs.mock_evaluation_system.dto.UserResponseDTO;
import com.fbs.mock_evaluation_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());

        return dto;
    }
}