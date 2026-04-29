package com.fbs.mock_evaluation_system.repository;

import com.fbs.mock_evaluation_system.entity.User;
import com.fbs.mock_evaluation_system.entity.UserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // For auth — load user by email
    Optional<User> findByEmail(String email);

    // For duplicate check on create
    boolean existsByEmail(String email);

    // For duplicate check on update — exclude current user
    boolean existsByEmailAndIdNot(String email, Long id);

    // Filter queries
    Page<User> findByRole(UserRole role, Pageable pageable);

    Page<User> findByActive(boolean active, Pageable pageable);

    Page<User> findByRoleAndActive(UserRole role, boolean active, Pageable pageable);
}