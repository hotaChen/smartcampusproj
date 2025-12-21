# Swagger API 接口测试文档

## 概述

本文档提供了智慧校园系统API接口的详细说明，包括如何使用Swagger进行接口测试。系统已集成Swagger UI，可以通过浏览器直接访问和测试API。

## 访问Swagger UI

启动应用后，在浏览器中访问以下地址：
```
http://localhost:8080/swagger-ui.html
```

## 测试账号

系统初始化了以下测试账号：

| 用户类型 | 用户名 | 密码 | 真实姓名 | 用户ID |
|---------|--------|------|----------|--------|
| 管理员 | admin | admin123 | 系统管理员 | 1 |
| 教师 | teacher001 | teacher123 | 张老师 | 2 |
| 学生 | student001 | student123 | 张三 | 3 |
| 学生 | student002 | student123 | 学生2 | 4 |
| 学生 | student003 | student123 | 学生3 | 5 |
| 学生 | student004 | student123 | 学生4 | 6 |
| 学生 | student005 | student123 | 学生5 | 7 |

## API模块说明

### 1. 认证模块 (Authentication)

#### 登录接口
- **路径**: `/api/auth/login`
- **方法**: POST
- **描述**: 用户登录获取JWT令牌
- **请求参数**:
  ```json
  {
    "username": "admin",
    "password": "admin123",
    "userType": "ADMIN"
  }
  ```
  或者
  ```json
  {
    "username": "teacher001",
    "password": "teacher123",
    "userType": "TEACHER"
  }
  ```
  或者
  ```json
  {
    "username": "student001",
    "password": "student123",
    "userType": "STUDENT"
  }
  ```
- **响应示例**:
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

### 2. 用户管理模块 (User Management)

#### 获取当前用户信息
- **路径**: `/api/users/current`
- **方法**: GET
- **描述**: 获取当前登录用户信息
- **权限**: 需要登录
- **请求头**: `Authorization: Bearer {token}`

#### 获取用户列表
- **路径**: `/api/users`
- **方法**: GET
- **描述**: 获取所有用户列表
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

### 3. 课程管理模块 (Course Management)

#### 获取课程列表
- **路径**: `/api/courses`
- **方法**: GET
- **描述**: 获取所有课程列表
- **权限**: 需要登录
- **请求头**: `Authorization: Bearer {token}`

#### 获取教师课程列表
- **路径**: `/api/courses/teacher`
- **方法**: GET
- **描述**: 获取当前教师教授的课程列表
- **权限**: 教师
- **请求头**: `Authorization: Bearer {token}`

### 4. 预约管理模块 (Appointment Management)

#### 创建预约
- **路径**: `/api/appointments`
- **方法**: POST
- **描述**: 学生创建预约
- **权限**: 学生
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "appointmentTime": "2024-01-15T10:00:00",
    "serviceType": "咨询服务",
    "status": "待确认"
  }
  ```

#### 获取学生预约列表
- **路径**: `/api/appointments/student/{studentId}`
- **方法**: GET
- **描述**: 获取学生的预约列表
- **权限**: 学生、教师、管理员
- **请求头**: `Authorization: Bearer {token}`

### 5. 成绩管理模块 (Grade Management)

#### 获取学生成绩列表
- **路径**: `/api/grades/student/{studentId}`
- **方法**: GET
- **描述**: 获取学生的成绩列表
- **权限**: 学生、教师、管理员
- **请求头**: `Authorization: Bearer {token}`

#### 录入成绩
- **路径**: `/api/grades`
- **方法**: POST
- **描述**: 教师录入学生成绩
- **权限**: 教师
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
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

#### 生成成绩单
- **路径**: `/api/grades/report/{studentId}`
- **方法**: GET
- **描述**: 生成学生成绩单
- **权限**: 学生、教师、管理员
- **请求头**: `Authorization: Bearer {token}`

### 6. 补考管理模块 (Makeup Exam Management)

#### 申请补考
- **路径**: `/api/makeup-exams/apply`
- **方法**: POST
- **描述**: 学生申请补考
- **权限**: 学生
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "originalGradeId": 24,
    "courseCode": "CS101",
    "courseName": "计算机基础",
    "examDate": "2026-02-15T14:00:00",<必须是未来时间>
    "examLocation": "教学楼A101",
    "semester": "2023-2024-1",
    "applyReason": "因病请假未能参加期末考试"
  }
  ```

#### 审批补考申请
- **路径**: `/api/makeup-exams/{makeupExamId}/approve`
- **方法**: PUT
- **描述**: 教师审批补考申请
- **权限**: 教师
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "status": "已批准",
    "approvalRemark": "已核实情况，同意补考"
  }
  ```
  或者
  ```json
  {
    "status": "已拒绝",
    "approvalRemark": "申请理由不充分，拒绝补考"
  }
  ```

#### 录入补考成绩
- **路径**: `/api/makeup-exams/{makeupExamId}/grade`
- **方法**: PUT
- **描述**: 教师录入补考成绩
- **权限**: 教师
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "score": 78.5,
    "gradeLevel": "中等",
    "comment": "补考成绩合格"
  }
  ```

#### 获取学生补考记录
- **路径**: `/api/makeup-exams/student/{studentId}`
- **方法**: GET
- **描述**: 获取学生的补考记录
- **权限**: 学生、教师、管理员
- **请求头**: `Authorization: Bearer {token}`

#### 获取待审批补考申请
- **路径**: `/api/makeup-exams/pending`
- **方法**: GET
- **描述**: 获取所有待审批的补考申请
- **权限**: 教师、管理员
- **请求头**: `Authorization: Bearer {token}`

## 使用Swagger测试接口

1. **登录获取令牌**
   - 打开Swagger UI页面
   - 找到认证模块的登录接口
   - 点击"Try it out"
   - 输入测试账号的用户名和密码
   - 执行请求，复制返回的token值

2. **设置全局认证**
   - 点击页面顶部的"Authorize"按钮
   - 在弹出框中输入`Bearer {token}`（替换{token}为实际token值）
   - 点击"Authorize"确认

3. **测试其他接口**
   - 现在可以测试所有需要认证的接口
   - 根据接口文档填写请求参数
   - 点击"Try it out"执行请求
   - 查看响应结果

## 测试场景示例

### 场景1：学生申请补考
1. 使用学生账号(student001)登录
2. 获取该学生的成绩列表，选择需要补考的课程
3. 使用成绩ID申请补考
4. 查看申请结果

### 场景2：教师审批补考
1. 使用教师账号(teacher001)登录
2. 查看待审批的补考申请列表
3. 选择一个申请进行审批
4. 录入补考成绩

### 场景3：生成成绩单
1. 使用学生账号(student001)登录
2. 生成该学生的成绩单
3. 查看成绩单中的统计信息

## 注意事项

1. 所有需要认证的接口都必须在请求头中携带有效的JWT令牌
2. 学生只能查看和操作自己的数据
3. 教师只能操作自己负责的课程和学生数据
4. 管理员拥有所有权限
5. 某些操作有状态限制，例如只能为"待审核"状态的补考申请进行审批
6. 测试时请遵循业务流程，例如必须先申请补考，然后才能审批和录入成绩

## 常见错误处理

1. **401 Unauthorized**: 未登录或令牌已过期，需要重新登录
2. **403 Forbidden**: 权限不足，当前用户无权执行该操作
3. **400 Bad Request**: 请求参数错误，请检查请求参数是否符合要求
4. **404 Not Found**: 资源不存在，请检查请求路径和参数
5. **500 Internal Server Error**: 服务器内部错误，请检查服务器日志