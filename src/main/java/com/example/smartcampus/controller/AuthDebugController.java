package com.example.smartcampus.controller;

import com.example.smartcampus.dto.LoginRequest;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.security.CustomUserDetailsService;
import com.example.smartcampus.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/debug/auth")
@Tag(name = "认证调试", description = "认证系统调试接口")
public class AuthDebugController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthDebugController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil,
                               AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/check-users")
    @Operation(summary = "检查用户数据", description = "检查数据库中的用户数据")
    public ResponseEntity<Map<String, Object>> checkUsers() {
        Map<String, Object> result = new HashMap<>();

        // 检查所有用户
        result.put("allUsers", userRepository.findAll());

        // 检查管理员用户
        Optional<User> adminUser = userRepository.findByUsername("admin");
        result.put("adminUserExists", adminUser.isPresent());
        if (adminUser.isPresent()) {
            User admin = adminUser.get();
            Map<String, Object> adminInfo = new HashMap<>();
            adminInfo.put("id", admin.getId());
            adminInfo.put("username", admin.getUsername());
            adminInfo.put("password", admin.getPassword());
            adminInfo.put("userType", admin.getUserType());
            adminInfo.put("role", admin.getRole() != null ? admin.getRole().getName() : "无角色");
            adminInfo.put("status", admin.getStatus());

            // 验证密码
            boolean passwordMatches = passwordEncoder.matches("admin123", admin.getPassword());
            adminInfo.put("passwordMatches", passwordMatches);

            result.put("adminInfo", adminInfo);
        }

        // 用户统计
        result.put("totalUsers", userRepository.count());
        result.put("adminCount", userRepository.findByUserType("ADMIN").size());
        result.put("teacherCount", userRepository.findByUserType("TEACHER").size());
        result.put("studentCount", userRepository.findByUserType("STUDENT").size());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/test-login")
    @Operation(summary = "测试登录", description = "直接测试登录逻辑")
    public ResponseEntity<Map<String, Object>> testLogin(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 检查用户是否存在
            Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
            if (userOpt.isEmpty()) {
                result.put("error", "用户不存在");
                return ResponseEntity.badRequest().body(result);
            }

            User user = userOpt.get();
            result.put("userFound", true);
            result.put("username", user.getUsername());
            result.put("userType", user.getUserType());
            result.put("status", user.getStatus());

            // 2. 检查用户状态
            if (user.getStatus() != 1) {
                result.put("error", "用户已被禁用");
                return ResponseEntity.badRequest().body(result);
            }

            // 3. 验证密码
            boolean passwordValid = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            result.put("passwordValid", passwordValid);

            if (!passwordValid) {
                result.put("error", "密码错误");
                result.put("storedPassword", user.getPassword());
                result.put("inputPassword", loginRequest.getPassword());
                return ResponseEntity.badRequest().body(result);
            }

            // 4. 测试Spring Security认证
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );
                result.put("springAuthSuccess", true);
                result.put("authenticatedUser", authentication.getName());
            } catch (Exception e) {
                result.put("springAuthError", e.getMessage());
                return ResponseEntity.badRequest().body(result);
            }

            // 5. 测试JWT生成
            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
                String token = jwtUtil.generateToken(userDetails);
                result.put("jwtGenerated", true);
                result.put("token", token);

                // 验证Token
                boolean tokenValid = jwtUtil.validateToken(token, userDetails);
                result.put("tokenValid", tokenValid);

            } catch (Exception e) {
                result.put("jwtError", e.getMessage());
                return ResponseEntity.badRequest().body(result);
            }

            result.put("success", true);
            result.put("message", "登录测试成功");

        } catch (Exception e) {
            result.put("error", "系统错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-password")
    @Operation(summary = "检查密码", description = "检查指定用户的密码")
    public ResponseEntity<Map<String, Object>> checkPassword(
            @RequestParam String username,
            @RequestParam String password) {

        Map<String, Object> result = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            result.put("error", "用户不存在");
            return ResponseEntity.badRequest().body(result);
        }

        User user = userOpt.get();
        boolean matches = passwordEncoder.matches(password, user.getPassword());

        result.put("username", username);
        result.put("passwordMatches", matches);
        result.put("storedPasswordHash", user.getPassword());
        result.put("inputPassword", password);

        return ResponseEntity.ok(result);
    }
}