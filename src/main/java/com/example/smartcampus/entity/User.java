package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String realName;

    @Column(unique = true)
    private String email;

    private String phone;

    private String userType; // 用户类型: STUDENT, TEACHER, ADMIN

    // 学生特有字段
    private String studentId;     // 学号
    private String department;    // 院系
    private String major;         // 专业
    private Integer grade;        // 年级
    private String className;     // 班级

    // 教师特有字段
    private String teacherId;     // 工号
    private String title;         // 职称

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private Integer status = 1;   // 状态：0-禁用，1-启用

    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime lastLoginTime;

    // 构造函数
    public User() {}

    public User(String username, String password, String realName, String userType) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.userType = userType;
    }
}