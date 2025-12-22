package com.example.smartcampus.service;

import com.example.smartcampus.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getUsersByType(String userType);
    List<User> getUsersByRole(Long roleId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User updateUserStatus(Long id, Integer status);
}