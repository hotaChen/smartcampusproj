# 智慧校园系统 - 角色权限文档

## 文档概述

本文档描述智慧校园系统中三个角色的权限分配情况。

---

## 角色列表

| 角色代码 | 角色名称 | 角色描述 |
|---------|---------|---------|
| ROLE_ADMIN | 系统管理员 | 拥有系统所有权限，负责系统管理和维护 |
| ROLE_TEACHER | 教师 | 负责课程教学、成绩录入、预约审批等工作 |
| ROLE_STUDENT | 学生 | 普通学生用户，可查看信息、选课、申请补考等 |

---

## 权限模块说明

| 模块名称 | 权限代码 | 权限名称 | 权限描述 |
|---------|---------|---------|---------|
| 用户管理 | user:read | 查看用户 | 查看用户信息 |
| 用户管理 | user:create | 创建用户 | 创建新用户 |
| 用户管理 | user:update | 编辑用户 | 编辑用户信息 |
| 用户管理 | user:delete | 删除用户 | 删除用户 |
| 课程管理 | course:read | 查看课程 | 查看课程信息 |
| 课程管理 | course:create | 创建课程 | 创建新课程 |
| 课程管理 | course:update | 编辑课程 | 编辑课程信息 |
| 课程管理 | course:delete | 删除课程 | 删除课程 |
| 选课管理 | courseselection:create | 选择课程 | 选择课程 |
| 选课管理 | courseselection:read | 查看所选课程 | 查看所选课程 |
| 选课管理 | courseselection:delete | 取消课程 | 取消所选课程 |
| 课程大纲管理 | syllabus:create | 创建课程大纲 | 新建课程大纲 |
| 课程大纲管理 | syllabus:read | 查看大纲 | 查看所选课程大纲 |
| 课程大纲管理 | syllabus:delete | 删除大纲 | 删除所选课程大纲 |
| 课程表管理 | timetable:read | 查看课程时间表 | 查看课程对应时间表信息 |
| 课程表管理 | timetable:create | 创建课程表 | 创建新课程时间表 |
| 课程表管理 | timetable:update | 编辑课程表 | 编辑课程表信息 |
| 课程表管理 | timetable:delete | 删除课程表 | 删除课程表 |
| 预约管理 | appointment:read | 查看预约 | 查看预约信息 |
| 预约管理 | appointment:create | 创建预约 | 创建新预约 |
| 预约管理 | appointment:approve | 审批预约 | 审批预约 |
| 预约管理 | appointment:delete | 删除预约 | 删除预约 |
| 成绩管理 | grade:read | 查看成绩 | 查看成绩信息 |
| 成绩管理 | grade:create | 录入成绩 | 录入成绩 |
| 成绩管理 | grade:update | 修改成绩 | 修改成绩 |
| 成绩管理 | grade:report | 生成成绩单 | 生成学生成绩单 |
| 补考管理 | makeup:read | 查看补考 | 查看补考信息 |
| 补考管理 | makeup:apply | 申请补考 | 申请补考 |
| 补考管理 | makeup:approve | 审批补考 | 审批补考申请 |
| 补考管理 | makeup:grade | 录入补考成绩 | 录入补考成绩 |
| 补考管理 | makeup:delete | 删除补考 | 删除补考记录 |
| 系统管理 | system:config | 系统设置 | 系统配置 |
| 系统管理 | system:backup | 数据备份 | 数据备份恢复 |
| 系统管理 | system:log | 日志查看 | 查看系统日志 |
| 教师课程表管理 | teachertimetable:read | 教师查看课程表 | 查看教师课程表 |
| 学生课程表管理 | studenttimetable:read | 学生查看课程表 | 查看学生课程表 |
| 学费管理 | tuition:read | 查看学费 | 查看学费信息 |
| 学费管理 | tuition:create | 创建学费 | 创建学费记录 |
| 学费管理 | tuition:update | 编辑学费 | 编辑学费信息 |
| 学费管理 | tuition:delete | 删除学费 | 删除学费记录 |
| 缴费记录管理 | payment:read | 查看缴费记录 | 查看缴费记录信息 |
| 缴费记录管理 | payment:create | 创建缴费记录 | 创建缴费记录 |
| 缴费记录管理 | payment:update | 编辑缴费记录 | 编辑缴费记录信息 |
| 缴费记录管理 | payment:delete | 删除缴费记录 | 删除缴费记录 |
| 助学金管理 | financialaid:read | 查看助学金 | 查看助学金信息 |
| 助学金管理 | financialaid:create | 创建助学金 | 创建助学金记录 |
| 助学金管理 | financialaid:update | 编辑助学金 | 编辑助学金信息 |
| 助学金管理 | financialaid:delete | 删除助学金 | 删除助学金记录 |
| 助学金管理 | financialaid:approve | 审批助学金 | 审批助学金申请 |
| 奖学金管理 | scholarship:read | 查看奖学金 | 查看奖学金信息 |
| 奖学金管理 | scholarship:create | 创建奖学金 | 创建奖学金记录 |
| 奖学金管理 | scholarship:update | 编辑奖学金 | 编辑奖学金信息 |
| 奖学金管理 | scholarship:delete | 删除奖学金 | 删除奖学金记录 |
| 奖学金管理 | scholarship:approve | 审批奖学金 | 审批奖学金申请 |

---

## 角色权限分配

### 1. 系统管理员 (ROLE_ADMIN)

**权限范围：** 拥有系统所有权限

| 模块 | 权限 |
|-----|------|
| 用户管理 | ✓ user:read<br>✓ user:create<br>✓ user:update<br>✓ user:delete |
| 课程管理 | ✓ course:read<br>✓ course:create<br>✓ course:update<br>✓ course:delete |
| 选课管理 | ✓ courseselection:create<br>✓ courseselection:read<br>✓ courseselection:delete |
| 课程大纲管理 | ✓ syllabus:create<br>✓ syllabus:read<br>✓ syllabus:delete |
| 课程表管理 | ✓ timetable:read<br>✓ timetable:create<br>✓ timetable:update<br>✓ timetable:delete |
| 预约管理 | ✓ appointment:read<br>✓ appointment:create<br>✓ appointment:approve<br>✓ appointment:delete |
| 成绩管理 | ✓ grade:read<br>✓ grade:create<br>✓ grade:update<br>✓ grade:report |
| 补考管理 | ✓ makeup:read<br>✓ makeup:apply<br>✓ makeup:approve<br>✓ makeup:grade<br>✓ makeup:delete |
| 系统管理 | ✓ system:config<br>✓ system:backup<br>✓ system:log |
| 教师课程表管理 | ✓ teachertimetable:read |
| 学生课程表管理 | ✓ studenttimetable:read |
| 学费管理 | ✓ tuition:read<br>✓ tuition:create<br>✓ tuition:update<br>✓ tuition:delete |
| 缴费记录管理 | ✓ payment:read<br>✓ payment:create<br>✓ payment:update<br>✓ payment:delete |
| 助学金管理 | ✓ financialaid:read<br>✓ financialaid:create<br>✓ financialaid:update<br>✓ financialaid:delete<br>✓ financialaid:approve |
| 奖学金管理 | ✓ scholarship:read<br>✓ scholarship:create<br>✓ scholarship:update<br>✓ scholarship:delete<br>✓ scholarship:approve |

---

### 2. 教师 (ROLE_TEACHER)

**权限范围：** 课程管理、预约管理、成绩管理、补考管理、查看用户

| 模块 | 权限 |
|-----|------|
| 用户管理 | ✓ user:read |
| 课程管理 | ✓ course:read<br>✓ course:create<br>✓ course:update<br>✓ course:delete |
| 选课管理 | - |
| 课程大纲管理 | - |
| 课程表管理 | - |
| 预约管理 | ✓ appointment:read<br>✓ appointment:create<br>✓ appointment:approve<br>✓ appointment:delete |
| 成绩管理 | ✓ grade:read<br>✓ grade:create<br>✓ grade:update<br>✓ grade:report |
| 补考管理 | ✓ makeup:read<br>✓ makeup:apply<br>✓ makeup:approve<br>✓ makeup:grade<br>✓ makeup:delete |
| 系统管理 | - |
| 教师课程表管理 | - |
| 学生课程表管理 | - |
| 学费管理 | ✓ tuition:read |
| 缴费记录管理 | ✓ payment:read |
| 助学金管理 | ✓ financialaid:read<br>✓ financialaid:approve |
| 奖学金管理 | ✓ scholarship:read<br>✓ scholarship:approve |

---

### 3. 学生 (ROLE_STUDENT)

**权限范围：** 查看信息、生成成绩单、申请补考

| 模块 | 权限 |
|-----|------|
| 用户管理 | ✓ user:read |
| 课程管理 | ✓ course:read |
| 选课管理 | - |
| 课程大纲管理 | - |
| 课程表管理 | - |
| 预约管理 | ✓ appointment:read |
| 成绩管理 | ✓ grade:read<br>✓ grade:report |
| 补考管理 | ✓ makeup:read<br>✓ makeup:apply |
| 系统管理 | - |
| 教师课程表管理 | - |
| 学生课程表管理 | - |
| 学费管理 | ✓ tuition:read |
| 缴费记录管理 | ✓ payment:read |
| 助学金管理 | ✓ financialaid:read |
| 奖学金管理 | ✓ scholarship:read |

---

## 权限对比表

| 权限代码 | 权限名称 | 管理员 | 教师 | 学生 |
|---------|---------|-------|------|------|
| user:read | 查看用户 | ✓ | ✓ | ✓ |
| user:create | 创建用户 | ✓ | - | - |
| user:update | 编辑用户 | ✓ | - | - |
| user:delete | 删除用户 | ✓ | - | - |
| course:read | 查看课程 | ✓ | ✓ | ✓ |
| course:create | 创建课程 | ✓ | ✓ | - |
| course:update | 编辑课程 | ✓ | ✓ | - |
| course:delete | 删除课程 | ✓ | ✓ | - |
| courseselection:create | 选择课程 | ✓ | - | - |
| courseselection:read | 查看所选课程 | ✓ | - | - |
| courseselection:delete | 取消课程 | ✓ | - | - |
| syllabus:create | 创建课程大纲 | ✓ | - | - |
| syllabus:read | 查看大纲 | ✓ | - | - |
| syllabus:delete | 删除大纲 | ✓ | - | - |
| timetable:read | 查看课程时间表 | ✓ | - | - |
| timetable:create | 创建课程表 | ✓ | - | - |
| timetable:update | 编辑课程表 | ✓ | - | - |
| timetable:delete | 删除课程表 | ✓ | - | - |
| appointment:read | 查看预约 | ✓ | ✓ | ✓ |
| appointment:create | 创建预约 | ✓ | ✓ | - |
| appointment:approve | 审批预约 | ✓ | ✓ | - |
| appointment:delete | 删除预约 | ✓ | ✓ | - |
| grade:read | 查看成绩 | ✓ | ✓ | ✓ |
| grade:create | 录入成绩 | ✓ | ✓ | - |
| grade:update | 修改成绩 | ✓ | ✓ | - |
| grade:report | 生成成绩单 | ✓ | ✓ | ✓ |
| makeup:read | 查看补考 | ✓ | ✓ | ✓ |
| makeup:apply | 申请补考 | ✓ | ✓ | ✓ |
| makeup:approve | 审批补考 | ✓ | ✓ | - |
| makeup:grade | 录入补考成绩 | ✓ | ✓ | - |
| makeup:delete | 删除补考 | ✓ | ✓ | - |
| system:config | 系统设置 | ✓ | - | - |
| system:backup | 数据备份 | ✓ | - | - |
| system:log | 日志查看 | ✓ | - | - |
| teachertimetable:read | 教师查看课程表 | ✓ | - | - |
| studenttimetable:read | 学生查看课程表 | ✓ | - | - |
| tuition:read | 查看学费 | ✓ | ✓ | ✓ |
| tuition:create | 创建学费 | ✓ | - | - |
| tuition:update | 编辑学费 | ✓ | - | - |
| tuition:delete | 删除学费 | ✓ | - | - |
| payment:read | 查看缴费记录 | ✓ | ✓ | ✓ |
| payment:create | 创建缴费记录 | ✓ | - | - |
| payment:update | 编辑缴费记录 | ✓ | - | - |
| payment:delete | 删除缴费记录 | ✓ | - | - |
| financialaid:read | 查看助学金 | ✓ | ✓ | ✓ |
| financialaid:create | 创建助学金 | ✓ | - | - |
| financialaid:update | 编辑助学金 | ✓ | - | - |
| financialaid:delete | 删除助学金 | ✓ | - | - |
| financialaid:approve | 审批助学金 | ✓ | ✓ | - |
| scholarship:read | 查看奖学金 | ✓ | ✓ | ✓ |
| scholarship:create | 创建奖学金 | ✓ | - | - |
| scholarship:update | 编辑奖学金 | ✓ | - | - |
| scholarship:delete | 删除奖学金 | ✓ | - | - |
| scholarship:approve | 审批奖学金 | ✓ | ✓ | - |

---

## API 端点权限配置

### 公开接口 (无需认证)
- `/api/public/**` - 公开API
- `/api/auth/**` - 认证相关
- `/api/test/**` - 测试接口
- `/api/debug/**` - 调试接口

### 需要认证的接口
- `/api/users/**` - 用户管理
- `/api/appointments/**` - 预约管理
- `/api/classrooms/**` - 教室管理
- `/api/courses/**` - 课程管理
- `/api/timetables/**` - 课程表管理
- `/api/tuitions/**` - 学费管理
- `/api/payment-records/**` - 缴费记录管理
- `/api/financial-aid/**` - 助学金管理
- `/api/scholarships/**` - 奖学金管理

### 基于角色的接口
- `/api/admin/**` - 仅管理员可访问
- `/api/teacher/**` - 教师和管理员可访问
- `/api/student/**` - 学生、教师和管理员可访问

---

## 默认用户账号

| 用户名 | 密码 | 角色 | 真实姓名 |
|-------|------|------|---------|
| admin | admin123 | ROLE_ADMIN | 系统管理员 |
| teacher001 | teacher123 | ROLE_TEACHER | 张老师 |
| student001 | student123 | ROLE_STUDENT | 张三 |
| student002 | student123 | ROLE_STUDENT | 学生2 |
| student003 | student123 | ROLE_STUDENT | 学生3 |
| student004 | student123 | ROLE_STUDENT | 学生4 |
| student005 | student123 | ROLE_STUDENT | 学生5 |

---

## 更新日志

| 日期 | 版本 | 更新内容 |
|-----|------|---------|
| 2025-12-23 | 1.0 | 初始版本，定义三个角色及其权限 |
