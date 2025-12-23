# 智慧校园系统 API 接口文档

## 目录
1. [概述](#概述)
2. [认证方式](#认证方式)
3. [测试账号](#测试账号)
4. [接口模块](#接口模块)
   - [1. 认证与用户管理模块](#1-认证与用户管理模块)
   - [2. 课程管理模块](#2-课程管理模块)
   - [3. 成绩与考试模块](#3-成绩与考试模块)
   - [4. 课表与教室模块](#4-课表与教室模块)
   - [5. 财务管理模块](#5-财务管理模块)
   - [6. 系统管理模块](#6-系统管理模块)
   - [7. 校园服务模块](#7-校园服务模块)

## 概述


**基础URL**: `http://localhost:8081/api`

**接口版本**: v1.0

**响应格式**: JSON

## 认证方式

系统使用JWT（JSON Web Token）进行身份认证。

### 获取Token

通过登录接口获取JWT Token：

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "userType": "ADMIN"
}
```

### 使用Token

在需要认证的接口请求头中携带Token：

```http
Authorization: Bearer <your_jwt_token>
```

### Token有效期

Token有效期为24小时，过期后需要重新登录获取新Token。

## 测试账号

| 用户类型 | 用户名 | 密码 | 真实姓名 | 用户ID |
|---------|--------|------|----------|--------|
| 管理员 | admin | admin123 | 系统管理员 | 1 |
| 教师 | teacher001 | teacher123 | 张老师 | 2 |
| 学生 | student001 | student123 | 张三 | 3 |
| 学生 | student002 | student123 | 学生2 | 4 |
| 学生 | student003 | student123 | 学生3 | 5 |
| 学生 | student004 | student123 | 学生4 | 6 |
| 学生 | student005 | student123 | 学生5 | 7 |

---

## 1. 认证与用户管理模块

### 1.1 认证管理 (AuthController)

#### 用户登录
- **接口路径**: `/api/auth/login`
- **请求方法**: POST
- **权限**: 公开
- **描述**: 用户登录获取JWT Token

**请求参数**:
```json
{
  "username": "string",
  "password": "string",
  "userType": "ADMIN|TEACHER|STUDENT"
}
```

**响应示例**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "username": "admin",
  "realName": "系统管理员",
  "userType": "ADMIN",
  "role": "ROLE_ADMIN",
  "message": "登录成功"
}
```

#### 用户注册
- **接口路径**: `/api/auth/register`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 新用户注册

**请求参数**:
```json
{
  "username": "string",
  "password": "string",
  "realName": "string",
  "email": "string",
  "phone": "string",
  "userType": "STUDENT|TEACHER|ADMIN"
}
```

#### 用户登出
- **接口路径**: `/api/auth/logout`
- **请求方法**: POST
- **权限**: 需要登录
- **描述**: 用户登出

#### 获取当前用户信息
- **接口路径**: `/api/auth/me`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取当前登录用户信息

### 1.2 用户管理 (UserController)

#### 获取所有用户
- **接口路径**: `/api/users`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取系统中所有用户信息

#### 根据ID获取用户
- **接口路径**: `/api/users/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据用户ID获取用户详细信息

#### 创建用户
- **接口路径**: `/api/users`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 创建新用户

**请求参数**:
```json
{
  "username": "string",
  "password": "string",
  "realName": "string",
  "email": "string",
  "phone": "string",
  "userType": "STUDENT|TEACHER|ADMIN"
}
```

#### 更新用户
- **接口路径**: `/api/users/{id}`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 更新用户信息

#### 删除用户
- **接口路径**: `/api/users/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除用户

#### 根据用户名获取用户
- **接口路径**: `/api/users/username/{username}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据用户名获取用户信息

#### 根据学号获取用户
- **接口路径**: `/api/users/student/{studentId}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据学号获取学生用户信息

### 1.3 公开接口 (PublicController)

#### 获取欢迎信息
- **接口路径**: `/api/public/welcome`
- **请求方法**: GET
- **权限**: 公开
- **描述**: 获取系统欢迎信息

**响应示例**:
```json
{
  "message": "欢迎使用智能校园管理系统",
  "version": "1.0.0",
  "timestamp": 1734950400000
}
```

#### 获取测试账号信息
- **接口路径**: `/api/public/test-users`
- **请求方法**: GET
- **权限**: 公开
- **描述**: 获取测试用的账号信息

**响应示例**:
```json
{
  "admin": {
    "username": "admin",
    "password": "admin123",
    "role": "ROLE_ADMIN",
    "description": "系统管理员"
  },
  "teacher": {
    "username": "teacher001",
    "password": "teacher123",
    "role": "ROLE_TEACHER",
    "description": "测试教师"
  },
  "student": {
    "username": "student001",
    "password": "student123",
    "role": "ROLE_STUDENT",
    "description": "测试学生"
  }
}
```

### 1.4 认证测试接口 (AuthTestController)

#### 认证状态检查
- **接口路径**: `/api/test/auth-status`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 检查当前请求的认证状态

#### 公开测试接口
- **接口路径**: `/api/test/public-test`
- **请求方法**: GET
- **权限**: 公开
- **描述**: 无需认证的测试接口

#### 受保护测试接口
- **接口路径**: `/api/test/protected-test`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 需要认证的测试接口

#### SecurityContext详情
- **接口路径**: `/api/test/security-context`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取SecurityContext的详细信息

#### 直接验证Token
- **接口路径**: `/api/test/validate-token-direct`
- **请求方法**: POST
- **权限**: 需要登录
- **描述**: 直接测试JWT Token验证

#### JWT调试信息
- **接口路径**: `/api/test/jwt-debug`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取JWT相关调试信息

### 1.5 认证调试接口 (AuthDebugController)

#### 检查用户数据
- **接口路径**: `/api/debug/auth/check-users`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 检查数据库中的用户数据

#### 测试登录
- **接口路径**: `/api/debug/auth/test-login`
- **请求方法**: POST
- **权限**: 需要登录
- **描述**: 直接测试登录逻辑

**请求参数**:
```json
{
  "username": "string",
  "password": "string"
}
```

#### 检查密码
- **接口路径**: `/api/debug/auth/check-password`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 检查指定用户的密码

**请求参数**:
- `username` (query): 用户名
- `password` (query): 密码

### 1.6 调试接口 (DebugController)

#### 调试接口
- **接口路径**: `/__debug`
- **请求方法**: GET
- **权限**: 公开
- **描述**: 系统调试接口

---

## 2. 课程管理模块

### 2.1 课程管理 (CourseController)

#### 获取所有课程
- **接口路径**: `/api/courses`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取所有课程列表

#### 根据ID获取课程
- **接口路径**: `/api/courses/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据课程ID获取课程详情

#### 创建课程
- **接口路径**: `/api/courses`
- **请求方法**: POST
- **权限**: ADMIN/TEACHER
- **描述**: 创建新课程

**请求参数**:
```json
{
  "courseCode": "string",
  "name": "string",
  "credit": 3,
  "teacherId": 1,
  "classroomId": 1,
  "capacity": 50
}
```

#### 更新课程
- **接口路径**: `/api/courses/{id}`
- **请求方法**: PUT
- **权限**: ADMIN/TEACHER
- **描述**: 更新课程信息

#### 删除课程
- **接口路径**: `/api/courses/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除课程

#### 根据课程代码获取课程
- **接口路径**: `/api/courses/code/{courseCode}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据课程代码获取课程

#### 获取教师的课程
- **接口路径**: `/api/courses/teacher/{teacherId}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取指定教师教授的课程

#### 获取院系的课程
- **接口路径**: `/api/courses/department/{department}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取指定院系的课程

### 2.2 选课管理 (CourseSelectionController)

#### 学生选课
- **接口路径**: `/api/course-selection/enroll`
- **请求方法**: POST
- **权限**: STUDENT
- **描述**: 学生选择课程

**请求参数**:
```json
{
  "courseId": 1,
  "studentId": 1
}
```

#### 学生退课
- **接口路径**: `/api/course-selection/drop/{selectionId}`
- **请求方法**: DELETE
- **权限**: STUDENT
- **描述**: 学生退选课程

#### 获取我的选课
- **接口路径**: `/api/course-selection/my-courses`
- **请求方法**: GET
- **权限**: STUDENT
- **描述**: 获取当前学生的选课列表

#### 获取学生选课
- **接口路径**: `/api/course-selection/student/{studentId}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取指定学生的选课列表

#### 获取课程选课学生
- **接口路径**: `/api/course-selection/course/{courseId}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取选择指定课程的学生列表

#### 获取教师课程选课情况
- **接口路径**: `/api/course-selection/teacher/{teacherId}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取教师教授课程的选课情况

### 2.3 教学大纲管理 (SyllabusController)

#### 创建或更新教学大纲
- **接口路径**: `/api/syllabus/{courseId}`
- **请求方法**: POST
- **权限**: TEACHER/ADMIN
- **描述**: 创建或更新课程教学大纲

**请求参数**:
```json
{
  "content": "string"
}
```

#### 查看教学大纲
- **接口路径**: `/api/syllabus/{courseId}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 查看课程教学大纲

**响应示例**:
```json
{
  "id": 1,
  "courseId": 1,
  "content": "课程大纲内容...",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### 删除教学大纲
- **接口路径**: `/api/syllabus/{courseId}`
- **请求方法**: DELETE
- **权限**: TEACHER/ADMIN
- **描述**: 删除课程教学大纲

---

## 3. 成绩与考试模块

### 3.1 成绩管理 (GradeController)

#### 录入成绩
- **接口路径**: `/api/grades/enter`
- **请求方法**: POST
- **权限**: TEACHER/ADMIN
- **描述**: 教师或管理员录入学生成绩

**请求参数**:
```json
{
  "studentId": 1,
  "courseCode": "CS101",
  "courseName": "计算机基础",
  "examType": "期末考试",
  "score": 85,
  "semester": "2023-2024-1",
  "credit": 3,
  "gradeLevel": "良好",
  "finalScore": 80,
  "midtermScore": 85,
  "regularScore": 90,
  "remark": "表现良好",
  "totalScore": 85,
  "grade": 3.7,
  "comment": "学习态度认真"
}
```

#### 获取学生成绩
- **接口路径**: `/api/grades/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 获取指定学生的成绩列表

#### 获取课程成绩
- **接口路径**: `/api/grades/course/{courseId}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取指定课程的成绩列表

#### 获取学期成绩
- **接口路径**: `/api/grades/semester/{semester}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取指定学期的成绩列表

#### 生成我的成绩单
- **接口路径**: `/api/grades/report/my`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 生成当前登录学生的成绩单

**请求参数**:
- `semester` (query, 可选): 学期

#### 生成学生成绩单
- **接口路径**: `/api/grades/report/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 生成指定学生的成绩单

#### 课程成绩统计
- **接口路径**: `/api/grades/statistics/course/{courseId}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取课程成绩统计信息

#### 学期成绩统计
- **接口路径**: `/api/grades/statistics/semester/{semester}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取学期成绩统计信息

### 3.2 补考管理 (MakeupExamController)

#### 申请补考
- **接口路径**: `/api/makeup-exams/apply`
- **请求方法**: POST
- **权限**: STUDENT
- **描述**: 学生申请补考

**请求参数**:
```json
{
  "originalGradeId": 24,
  "courseCode": "CS101",
  "courseName": "计算机基础",
  "examDate": "2026-02-15T14:00:00",
  "examLocation": "教学楼A101",
  "semester": "2023-2024-1",
  "applyReason": "因病请假未能参加期末考试"
}
```

**注意**: `examDate` 必须是未来时间

#### 审批补考申请
- **接口路径**: `/api/makeup-exams/{makeupExamId}/approve`
- **请求方法**: PUT
- **权限**: TEACHER/ADMIN
- **描述**: 教师审批补考申请

**请求参数**:
```json
{
  "status": "已批准",
  "approvalRemark": "已核实情况，同意补考"
}
```

或

```json
{
  "status": "已拒绝",
  "approvalRemark": "申请理由不充分，拒绝补考"
}
```

#### 录入补考成绩
- **接口路径**: `/api/makeup-exams/{makeupExamId}/grade`
- **请求方法**: PUT
- **权限**: TEACHER/ADMIN
- **描述**: 教师录入补考成绩

**请求参数**:
```json
{
  "score": 78.5,
  "gradeLevel": "中等",
  "comment": "补考成绩合格"
}
```

#### 获取补考记录详情
- **接口路径**: `/api/makeup-exams/{makeupExamId}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 根据补考记录ID获取详情

#### 获取学生补考记录
- **接口路径**: `/api/makeup-exams/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 根据学生ID获取所有补考记录

#### 根据学号获取学生补考记录
- **接口路径**: `/api/makeup-exams/student/number/{studentNumber}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 根据学号获取所有补考记录

#### 获取教师审批的补考记录
- **接口路径**: `/api/makeup-exams/teacher`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取当前教师审批的所有补考记录

#### 获取学生学期补考记录
- **接口路径**: `/api/makeup-exams/student/{studentId}/semester/{semester}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 获取学生在某学期的所有补考记录

#### 根据学号获取学生学期补考记录
- **接口路径**: `/api/makeup-exams/student/number/{studentNumber}/semester/{semester}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 根据学号获取学生在某学期的所有补考记录

#### 获取课程补考记录
- **接口路径**: `/api/makeup-exams/teacher/course/{courseCode}/semester/{semester}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取教师教授的某门课程在某学期的所有补考记录

#### 获取待审批补考申请
- **接口路径**: `/api/makeup-exams/pending`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取所有待审批的补考申请

#### 删除补考记录
- **接口路径**: `/api/makeup-exams/{makeupExamId}`
- **请求方法**: DELETE
- **权限**: TEACHER/ADMIN
- **描述**: 教师或管理员删除补考记录

#### 获取补考记录列表
- **接口路径**: `/api/makeup-exams`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取所有补考记录，支持按学号、课程代码、学期查询

**请求参数**:
- `studentNumber` (query, 可选): 学号
- `courseCode` (query, 可选): 课程代码
- `semester` (query, 可选): 学期

#### 获取我的补考记录
- **接口路径**: `/api/makeup-exams/my`
- **请求方法**: GET
- **权限**: STUDENT
- **描述**: 获取当前登录学生的所有补考记录

---

## 4. 课表与教室模块

### 4.1 课表管理 (TimetableController)

#### 获取所有课表
- **接口路径**: `/api/timetable`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取所有课表

#### 根据ID获取课表
- **接口路径**: `/api/timetable/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据课表ID获取课表详情

#### 创建课表
- **接口路径**: `/api/timetable`
- **请求方法**: POST
- **权限**: ADMIN/TEACHER
- **描述**: 创建新课表

**请求参数**:
```json
{
  "courseCode": "string",
  "courseName": "string",
  "teacherName": "string",
  "classroomName": "string",
  "dayOfWeek": "周一",
  "timeSlot": "08:00-09:35",
  "semester": "2023-2024-1"
}
```

#### 更新课表
- **接口路径**: `/api/timetable/{id}`
- **请求方法**: PUT
- **权限**: ADMIN/TEACHER
- **描述**: 更新课表信息

#### 删除课表
- **接口路径**: `/api/timetable/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除课表

### 4.2 学生课表 (StudentTimetableController)

#### 获取我的课表
- **接口路径**: `/api/student-timetable/my`
- **请求方法**: GET
- **权限**: STUDENT
- **描述**: 获取当前学生的课表

#### 获取学生课表
- **接口路径**: `/api/student-timetable/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 获取指定学生的课表

#### 获取学期课表
- **接口路径**: `/api/student-timetable/semester/{semester}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 获取指定学期的课表

### 4.3 教师课表 (TeacherTimetableController)

#### 获取我的课表
- **接口路径**: `/api/teacher-timetable/my`
- **请求方法**: GET
- **权限**: TEACHER
- **描述**: 获取当前教师的课表

#### 获取教师课表
- **接口路径**: `/api/teacher-timetable/teacher/{teacherId}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取指定教师的课表

#### 获取学期课表
- **接口路径**: `/api/teacher-timetable/semester/{semester}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 获取指定学期的课表

### 4.4 教室管理 (ClassroomController)

#### 获取所有教室
- **接口路径**: `/api/classrooms`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取所有教室列表

#### 根据ID获取教室
- **接口路径**: `/api/classrooms/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据教室ID获取教室详情

#### 创建教室
- **接口路径**: `/api/classrooms`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 创建新教室

**请求参数**:
```json
{
  "name": "string",
  "location": "string",
  "capacity": 50,
  "equipment": "string"
}
```

#### 更新教室
- **接口路径**: `/api/classrooms/{id}`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 更新教室信息

#### 删除教室
- **接口路径**: `/api/classrooms/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除教室

#### 获取可用教室
- **接口路径**: `/api/classrooms/available`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取可用教室列表

#### 获取教学楼教室
- **接口路径**: `/api/classrooms/building/{building}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取指定教学楼的教室

#### 按容量查询教室
- **接口路径**: `/api/classrooms/capacity/{minCapacity}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取容量大于等于指定值的教室

---

## 5. 财务管理模块

### 5.1 学费管理 (TuitionController)

#### 获取所有学费记录
- **接口路径**: `/api/tuition`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取所有学费记录

#### 根据ID获取学费记录
- **接口路径**: `/api/tuition/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据学费记录ID获取详情

#### 创建学费记录
- **接口路径**: `/api/tuition`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 为学生创建学费记录

**请求参数**:
```json
{
  "studentId": 1,
  "amount": 5000.00,
  "semester": "2023-2024学年第一学期",
  "dueDate": "2023-09-30T23:59:59",
  "description": "2023-2024学年第一学期学费"
}
```

#### 更新学费记录
- **接口路径**: `/api/tuition/{id}`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 更新学费记录信息

#### 删除学费记录
- **接口路径**: `/api/tuition/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除学费记录

#### 获取学生学费
- **接口路径**: `/api/tuition/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/ADMIN
- **描述**: 获取指定学生的学费记录

#### 获取学期学费
- **接口路径**: `/api/tuition/semester/{semester}`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取指定学期的学费记录

#### 按状态查询学费
- **接口路径**: `/api/tuition/status/{status}`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 根据状态查询学费记录

### 5.2 缴费记录管理 (PaymentRecordController)

#### 获取所有支付记录
- **接口路径**: `/api/payment-records`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取所有支付记录

#### 根据ID获取支付记录
- **接口路径**: `/api/payment-records/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据支付记录ID获取详情

#### 创建支付记录
- **接口路径**: `/api/payment-records`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 创建学生缴费记录

**请求参数**:
```json
{
  "studentId": 1,
  "amount": 2000.00,
  "paymentType": "学费",
  "paymentMethod": "银行卡",
  "paymentDate": "2023-09-15T10:00:00",
  "semester": "2023-2024学年第一学期",
  "transactionId": "TXN123456789",
  "description": "第一笔学费缴纳",
  "status": 1
}
```

#### 更新支付记录
- **接口路径**: `/api/payment-records/{id}`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 更新支付记录信息

#### 删除支付记录
- **接口路径**: `/api/payment-records/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除支付记录

#### 获取学生支付记录
- **接口路径**: `/api/payment-records/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/ADMIN
- **描述**: 获取指定学生的支付记录

#### 获取学费支付记录
- **接口路径**: `/api/payment-records/tuition/{tuitionId}`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取指定学费的支付记录

### 5.3 奖学金管理 (ScholarshipController)

#### 获取所有奖学金
- **接口路径**: `/api/scholarships`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取所有奖学金记录

#### 根据ID获取奖学金
- **接口路径**: `/api/scholarships/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据奖学金ID获取详情

#### 创建奖学金
- **接口路径**: `/api/scholarships`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 为学生创建奖学金记录

**请求参数**:
```json
{
  "studentId": 1,
  "amount": 2000.00,
  "name": "国家奖学金",
  "type": "国家级",
  "semester": "2023-2024学年第一学期",
  "awardDate": "2023-10-15T10:00:00",
  "description": "学习成绩优秀，综合素质评价高",
  "reason": "学习成绩优秀，综合素质评价高",
  "status": 1
}
```

#### 更新奖学金
- **接口路径**: `/api/scholarships/{id}`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 更新奖学金记录信息

#### 删除奖学金
- **接口路径**: `/api/scholarships/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除奖学金记录

#### 获取学生奖学金
- **接口路径**: `/api/scholarships/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/ADMIN
- **描述**: 获取指定学生的奖学金记录

#### 按类型查询奖学金
- **接口路径**: `/api/scholarships/type/{type}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 根据类型查询奖学金记录

#### 按状态查询奖学金
- **接口路径**: `/api/scholarships/status/{status}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 根据状态查询奖学金记录

### 5.4 助学金管理 (FinancialAidController)

#### 获取所有助学金
- **接口路径**: `/api/financial-aid`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取所有助学金记录

#### 根据ID获取助学金
- **接口路径**: `/api/financial-aid/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据助学金ID获取详情

#### 创建助学金
- **接口路径**: `/api/financial-aid`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 为学生创建助学金记录

**请求参数**:
```json
{
  "studentId": 1,
  "amount": 1500.00,
  "name": "国家助学金",
  "type": "国家级",
  "semester": "2023-2024学年第一学期",
  "awardDate": "2023-10-15T10:00:00",
  "description": "家庭经济困难，学习成绩良好",
  "reason": "家庭经济困难，学习成绩良好",
  "status": 1
}
```

#### 更新助学金
- **接口路径**: `/api/financial-aid/{id}`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 更新助学金记录信息

#### 删除助学金
- **接口路径**: `/api/financial-aid/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除助学金记录

#### 获取学生助学金
- **接口路径**: `/api/financial-aid/student/{studentId}`
- **请求方法**: GET
- **权限**: STUDENT/ADMIN
- **描述**: 获取指定学生的助学金记录

#### 按类型查询助学金
- **接口路径**: `/api/financial-aid/type/{type}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 根据类型查询助学金记录

#### 按状态查询助学金
- **接口路径**: `/api/financial-aid/status/{status}`
- **请求方法**: GET
- **权限**: TEACHER/ADMIN
- **描述**: 根据状态查询助学金记录

---

## 6. 系统管理模块

### 6.1 通知管理 (NotificationController)

#### 获取所有通知
- **接口路径**: `/api/notifications`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取所有通知

#### 根据ID获取通知
- **接口路径**: `/api/notifications/{id}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据通知ID获取详情

#### 创建通知
- **接口路径**: `/api/notifications`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 创建新通知

**请求参数**:
```json
{
  "title": "string",
  "content": "string",
  "type": "string",
  "priority": "string"
}
```

#### 更新通知
- **接口路径**: `/api/notifications/{id}`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 更新通知信息

#### 删除通知
- **接口路径**: `/api/notifications/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除通知

#### 获取我的通知
- **接口路径**: `/api/notifications/my`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取当前用户的通知

#### 获取未读通知
- **接口路径**: `/api/notifications/unread`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取当前用户的未读通知

#### 标记为已读
- **接口路径**: `/api/notifications/{id}/read`
- **请求方法**: PUT
- **权限**: 需要登录
- **描述**: 标记通知为已读

#### 标记所有为已读
- **接口路径**: `/api/notifications/mark-all-read`
- **请求方法**: PUT
- **权限**: 需要登录
- **描述**: 标记所有通知为已读

### 6.2 系统报表管理 (SystemReportController)

#### 获取所有报表（分页）
- **接口路径**: `/api/system-reports`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取所有报表，支持分页

**请求参数**:
- `page` (query, 默认0): 页码
- `size` (query, 默认10): 每页大小

#### 根据报表类型获取
- **接口路径**: `/api/system-reports/type/{reportType}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据报表类型获取报表列表

#### 根据生成者ID获取
- **接口路径**: `/api/system-reports/generated-by/{generatedById}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据生成者ID获取报表列表

#### 根据状态获取
- **接口路径**: `/api/system-reports/status/{status}`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据状态获取报表列表

#### 根据时间范围获取
- **接口路径**: `/api/system-reports/time-range`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据时间范围获取报表列表

**请求参数**:
- `start` (query): 开始时间
- `end` (query): 结束时间

#### 根据时间范围和类型获取
- **接口路径**: `/api/system-reports/time-range-type`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 根据时间范围和类型获取报表列表

**请求参数**:
- `start` (query): 开始时间
- `end` (query): 结束时间
- `reportType` (query): 报表类型

#### 生成新报表
- **接口路径**: `/api/system-reports/generate`
- **请求方法**: POST
- **权限**: 需要登录
- **描述**: 生成新报表

#### 更新报表状态
- **接口路径**: `/api/system-reports/{id}/status`
- **请求方法**: PUT
- **权限**: 需要登录
- **描述**: 更新报表状态

**请求参数**:
- `status` (query): 新状态

#### 获取报表统计信息
- **接口路径**: `/api/system-reports/statistics`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取报表统计信息

#### 获取已完成报表数量
- **接口路径**: `/api/system-reports/count/completed`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 获取已完成报表数量

#### 按报表类型统计数量
- **接口路径**: `/api/system-reports/count/by-type`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 按报表类型统计数量

#### 下载报表文件
- **接口路径**: `/api/system-reports/{id}/download`
- **请求方法**: GET
- **权限**: 需要登录
- **描述**: 下载报表文件

#### 删除报表
- **接口路径**: `/api/system-reports/{id}`
- **请求方法**: DELETE
- **权限**: 需要登录
- **描述**: 删除报表

### 6.3 错误日志管理 (ErrorLogController)

#### 查询错误日志（分页）
- **接口路径**: `/api/error-logs/query`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 查询错误日志，支持分页和条件过滤

**请求参数**:
```json
{
  "module": "string",
  "level": "string",
  "startTime": "2024-01-01T00:00:00",
  "endTime": "2024-12-31T23:59:59",
  "page": 0,
  "size": 20
}
```

#### 根据ID获取错误日志详情
- **接口路径**: `/api/error-logs/{id}`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 根据错误日志ID获取详情

#### 获取错误统计信息
- **接口路径**: `/api/error-logs/statistics`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取错误统计信息

#### 获取模块错误统计
- **接口路径**: `/api/error-logs/statistics/module`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取模块错误统计

#### 获取错误级别统计
- **接口路径**: `/api/error-logs/statistics/level`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取错误级别统计

#### 获取错误趋势
- **接口路径**: `/api/error-logs/statistics/trend`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取错误趋势

**请求参数**:
- `days` (query, 默认7): 天数

#### 删除错误日志
- **接口路径**: `/api/error-logs/{id}`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 删除错误日志

#### 批量删除错误日志
- **接口路径**: `/api/error-logs/batch`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 批量删除错误日志

**请求参数**:
```json
[1, 2, 3, 4, 5]
```

#### 清理过期错误日志
- **接口路径**: `/api/error-logs/cleanup`
- **请求方法**: DELETE
- **权限**: ADMIN
- **描述**: 清理过期错误日志

**请求参数**:
- `beforeTime` (query): 清理此时间之前的日志

#### 手动触发每月清理任务
- **接口路径**: `/api/error-logs/cleanup/monthly`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 手动触发每月清理任务

#### 按天数清理错误日志
- **接口路径**: `/api/error-logs/cleanup/days/{days}`
- **请求方法**: POST
- **权限**: ADMIN
- **描述**: 清理指定天数前的错误日志

#### 获取简单的错误日志列表
- **接口路径**: `/api/error-logs/list`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 获取简单的错误日志列表

**请求参数**:
- `page` (query, 默认0): 页码
- `size` (query, 默认20): 每页大小

#### 根据模块查询错误日志
- **接口路径**: `/api/error-logs/module/{module}`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 根据模块查询错误日志

**请求参数**:
- `page` (query, 默认0): 页码
- `size` (query, 默认20): 每页大小

#### 根据错误级别查询错误日志
- **接口路径**: `/api/error-logs/level/{level}`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 根据错误级别查询错误日志

**请求参数**:
- `page` (query, 默认0): 页码
- `size` (query, 默认20): 每页大小

#### 根据时间范围查询错误日志
- **接口路径**: `/api/error-logs/time-range`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 根据时间范围查询错误日志

**请求参数**:
- `startTime` (query): 开始时间
- `endTime` (query): 结束时间
- `page` (query, 默认0): 页码
- `size` (query, 默认20): 每页大小

---

## 7. 校园服务模块

### 7.1 预约管理 (AppointmentController)

#### 学生创建预约
- **接口路径**: `/api/appointments`
- **请求方法**: POST
- **权限**: STUDENT
- **描述**: 学生创建预约

**请求参数**:
- `serviceType` (query): 服务类型
- `appointmentTime` (query): 预约时间，格式: yyyy-MM-ddTHH:mm

#### 查询我的预约
- **接口路径**: `/api/appointments/mine`
- **请求方法**: GET
- **权限**: STUDENT
- **描述**: 学生查询自己的预约记录

#### 根据学号查询学生预约
- **接口路径**: `/api/appointments/student/number/{studentNumber}`
- **请求方法**: GET
- **权限**: STUDENT/TEACHER/ADMIN
- **描述**: 根据学号查询学生的预约记录

#### 取消预约
- **接口路径**: `/api/appointments/{id}/cancel`
- **请求方法**: PUT
- **权限**: STUDENT
- **描述**: 学生取消自己的预约

#### 管理员查询所有预约
- **接口路径**: `/api/appointments`
- **请求方法**: GET
- **权限**: ADMIN
- **描述**: 管理员查询所有预约记录

#### 管理员审批预约
- **接口路径**: `/api/appointments/{id}/approve`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 管理员审批待处理的预约

#### 管理员完成预约
- **接口路径**: `/api/appointments/{id}/complete`
- **请求方法**: PUT
- **权限**: ADMIN
- **描述**: 管理员标记预约为已完成

---

## 附录

### A. HTTP状态码

| 状态码 | 说明 |
|-------|------|
| 200 | 请求成功 |
| 201 | 创建成功 |
| 400 | 请求参数错误 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### B. 响应格式

#### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

#### 错误响应
```json
{
  "code": 400,
  "message": "错误描述",
  "data": null
}
```

### C. 权限说明

| 角色 | 权限范围 |
|------|---------|
| ADMIN | 所有功能 |
| TEACHER | 课程管理、成绩录入、课表查看、学生信息查询 |
| STUDENT | 个人信息、选课、成绩查询、预约服务 |

### D. 常见问题

**Q: Token过期怎么办？**
A: Token有效期为24小时，过期后需要重新登录获取新Token。

**Q: 如何获取Swagger UI访问地址？**
A: 启动应用后访问 http://localhost:8081/swagger-ui.html

**Q: 接口请求超时怎么办？**
A: 检查网络连接，确认应用服务是否正常运行。

---

**文档版本**: v1.0
**最后更新**: 2024-12-23
**维护者**: 智慧校园系统开发团队
