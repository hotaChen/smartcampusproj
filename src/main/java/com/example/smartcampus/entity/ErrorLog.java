package com.example.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "error_logs")
@Getter
@Setter
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String module; // 模块名称，如：用户管理、课程管理

    @Column(nullable = false)
    private String className; // 发生错误的类名

    @Column(nullable = false)
    private String methodName; // 发生错误的方法名

    @Column(columnDefinition = "TEXT")
    private String errorMessage; // 错误消息

    @Column(columnDefinition = "TEXT")
    private String stackTrace; // 完整的堆栈跟踪

    @Column(nullable = false)
    private String errorType; // 错误类型，如：NullPointerException, SQLException

    @Column(nullable = false)
    private String level; // 错误级别：ERROR, WARN, INFO

    @Column(length = 1000)
    private String requestUrl; // 请求URL（如果是Web请求）

    @Column(length = 20)
    private String requestMethod; // 请求方法：GET, POST, PUT, DELETE

    private String clientIp; // 客户端IP地址

    @Column(columnDefinition = "TEXT")
    private String requestParams; // 请求参数（JSON格式）

    @Column(nullable = false)
    private LocalDateTime errorTime;

    private Long userId; // 触发错误的用户ID（如果有）

    private String username; // 用户名

    @Column(nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (errorTime == null) {
            errorTime = LocalDateTime.now();
        }
    }
}