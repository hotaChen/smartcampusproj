package com.example.smartcampus.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class UserDto {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String userType;
    private String studentId;
    private String teacherId;
    private String department;
    private String major;
    private Integer grade;
    private String className;
    private String title;
    private String roleName;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;

    public UserDto() {}
}