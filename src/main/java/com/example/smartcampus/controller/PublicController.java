package com.example.smartcampus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@Tag(name = "公开接口", description = "无需认证即可访问的接口")
public class PublicController {

    @GetMapping("/welcome")
    @Operation(summary = "欢迎信息", description = "获取系统欢迎信息")
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "欢迎使用智能校园管理系统");
        response.put("version", "1.0.0");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-users")
    @Operation(summary = "测试账号信息", description = "获取测试用的账号信息")
    public ResponseEntity<Map<String, Object>> getTestAccounts() {
        Map<String, Object> accounts = new HashMap<>();

        accounts.put("admin", Map.of(
                "username", "admin",
                "password", "admin123",
                "role", "ROLE_ADMIN",
                "description", "系统管理员"
        ));

        accounts.put("teacher", Map.of(
                "username", "teacher001",
                "password", "teacher123",
                "role", "ROLE_TEACHER",
                "description", "测试教师"
        ));

        accounts.put("student", Map.of(
                "username", "student001",
                "password", "student123",
                "role", "ROLE_STUDENT",
                "description", "测试学生"
        ));

        return ResponseEntity.ok(accounts);
    }
}