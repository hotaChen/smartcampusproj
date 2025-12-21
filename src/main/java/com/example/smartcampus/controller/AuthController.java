package com.example.smartcampus.controller;

import com.example.smartcampus.dto.LoginRequest;
import com.example.smartcampus.dto.LoginResponse;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.service.AuthService;
import com.example.smartcampus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户登录、注册、权限认证相关接口")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口，返回JWT Token")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(401)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            authService.register(user);
            return ResponseEntity.ok("用户注册成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "修改密码", description = "用户修改密码接口")
    public ResponseEntity<?> changePassword(@RequestParam Long userId,
                                            @RequestParam String oldPassword,
                                            @RequestParam String newPassword) {
        try {
            authService.changePassword(userId, oldPassword, newPassword);
            return ResponseEntity.ok("密码修改成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "管理员重置用户密码接口")
    public ResponseEntity<?> resetPassword(@RequestParam String username,
                                           @RequestParam String newPassword) {
        try {
            authService.resetPassword(username, newPassword);
            return ResponseEntity.ok("密码重置成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/validate-token")
    @Operation(summary = "验证Token", description = "验证JWT Token是否有效")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid ? "Token有效" : "Token无效");
    }

    @GetMapping("/user-info")
    @Operation(summary = "获取用户信息", description = "根据Token获取用户信息")
    public ResponseEntity<?> getUserInfo(@RequestParam String token) {
        try {
            User user = authService.getUserFromToken(token);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/health")
    @Operation(summary = "认证服务健康检查", description = "检查认证服务是否正常运行")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("认证服务运行正常");
    }
}