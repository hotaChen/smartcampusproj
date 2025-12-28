package com.example.smartcampus.controller;

import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户信息管理相关接口")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "获取所有用户", description = "获取系统中所有用户信息")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("=== UserController.getAllUsers() 被调用 ===");
        try {
            List<User> users = userService.getAllUsers();
            logger.info("成功获取到 {} 个用户", users.size());

            // 记录每个用户的信息
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                logger.info("用户 {}: ID={}, 用户名={}, 真实姓名={}, 类型={}",
                        i + 1, user.getId(), user.getUsername(), user.getRealName(), user.getUserType());
            }

            // 过滤敏感信息
            users.forEach(user -> {
                user.setPassword("[PROTECTED]"); // 用占位符替换密码
            });

            return ResponseEntity.ok(users);

        } catch (Exception e) {
            logger.error("获取用户列表时发生错误: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户", description = "根据用户ID获取用户详细信息")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("=== UserController.getUserById() 被调用 ===");
        logger.info("请求的用户ID: {}", id);

        try {
            User user = userService.getUserById(id);
            logger.info("找到用户: ID={}, 用户名={}, 真实姓名={}",
                    user.getId(), user.getUsername(), user.getRealName());

            user.setPassword("[PROTECTED]");
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            logger.error("根据ID获取用户时发生错误: ID={}, 错误: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "根据用户名获取用户信息")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        logger.info("=== UserController.getUserByUsername() 被调用 ===");
        logger.info("请求的用户名: {}", username);

        try {
            Optional<User> userOpt = userService.getUserByUsername(username);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                logger.info("找到用户: 用户名={}, 真实姓名={}, 类型={}",
                        user.getUsername(), user.getRealName(), user.getUserType());
                user.setPassword("[PROTECTED]");
                return ResponseEntity.ok(user);
            } else {
                logger.warn("未找到用户: 用户名={}", username);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            logger.error("根据用户名获取用户时发生错误: 用户名={}, 错误: {}", username, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/type/{userType}")
    @Operation(summary = "根据类型获取用户", description = "根据用户类型获取用户列表")
    public ResponseEntity<List<User>> getUsersByType(@PathVariable String userType) {
        logger.info("=== UserController.getUsersByType() 被调用 ===");
        logger.info("请求的用户类型: {}", userType);

        try {
            List<User> users = userService.getUsersByType(userType);
            logger.info("找到 {} 个 {} 类型的用户", users.size(), userType);

            users.forEach(user -> {
                user.setPassword("[PROTECTED]");
            });

            return ResponseEntity.ok(users);

        } catch (Exception e) {
            logger.error("根据类型获取用户时发生错误: 类型={}, 错误: {}", userType, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/debug/info")
    @Operation(summary = "调试信息", description = "获取用户相关的调试信息")
    public ResponseEntity<Map<String, Object>> getDebugInfo() {
        logger.info("=== UserController.getDebugInfo() 被调用 ===");

        try {
            List<User> allUsers = userService.getAllUsers();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "用户控制器调试信息");
            response.put("totalUsers", allUsers.size());
            response.put("timestamp", LocalDateTime.now().toString());

            // 创建用户列表
            List<Map<String, Object>> userList = allUsers.stream()
                    .map(user -> {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("id", user.getId());
                        userInfo.put("username", user.getUsername());
                        userInfo.put("realName", user.getRealName());
                        userInfo.put("userType", user.getUserType());
                        userInfo.put("role", user.getRole() != null ? user.getRole().getName() : "无角色");
                        return userInfo;
                    })
                    .toList();

            response.put("users", userList);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("获取调试信息时发生错误: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "用户控制器健康检查")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("=== UserController.healthCheck() 被调用 ===");

        try {
            int userCount = userService.getAllUsers().size();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("service", "UserController");
            response.put("userCount", userCount);
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("message", "用户控制器运行正常");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("健康检查失败: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "DOWN");
            errorResponse.put("service", "UserController");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            errorResponse.put("message", "用户控制器运行异常");

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/student-number/{studentNumber}")
    @Operation(summary = "根据学号获取用户", description = "根据学生学号获取用户信息")
    public ResponseEntity<User> getUserByStudentNumber(@PathVariable String studentNumber) {
        logger.info("=== UserController.getUserByStudentNumber() 被调用 ===");
        logger.info("请求的学生学号: {}", studentNumber);

        try {
            Optional<User> userOpt = userService.getUserByStudentId(studentNumber);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                logger.info("找到用户: 学号={}, 用户名={}, 真实姓名={}",
                        studentNumber, user.getUsername(), user.getRealName());
                user.setPassword("[PROTECTED]");
                return ResponseEntity.ok(user);
            } else {
                logger.warn("未找到用户: 学号={}", studentNumber);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            logger.error("根据学号获取用户时发生错误: 学号={}, 错误: {}", studentNumber, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}