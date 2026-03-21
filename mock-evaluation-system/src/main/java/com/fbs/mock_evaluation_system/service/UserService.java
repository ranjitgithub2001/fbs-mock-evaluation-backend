package com.fbs.mock_evaluation_system.service;

import com.fbs.mock_evaluation_system.dto.ChangePasswordRequestDTO;
import com.fbs.mock_evaluation_system.dto.PageResponseDTO;
import com.fbs.mock_evaluation_system.dto.UserFilterDTO;
import com.fbs.mock_evaluation_system.dto.UserRequestDTO;
import com.fbs.mock_evaluation_system.dto.UserResponseDTO;
import com.fbs.mock_evaluation_system.entity.User;
import com.fbs.mock_evaluation_system.exception.DuplicateResourceException;
import com.fbs.mock_evaluation_system.exception.InvalidInputException;
import com.fbs.mock_evaluation_system.exception.ResourceNotFoundException;
import com.fbs.mock_evaluation_system.mapper.UserMapper;
import com.fbs.mock_evaluation_system.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final String DEFAULT_PASSWORD = "Welcome@123";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedName = request.getFullName().trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + normalizedEmail);
        }

        User user = new User();
        user.setFullName(normalizedName);
        user.setEmail(normalizedEmail);
        user.setRole(request.getRole());
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

        User saved = userRepository.save(user);

        return userMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<UserResponseDTO> getUsers(UserFilterDTO filter,
            Pageable pageable) {

        Page<User> userPage;

        boolean hasRole = filter.getRole() != null;
        boolean hasActive = filter.getActive() != null;

        if (hasRole && hasActive) {
            userPage = userRepository.findByRoleAndActive(
                    filter.getRole(), filter.getActive(), pageable);
        } else if (hasRole) {
            userPage = userRepository.findByRole(filter.getRole(), pageable);
        } else if (hasActive) {
            userPage = userRepository.findByActive(filter.getActive(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        List<UserResponseDTO> content = new ArrayList<>();
        for (User user : userPage.getContent()) {
            content.add(userMapper.toResponseDTO(user));
        }

        return new PageResponseDTO<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedName = request.getFullName().trim();

        if (userRepository.existsByEmailAndIdNot(normalizedEmail, id)) {
            throw new DuplicateResourceException(
                    "Another user already exists with email: " + normalizedEmail);
        }

        user.setFullName(normalizedName);
        user.setEmail(normalizedEmail);
        user.setRole(request.getRole());

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDTO request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidInputException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException(
                    "New password and confirm password do not match");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new InvalidInputException(
                    "New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDTO deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));

        if (!user.isActive()) {
            throw new InvalidInputException("User is already inactive");
        }

        user.setActive(false);

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));

        if (user.isActive()) {
            throw new InvalidInputException("User is already active");
        }

        user.setActive(true);

        return userMapper.toResponseDTO(userRepository.save(user));
    }
}