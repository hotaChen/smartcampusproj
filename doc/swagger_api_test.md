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

### 7. 学费管理模块 (Tuition Management)

#### 创建学费记录
- **路径**: `/api/tuitions`
- **方法**: POST
- **描述**: 为学生创建学费记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
    "amount": 5000.00,
    "semester": "2023-2024学年第一学期",
    "dueDate": "2023-09-30T23:59:59",
    "description": "2023-2024学年第一学期学费"
  }
  ```

#### 批量创建学费记录
- **路径**: `/api/tuitions/batch`
- **方法**: POST
- **描述**: 为多个学生批量创建学费记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "semester": "2023-2024学年第一学期",
    "amount": 5000.00,
    "department": "计算机科学与技术学院",
    "grade": 3,
    "dueDate": "2023-09-30T23:59:59",
    "description": "2023-2024学年第一学期学费"
  }
  ```

#### 学费缴费
- **路径**: `/api/tuitions/payment`
- **方法**: POST
- **描述**: 为学生缴纳学费
- **权限**: 管理员、学生
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "tuitionId": 1,
    "paymentAmount": 2000.00,
    "paymentMethod": "银行卡",
    "description": "第一次缴费"
  }
  ```

#### 获取学生学费记录
- **路径**: `/api/tuitions/student/{studentId}`
- **方法**: GET
- **描述**: 获取学生的学费记录列表
- **权限**: 管理员、学生
- **请求头**: `Authorization: Bearer {token}`

#### 获取学期学费记录
- **路径**: `/api/tuitions/semester/{semester}`
- **方法**: GET
- **描述**: 获取指定学期的学费记录列表
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 获取逾期未缴学费记录
- **路径**: `/api/tuitions/overdue`
- **方法**: GET
- **描述**: 获取逾期未缴的学费记录列表
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 获取学费统计信息
- **路径**: `/api/tuitions/statistics/{semester}`
- **方法**: GET
- **描述**: 获取指定学期的学费统计信息
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 获取院系学费统计信息
- **路径**: `/api/tuitions/statistics/department/{department}/{semester}`
- **方法**: GET
- **描述**: 获取指定院系和学期的学费统计信息
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

### 8. 奖学金管理模块 (Scholarship Management)

#### 创建奖学金记录
- **路径**: `/api/scholarships`
- **方法**: POST
- **描述**: 为学生创建奖学金记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
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

#### 更新奖学金记录
- **路径**: `/api/scholarships/{id}`
- **方法**: PUT
- **描述**: 更新奖学金记录信息
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
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

#### 删除奖学金记录
- **路径**: `/api/scholarships/{id}`
- **方法**: DELETE
- **描述**: 删除奖学金记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 根据ID获取奖学金记录
- **路径**: `/api/scholarships/{id}`
- **方法**: GET
- **描述**: 根据ID获取奖学金记录详情
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 获取所有奖学金记录
- **路径**: `/api/scholarships`
- **方法**: GET
- **描述**: 获取系统中所有奖学金记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 根据学生ID获取奖学金记录
- **路径**: `/api/scholarships/student/{studentId}`
- **方法**: GET
- **描述**: 根据学生ID获取奖学金记录列表
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 根据学期获取奖学金记录
- **路径**: `/api/scholarships/semester/{semester}`
- **方法**: GET
- **描述**: 根据学期获取奖学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据状态获取奖学金记录
- **路径**: `/api/scholarships/status/{status}`
- **方法**: GET
- **描述**: 根据状态获取奖学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据类型获取奖学金记录
- **路径**: `/api/scholarships/type/{type}`
- **方法**: GET
- **描述**: 根据类型获取奖学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据学生ID和学期获取奖学金记录
- **路径**: `/api/scholarships/student/{studentId}/semester/{semester}`
- **方法**: GET
- **描述**: 根据学生ID和学期获取奖学金记录列表
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 根据院系和学期获取奖学金记录
- **路径**: `/api/scholarships/department/{department}/semester/{semester}`
- **方法**: GET
- **描述**: 根据院系和学期获取奖学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据专业和学期获取奖学金记录
- **路径**: `/api/scholarships/major/{major}/semester/{semester}`
- **方法**: GET
- **描述**: 根据专业和学期获取奖学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据年级和学期获取奖学金记录
- **路径**: `/api/scholarships/grade/{grade}/semester/{semester}`
- **方法**: GET
- **描述**: 根据年级和学期获取奖学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 获取奖学金统计信息
- **路径**: `/api/scholarships/statistics/{semester}`
- **方法**: GET
- **描述**: 获取指定学期的奖学金统计信息
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 获取院系奖学金统计信息
- **路径**: `/api/scholarships/statistics/department/{department}/{semester}`
- **方法**: GET
- **描述**: 获取指定院系和学期的奖学金统计信息
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

### 9. 助学金管理模块 (Financial Aid Management)

#### 创建助学金记录
- **路径**: `/api/financial-aids`
- **方法**: POST
- **描述**: 为学生创建助学金记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
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

#### 更新助学金记录
- **路径**: `/api/financial-aids/{id}`
- **方法**: PUT
- **描述**: 更新助学金记录信息
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
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

#### 删除助学金记录
- **路径**: `/api/financial-aids/{id}`
- **方法**: DELETE
- **描述**: 删除助学金记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 根据ID获取助学金记录
- **路径**: `/api/financial-aids/{id}`
- **方法**: GET
- **描述**: 根据ID获取助学金记录详情
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 获取所有助学金记录
- **路径**: `/api/financial-aids`
- **方法**: GET
- **描述**: 获取系统中所有助学金记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 根据学生ID获取助学金记录
- **路径**: `/api/financial-aids/student/{studentId}`
- **方法**: GET
- **描述**: 根据学生ID获取助学金记录列表
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 根据学期获取助学金记录
- **路径**: `/api/financial-aids/semester/{semester}`
- **方法**: GET
- **描述**: 根据学期获取助学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据状态获取助学金记录
- **路径**: `/api/financial-aids/status/{status}`
- **方法**: GET
- **描述**: 根据状态获取助学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据类型获取助学金记录
- **路径**: `/api/financial-aids/type/{type}`
- **方法**: GET
- **描述**: 根据类型获取助学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据学生ID和学期获取助学金记录
- **路径**: `/api/financial-aids/student/{studentId}/semester/{semester}`
- **方法**: GET
- **描述**: 根据学生ID和学期获取助学金记录列表
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 根据院系和学期获取助学金记录
- **路径**: `/api/financial-aids/department/{department}/semester/{semester}`
- **方法**: GET
- **描述**: 根据院系和学期获取助学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据专业和学期获取助学金记录
- **路径**: `/api/financial-aids/major/{major}/semester/{semester}`
- **方法**: GET
- **描述**: 根据专业和学期获取助学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据年级和学期获取助学金记录
- **路径**: `/api/financial-aids/grade/{grade}/semester/{semester}`
- **方法**: GET
- **描述**: 根据年级和学期获取助学金记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 获取助学金统计信息
- **路径**: `/api/financial-aids/statistics/{semester}`
- **方法**: GET
- **描述**: 获取指定学期的助学金统计信息
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 获取院系助学金统计信息
- **路径**: `/api/financial-aids/statistics/department/{department}/{semester}`
- **方法**: GET
- **描述**: 获取指定院系和学期的助学金统计信息
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

### 10. 缴费记录管理模块 (Payment Record Management)

#### 创建缴费记录
- **路径**: `/api/payment-records`
- **方法**: POST
- **描述**: 创建学生缴费记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
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

#### 更新缴费记录
- **路径**: `/api/payment-records/{id}`
- **方法**: PUT
- **描述**: 更新缴费记录信息
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  ```json
  {
    "studentId": 3,
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

#### 删除缴费记录
- **路径**: `/api/payment-records/{id}`
- **方法**: DELETE
- **描述**: 删除缴费记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 根据ID获取缴费记录
- **路径**: `/api/payment-records/{id}`
- **方法**: GET
- **描述**: 根据ID获取缴费记录详情
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 获取所有缴费记录
- **路径**: `/api/payment-records`
- **方法**: GET
- **描述**: 获取系统中所有缴费记录
- **权限**: 管理员
- **请求头**: `Authorization: Bearer {token}`

#### 根据学生ID获取缴费记录
- **路径**: `/api/payment-records/student/{studentId}`
- **方法**: GET
- **描述**: 根据学生ID获取缴费记录列表
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 根据学期获取缴费记录
- **路径**: `/api/payment-records/semester/{semester}`
- **方法**: GET
- **描述**: 根据学期获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据状态获取缴费记录
- **路径**: `/api/payment-records/status/{status}`
- **方法**: GET
- **描述**: 根据状态获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据缴费类型获取缴费记录
- **路径**: `/api/payment-records/payment-type/{paymentType}`
- **方法**: GET
- **描述**: 根据缴费类型获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据缴费方式获取缴费记录
- **路径**: `/api/payment-records/payment-method/{paymentMethod}`
- **方法**: GET
- **描述**: 根据缴费方式获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据学生ID和学期获取缴费记录
- **路径**: `/api/payment-records/student/{studentId}/semester/{semester}`
- **方法**: GET
- **描述**: 根据学生ID和学期获取缴费记录列表
- **权限**: 管理员、教师、学生
- **请求头**: `Authorization: Bearer {token}`

#### 根据日期范围获取缴费记录
- **路径**: `/api/payment-records/date-range`
- **方法**: GET
- **描述**: 根据日期范围获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`
- **请求参数**:
  - startDate (query): 开始日期，格式为 yyyy-MM-dd HH:mm:ss
  - endDate (query): 结束日期，格式为 yyyy-MM-dd HH:mm:ss

#### 根据院系和学期获取缴费记录
- **路径**: `/api/payment-records/department/{department}/semester/{semester}`
- **方法**: GET
- **描述**: 根据院系和学期获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据专业和学期获取缴费记录
- **路径**: `/api/payment-records/major/{major}/semester/{semester}`
- **方法**: GET
- **描述**: 根据专业和学期获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 根据年级和学期获取缴费记录
- **路径**: `/api/payment-records/grade/{grade}/semester/{semester}`
- **方法**: GET
- **描述**: 根据年级和学期获取缴费记录列表
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 获取缴费统计信息
- **路径**: `/api/payment-records/statistics/{semester}`
- **方法**: GET
- **描述**: 获取指定学期的缴费统计信息
- **权限**: 管理员、教师
- **请求头**: `Authorization: Bearer {token}`

#### 获取院系缴费统计信息
- **路径**: `/api/payment-records/statistics/department/{department}/{semester}`
- **方法**: GET
- **描述**: 获取指定院系和学期的缴费统计信息
- **权限**: 管理员、教师
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

### 场景4：学费管理
1. 使用管理员账号(admin)登录
2. 创建学费记录，为学生(student001)设置学费
3. 批量创建学费记录，为计算机学院三年级学生设置学费
4. 使用学生账号(student001)登录，查看自己的学费记录
5. 进行学费缴费操作
6. 查看学费统计信息

### 场景5：学费催缴管理
1. 使用管理员账号(admin)登录
2. 查询逾期未缴的学费记录
3. 查看院系学费统计信息
4. 查看学期学费统计信息

### 场景6：奖学金管理
1. 使用管理员账号(admin)登录
2. 为学生(student001)创建国家奖学金记录
3. 更新奖学金记录，修改金额和状态
4. 查看学生的奖学金记录
5. 查看学期奖学金记录
6. 查看院系奖学金统计信息
7. 查看奖学金类型统计信息
8. 使用学生账号(student001)登录，查看自己的奖学金记录

### 场景7：奖学金审批流程
1. 使用管理员账号(admin)登录
2. 为学生(student002)创建待审核状态的奖学金记录
3. 查看待审核状态的奖学金记录
4. 更新奖学金状态为已批准
5. 查看已批准状态的奖学金记录

### 场景8：奖学金统计分析
1. 使用管理员账号(admin)登录
2. 查看学期奖学金统计信息
3. 查看院系奖学金统计信息
4. 查看按类型统计的奖学金信息
5. 查看按专业统计的奖学金信息
6. 查看按年级统计的奖学金信息

### 场景9：助学金管理
1. 使用管理员账号(admin)登录
2. 为学生(student001)创建国家助学金记录
3. 更新助学金记录，修改金额和状态
4. 查看学生的助学金记录
5. 查看学期助学金记录
6. 查看院系助学金统计信息
7. 查看助学金类型统计信息
8. 使用学生账号(student001)登录，查看自己的助学金记录

### 场景10：助学金审批流程
1. 使用管理员账号(admin)登录
2. 为学生(student002)创建待审核状态的助学金记录
3. 查看待审核状态的助学金记录
4. 更新助学金状态为已批准
5. 查看已批准状态的助学金记录

### 场景11：缴费记录管理
1. 使用管理员账号(admin)登录
2. 为学生(student001)创建缴费记录
3. 更新缴费记录，修改金额和状态
4. 查看学生的缴费记录
5. 查看学期缴费记录
6. 查看院系缴费统计信息
7. 查看缴费类型统计信息
8. 使用学生账号(student001)登录，查看自己的缴费记录

### 场景12：缴费记录统计分析
1. 使用管理员账号(admin)登录
2. 查看学期缴费统计信息
3. 查看院系缴费统计信息
4. 查看按缴费类型统计的信息
5. 查看按缴费方式统计的信息
6. 查看按日期范围统计的缴费记录

## 注意事项

1. 所有需要认证的接口都必须在请求头中携带有效的JWT令牌
2. 学生只能查看和操作自己的数据
3. 教师只能操作自己负责的课程和学生数据
4. 管理员拥有所有权限
5. 某些操作有状态限制，例如只能为"待审核"状态的补考申请进行审批
6. 测试时请遵循业务流程，例如必须先申请补考，然后才能审批和录入成绩
7. 学费管理模块中，只有管理员可以创建和批量创建学费记录
8. 学生只能查看自己的学费记录和进行缴费操作
9. 学费缴费金额不能超过未缴金额
10. 批量创建学费记录时，可以根据院系、专业、年级、班级等条件筛选学生
11. 奖学金管理模块中，只有管理员可以创建、更新和删除奖学金记录
12. 学生只能查看自己的奖学金记录
13. 教师可以查看本院系学生的奖学金记录
14. 奖学金状态包括：0-待审核、1-已批准、2-已拒绝
15. 奖学金类型包括：国家级、省级、校级、院级等
16. 奖学金统计信息包括总数、各状态数量、总金额、已批准金额等
17. 助学金状态包括：0-待审核、1-已批准、2-已拒绝
18. 助学金类型包括：国家级、省级、校级、院级等
19. 助学金统计信息包括总数、各状态数量、总金额、已批准金额等
20. 缴费记录状态包括：0-待处理、1-成功、2-失败
21. 缴费类型包括：学费、住宿费、教材费、其他费用等
22. 缴费方式包括：银行卡、支付宝、微信支付、现金等
23. 缴费统计信息包括总数、各状态数量、总金额、成功金额等
24. 助学金管理模块中，只有管理员可以创建、更新和删除助学金记录
25. 学生只能查看自己的助学金记录
26. 教师可以查看本院系学生的助学金记录
27. 缴费记录管理模块中，只有管理员可以创建、更新和删除缴费记录
28. 学生只能查看自己的缴费记录
29. 教师可以查看本院系学生的缴费记录
30. 缴费记录中的交易ID必须唯一，用于关联实际支付系统

## 常见错误处理

1. **401 Unauthorized**: 未登录或令牌已过期，需要重新登录
2. **403 Forbidden**: 权限不足，当前用户无权执行该操作
3. **400 Bad Request**: 请求参数错误，请检查请求参数是否符合要求
4. **404 Not Found**: 资源不存在，请检查请求路径和参数
5. **500 Internal Server Error**: 服务器内部错误，请检查服务器日志