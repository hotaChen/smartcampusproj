package com.example.smartcampus.config;

import com.example.smartcampus.entity.Permission;
import com.example.smartcampus.entity.Role;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.repository.PermissionRepository;
import com.example.smartcampus.repository.RoleRepository;
import com.example.smartcampus.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("开始数据初始化...");

        // 1. 初始化权限
        initPermissions();

        // 2. 初始化角色
        initRoles();

        // 3. 初始化用户
        initUsers();

        logger.info("数据初始化完成");
        logUserStatistics();
    }

    private void initPermissions() {
        long permissionCount = permissionRepository.count();
        logger.info("当前权限数量: {}", permissionCount);

        if (permissionCount == 0) {
            List<Permission> permissions = Arrays.asList(
                    // 用户管理权限
                    new Permission("查看用户", "user:read", "查看用户信息", "用户管理"),
                    new Permission("创建用户", "user:create", "创建新用户", "用户管理"),
                    new Permission("编辑用户", "user:update", "编辑用户信息", "用户管理"),
                    new Permission("删除用户", "user:delete", "删除用户", "用户管理"),

                    // 课程管理权限
                    new Permission("查看课程", "course:read", "查看课程信息", "课程管理"),
                    new Permission("创建课程", "course:create", "创建新课程", "课程管理"),
                    new Permission("编辑课程", "course:update", "编辑课程信息", "课程管理"),
                    new Permission("删除课程", "course:delete", "删除课程", "课程管理"),

                    //选课权限
                    new Permission("选择课程", "courseselection:create", "选择课程", "选课管理"),
                    new Permission("查看所选课程", "courseselection:read", "查看所选课程", "选课管理"),
                    new Permission("取消课程", "courseselection:delete", "取消所选课程", "选课管理"),

                    //课程大纲相关权限
                    new Permission("创建课程大纲", "syllabus:create", "新建课程大纲", "课程大纲管理"),
                    new Permission("查看大纲", "syllabus:read", "查看所选课程大纲", "课程大纲管理"),
                    new Permission("删除大纲", "syllabus:delete", "删除所选课程大纲", "课程大纲管理"),


                    //课程表权限
                    new Permission("查看课程时间表", "timetable:read", "查看课程对应时间表信息", "课程表管理"),
                    new Permission("创建课程表", "timetable:create", "创建新课程时间表", "课程表管理"),
                    new Permission("编辑课程表", "timetable:update", "编辑课程表信息", "课程表管理"),
                    new Permission("删除课程表", "timetable:delete", "删除课程表", "课程表管理"),

                    // 预约管理权限
                    new Permission("查看预约", "appointment:read", "查看预约信息", "预约管理"),
                    new Permission("创建预约", "appointment:create", "创建新预约", "预约管理"),
                    new Permission("审批预约", "appointment:approve", "审批预约", "预约管理"),
                    new Permission("删除预约", "appointment:delete", "删除预约", "预约管理"),

                    // 成绩管理权限
                    new Permission("查看成绩", "grade:read", "查看成绩信息", "成绩管理"),
                    new Permission("录入成绩", "grade:create", "录入成绩", "成绩管理"),
                    new Permission("修改成绩", "grade:update", "修改成绩", "成绩管理"),
                    new Permission("生成成绩单", "grade:report", "生成学生成绩单", "成绩管理"),

                    // 补考管理权限
                    new Permission("查看补考", "makeup:read", "查看补考信息", "补考管理"),
                    new Permission("申请补考", "makeup:apply", "申请补考", "补考管理"),
                    new Permission("审批补考", "makeup:approve", "审批补考申请", "补考管理"),
                    new Permission("录入补考成绩", "makeup:grade", "录入补考成绩", "补考管理"),
                    new Permission("删除补考", "makeup:delete", "删除补考记录", "补考管理"),

                    // 系统管理权限
                    new Permission("系统设置", "system:config", "系统配置", "系统管理"),
                    new Permission("数据备份", "system:backup", "数据备份恢复", "系统管理"),
                    new Permission("日志查看", "system:log", "查看系统日志", "系统管理"),

                    //教师查看课表权限
                    new Permission("教师查看课程表", "teachertimetable:read", "查看教师课程表", "教师课程表管理"),
                    //学生查看课表权限
                    new Permission("学生查看课程表", "studenttimetable:read", "查看学生课程表", "学生课程表管理")
            );

            permissionRepository.saveAll(permissions);
            logger.info("权限初始化完成，共初始化 {} 个权限", permissions.size());
        } else {
            logger.info("权限已存在，跳过初始化");
        }
    }

    private void initRoles() {
        long roleCount = roleRepository.count();
        logger.info("当前角色数量: {}", roleCount);

        if (roleCount == 0) {
            // 获取所有权限
            List<Permission> allPermissions = permissionRepository.findAll();

            // 管理员角色 - 拥有所有权限
            Role adminRole = new Role("ROLE_ADMIN", "系统管理员");
            adminRole.setPermissions(allPermissions);
            roleRepository.save(adminRole);

            // 教师角色 - 拥有课程、预约、成绩、补考相关权限
            List<Permission> teacherPermissions = allPermissions.stream()
                    .filter(p -> p.getModule().equals("课程管理") ||
                            p.getModule().equals("预约管理") ||
                            p.getModule().equals("成绩管理") ||
                            p.getModule().equals("补考管理") ||
                            p.getCode().equals("user:read"))
                    .toList();

            Role teacherRole = new Role("ROLE_TEACHER", "教师");
            teacherRole.setPermissions(teacherPermissions);
            roleRepository.save(teacherRole);

            // 学生角色 - 只有查看权限、生成成绩单权限和申请补考权限
            List<Permission> studentPermissions = allPermissions.stream()
                    .filter(p -> p.getCode().equals("user:read") ||
                            p.getCode().equals("course:read") ||
                            p.getCode().equals("appointment:read") ||
                            p.getCode().equals("grade:read") ||
                            p.getCode().equals("grade:report") ||
                            p.getCode().equals("makeup:read") ||
                            p.getCode().equals("makeup:apply"))
                    .toList();

            Role studentRole = new Role("ROLE_STUDENT", "学生");
            studentRole.setPermissions(studentPermissions);
            roleRepository.save(studentRole);

            logger.info("角色初始化完成");
        } else {
            logger.info("角色已存在，跳过初始化");
        }
    }

    private void initUsers() {
        long userCount = userRepository.count();
        logger.info("当前用户数量: {}", userCount);

        // 初始化管理员用户
        initUser("admin", "admin123", "系统管理员", "ADMIN", "ROLE_ADMIN", null);

        // 初始化教师用户
        initUser("teacher001", "teacher123", "张老师", "TEACHER", "ROLE_TEACHER", "T2023001");

        // 初始化5个学生用户
        for (int i = 1; i <= 5; i++) {
            String username = "student00" + i;
            String realName = i == 1 ? "张三" : "学生" + i;
            String studentId = "2023000" + i;
            initUser(username, "student123", realName, "STUDENT", "ROLE_STUDENT", studentId);
        }

        logger.info("用户初始化完成");
    }

    private void initUser(String username, String password, String realName,
                          String userType, String roleName, String specificId) {
        if (!userRepository.existsByUsername(username)) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("角色不存在: " + roleName));

            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRealName(realName);
            user.setEmail(username + "@campus.com");
            user.setPhone("13800138" + String.format("%03d", (int)(Math.random() * 1000)));
            user.setUserType(userType);

            // 设置特定ID
            if (userType.equals("STUDENT")) {
                user.setStudentId(specificId);
                user.setDepartment("计算机科学与技术学院");
                user.setMajor(Math.random() > 0.5 ? "软件工程" : "计算机科学");
                user.setGrade(2023);
                user.setClassName("软件工程1班");
            } else if (userType.equals("TEACHER")) {
                user.setTeacherId(specificId);
                user.setTitle("副教授");
            }

            user.setRole(role);
            user.setCreateTime(LocalDateTime.now());
            user.setStatus(1);

            userRepository.save(user);
            logger.info("创建用户: {} ({})", username, realName);
        } else {
            logger.info("用户已存在，跳过创建: {}", username);
        }
    }

    private void logUserStatistics() {
        long totalUsers = userRepository.count();
        long adminCount = userRepository.findByUserType("ADMIN").size();
        long teacherCount = userRepository.findByUserType("TEACHER").size();
        long studentCount = userRepository.findByUserType("STUDENT").size();

        logger.info("=== 用户统计 ===");
        logger.info("管理员: {} 人", adminCount);
        logger.info("教师: {} 人", teacherCount);
        logger.info("学生: {} 人", studentCount);
        logger.info("总计: {} 人", totalUsers);

        if (totalUsers != (adminCount + teacherCount + studentCount)) {
            logger.warn("⚠️ 用户统计不一致！可能存在未知类型的用户");
        }

        // 列出所有用户
        List<User> allUsers = userRepository.findAll();
        logger.info("=== 用户列表 ===");
        allUsers.forEach(user -> {
            logger.info("ID: {}, 用户名: {}, 真实姓名: {}, 类型: {}, 状态: {}",
                    user.getId(), user.getUsername(), user.getRealName(),
                    user.getUserType(), user.getStatus() == 1 ? "启用" : "禁用");
        });
    }
}