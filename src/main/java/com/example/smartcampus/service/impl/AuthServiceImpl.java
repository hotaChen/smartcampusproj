package com.example.smartcampus.service.impl;

import com.example.smartcampus.dto.LoginRequest;
import com.example.smartcampus.dto.LoginResponse;
import com.example.smartcampus.entity.Role;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.RoleRepository;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.security.CustomUserDetails;
import com.example.smartcampus.service.AuthService;
import com.example.smartcampus.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // 验证用户凭证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            // 更新最后登录时间
            User user = userRepository.findById(userDetails.getUserId())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);

            return new LoginResponse(
                    jwt,
                    user.getId(),
                    user.getUsername(),
                    user.getRealName(),
                    user.getUserType(),
                    user.getRole().getName()
            );

        } catch (Exception e) {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    @Override
    public void register(User user) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 必填字段校验
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (user.getRealName() == null || user.getRealName().isBlank()) {
            throw new RuntimeException("真实姓名不能为空");
        }
        if (user.getUserType() == null || user.getUserType().isBlank()) {
            throw new RuntimeException("用户类型不能为空");
        }

        // 根据角色校验必填字段
        String type = user.getUserType().toUpperCase();
        switch (type) {
            case "STUDENT":
                if (user.getStudentId() == null || user.getStudentId().isBlank()) {
                    throw new RuntimeException("学生学号不能为空");
                }
                if (user.getDepartment() == null || user.getDepartment().isBlank()) {
                    throw new RuntimeException("院系不能为空");
                }
                if (user.getMajor() == null || user.getMajor().isBlank()) {
                    throw new RuntimeException("专业不能为空");
                }
                if (user.getGrade() == null) {
                    throw new RuntimeException("年级不能为空");
                }
                if (user.getClassName() == null || user.getClassName().isBlank()) {
                    throw new RuntimeException("班级不能为空");
                }
                break;
            case "TEACHER":
                if (user.getTeacherId() == null || user.getTeacherId().isBlank()) {
                    throw new RuntimeException("教师工号不能为空");
                }
                if (user.getTitle() == null || user.getTitle().isBlank()) {
                    throw new RuntimeException("职称不能为空");
                }
                if (user.getDepartment() == null || user.getDepartment().isBlank()) {
                    throw new RuntimeException("院系不能为空");
                }
                break;
            case "ADMIN":
                // 管理员不需要额外字段
                break;
            default:
                throw new RuntimeException("未知用户类型: " + user.getUserType());
        }

        // 设置默认密码并加密
        if (user.getPassword() == null) {
            user.setPassword("123456"); // 默认密码
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 设置默认角色
        if (user.getRole() == null) {
            String roleName = "ROLE_" + type;
            Role defaultRole = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("默认角色不存在: " + roleName));
            user.setRole(defaultRole);
        }

        // 设置创建时间与状态
        user.setCreateTime(LocalDateTime.now());
        user.setStatus(1); // 启用状态

        userRepository.save(user);
    }


    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.getUsernameFromToken(token);
            Optional<User> user = userRepository.findByUsername(username);
            return user.isPresent() && jwtUtil.validateToken(token,
                    new CustomUserDetails(user.get()));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User getUserFromToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}