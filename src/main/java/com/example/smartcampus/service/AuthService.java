package com.example.smartcampus.service;

import com.example.smartcampus.dto.LoginRequest;
import com.example.smartcampus.dto.LoginResponse;
import com.example.smartcampus.entity.User;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    void register(User user);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void resetPassword(String username, String newPassword);
    boolean validateToken(String token);
    User getUserFromToken(String token);
}