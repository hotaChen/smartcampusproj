package com.example.smartcampus.controller;

import com.example.smartcampus.security.CustomUserDetailsService;
import com.example.smartcampus.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
@Tag(name = "认证测试", description = "认证状态测试接口")
public class AuthTestController {

    private static final Logger logger = LoggerFactory.getLogger(AuthTestController.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthTestController(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/auth-status")
    @Operation(summary = "认证状态检查", description = "检查当前请求的认证状态")
    public ResponseEntity<Map<String, Object>> getAuthStatus(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        logger.info("=== AuthTestController.getAuthStatus() 被调用 ===");
        logger.info("Authorization头: {}", authHeader);

        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        result.put("endpoint", "/api/test/auth-status");

        // 检查SecurityContext中的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result.put("authenticationObject", authentication != null ? authentication.getClass().getSimpleName() : "null");

        if (authentication != null && authentication.isAuthenticated()) {
            result.put("authenticated", true);
            result.put("username", authentication.getName());
            result.put("authorities", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            result.put("authenticationDetails", authentication.getDetails());

            logger.info("✅ 认证成功 - 用户名: {}, 权限: {}",
                    authentication.getName(),
                    authentication.getAuthorities());
        } else {
            result.put("authenticated", false);
            result.put("username", null);
            result.put("authorities", List.of());
            result.put("message", "未认证或认证失败");

            logger.warn("❌ 未认证 - authentication: {}", authentication);
        }

        // 检查SecurityContext本身
        result.put("securityContext", SecurityContextHolder.getContext().getClass().getSimpleName());
        result.put("authenticationExists", authentication != null);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/public-test")
    @Operation(summary = "公开测试接口", description = "无需认证的测试接口")
    public ResponseEntity<Map<String, Object>> publicTest() {
        logger.info("=== AuthTestController.publicTest() 被调用 ===");

        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个公开接口，无需认证");
        result.put("status", "success");
        result.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/protected-test")
    @Operation(summary = "受保护测试接口", description = "需要认证的测试接口")
    public ResponseEntity<Map<String, Object>> protectedTest() {
        logger.info("=== AuthTestController.protectedTest() 被调用 ===");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个受保护接口，需要认证");
        result.put("timestamp", System.currentTimeMillis());

        if (authentication != null && authentication.isAuthenticated()) {
            result.put("status", "authenticated");
            result.put("username", authentication.getName());
            result.put("authorities", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            logger.info("✅ 受保护接口访问成功");
        } else {
            result.put("status", "unauthenticated");
            result.put("error", "未认证或认证失败");
            logger.warn("❌ 受保护接口访问失败 - 未认证");
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/security-context")
    @Operation(summary = "SecurityContext详情", description = "获取SecurityContext的详细信息")
    public ResponseEntity<Map<String, Object>> getSecurityContextDetails() {
        logger.info("=== AuthTestController.getSecurityContextDetails() 被调用 ===");

        Map<String, Object> result = new HashMap<>();

        // 获取SecurityContext详细信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result.put("authenticationExists", authentication != null);

        if (authentication != null) {
            Map<String, Object> authDetails = new HashMap<>();
            authDetails.put("class", authentication.getClass().getName());
            authDetails.put("name", authentication.getName());
            authDetails.put("authenticated", authentication.isAuthenticated());
            authDetails.put("principalClass", authentication.getPrincipal().getClass().getName());
            authDetails.put("authorities", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            result.put("authentication", authDetails);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate-token-direct")
    @Operation(summary = "直接验证Token", description = "直接测试JWT Token验证")
    public ResponseEntity<Map<String, Object>> validateTokenDirect(
            @RequestHeader("Authorization") String authHeader) {

        System.out.println("=== 直接Token验证开始 ===");
        System.out.println("Authorization头: " + authHeader);

        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            result.put("tokenLength", token.length());
            result.put("tokenPrefix", token.substring(0, Math.min(20, token.length())) + "...");

            try {
                // 直接使用JwtUtil验证
                String username = jwtUtil.getUsernameFromToken(token);
                result.put("tokenUsername", username);
                result.put("tokenValid", true);

                // 尝试加载用户
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                result.put("userDetailsUsername", userDetails.getUsername());
                result.put("userAuthorities", userDetails.getAuthorities().toString());

                boolean tokenValid = jwtUtil.validateToken(token, userDetails);
                result.put("finalValidation", tokenValid);

                System.out.println("✅ Token验证结果: " + tokenValid);
                System.out.println("用户名: " + username);
                System.out.println("用户权限: " + userDetails.getAuthorities());

            } catch (Exception e) {
                result.put("error", e.getMessage());
                result.put("errorType", e.getClass().getSimpleName());
                System.out.println("❌ Token验证异常: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            result.put("error", "Authorization头格式不正确");
            System.out.println("❌ Authorization头格式不正确");
        }

        System.out.println("=== 直接Token验证结束 ===");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/jwt-debug")
    @Operation(summary = "JWT调试信息", description = "获取JWT相关调试信息")
    public ResponseEntity<Map<String, Object>> getJwtDebugInfo(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());

        if (authHeader != null) {
            result.put("authorizationHeader", authHeader);
            result.put("hasBearer", authHeader.startsWith("Bearer "));

            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                result.put("tokenLength", token.length());

                try {
                    String username = jwtUtil.getUsernameFromToken(token);
                    result.put("parsedUsername", username);
                    result.put("parseSuccess", true);
                } catch (Exception e) {
                    result.put("parseError", e.getMessage());
                    result.put("parseSuccess", false);
                }
            }
        }

        // 检查当前认证状态
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        result.put("currentAuth", auth != null ? auth.getName() : "null");
        result.put("currentAuthorities", auth != null ?
                auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()) :
                List.of());

        return ResponseEntity.ok(result);
    }
}