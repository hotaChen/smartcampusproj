package com.example.smartcampus.service.impl;

import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.UserRepository;
import com.example.smartcampus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("UserServiceImpl 初始化完成");
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.info("=== UserServiceImpl.getAllUsers() 开始 ===");

        long count = userRepository.count();
        logger.info("数据库中的用户总数: {}", count);

        List<User> users = userRepository.findAll();
        logger.info("实际查询到的用户数量: {}", users.size());

        if (users.isEmpty()) {
            logger.warn("⚠️ 查询到的用户列表为空！");
        } else {
            logger.info("✅ 成功查询到 {} 个用户", users.size());

            // 记录前几个用户的信息用于调试
            int maxLog = Math.min(users.size(), 5);
            for (int i = 0; i < maxLog; i++) {
                User user = users.get(i);
                logger.info("用户[{}]: ID={}, 用户名={}, 类型={}",
                        i + 1, user.getId(), user.getUsername(), user.getUserType());
            }

            if (users.size() > 5) {
                logger.info("... 还有 {} 个用户", users.size() - 5);
            }
        }

        logger.info("=== UserServiceImpl.getAllUsers() 结束 ===");
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        logger.info("=== UserServiceImpl.getUserById() 开始 ===");
        logger.info("查询用户ID: {}", id);

        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            logger.info("✅ 找到用户: ID={}, 用户名={}, 真实姓名={}",
                    user.getId(), user.getUsername(), user.getRealName());
            return user;
        } else {
            logger.warn("❌ 未找到用户: ID={}", id);
            throw new RuntimeException("用户不存在: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        logger.info("=== UserServiceImpl.getUserByUsername() 开始 ===");
        logger.info("查询用户名: {}", username);

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            logger.info("✅ 找到用户: 用户名={}, 真实姓名={}, 类型={}",
                    user.getUsername(), user.getRealName(), user.getUserType());
        } else {
            logger.warn("❌ 未找到用户: 用户名={}", username);
        }

        return userOpt;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByType(String userType) {
        logger.info("=== UserServiceImpl.getUsersByType() 开始 ===");
        logger.info("查询用户类型: {}", userType);

        List<User> users = userRepository.findByUserType(userType);
        logger.info("找到 {} 个 {} 类型的用户", users.size(), userType);

        if (users.isEmpty()) {
            logger.warn("⚠️ 未找到 {} 类型的用户", userType);
        }

        return users;
    }

    @Override
    public User createUser(User user) {
        logger.info("创建用户: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        logger.info("更新用户: {}", id);
        User existingUser = getUserById(id);
        // 更新逻辑...
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("删除用户: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(Long roleId) {
        logger.info("根据角色ID查询用户: {}", roleId);
        return userRepository.findByRoleId(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        boolean exists = userRepository.existsByUsername(username);
        logger.info("检查用户名是否存在: {} -> {}", username, exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User updateUserStatus(Long id, Integer status) {
        logger.info("更新用户状态: {} -> {}", id, status);
        User user = getUserById(id);
        user.setStatus(status);
        return userRepository.save(user);
    }
}