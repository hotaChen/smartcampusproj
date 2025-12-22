package com.example.smartcampus.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String realName;
    private String userType;
    private String role;
    private String message;

    public LoginResponse(String token, Long userId, String username, String realName, String userType, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.userType = userType;
        this.role = role;
        this.message = "登录成功";
    }

    public LoginResponse(String message) {
        this.message = message;
    }
}