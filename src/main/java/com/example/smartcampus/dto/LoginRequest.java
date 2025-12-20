package com.example.smartcampus.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    private String username;
    private String password;
    private String userType; // STUDENT, TEACHER, ADMIN

    public LoginRequest() {}

    public LoginRequest(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
}