# Smart Campus Architecture Document

## 项目架构概述

本项目是基于 Spring Boot 3.5.8 的智慧校园管理系统，采用单体架构设计。

## 技术栈

### 后端技术
- **框架**: Spring Boot 3.5.8
- **语言**: Java 21
- **安全**: Spring Security + JWT
- **数据库**: H2 (开发) / MySQL (生产)
- **ORM**: Spring Data JPA
- **API文档**: Springdoc OpenAPI 2.5.0 (Swagger UI)

### 前端技术
- **模板引擎**: Thymeleaf + HTML5
- **样式**: CSS3 + 自定义样式

## 模块结构

```
src/main/java/com/example/smartcampus/
├── config/           # 配置类
│   ├── SecurityConfig.java       # Spring Security 配置
│   ├── SwaggerConfig.java        # Swagger/OpenAPI 配置
│   ├── GlobalExceptionHandler.java  # 全局异常处理器
│   └── ...                       # 其他配置
├── controller/       # REST API 控制器
│   ├── AuthController.java       # 认证接口
│   ├── CourseController.java     # 课程管理
│   ├── GradeController.java      # 成绩管理
│   └── ...                       # 其他控制器
├── dto/              # 数据传输对象
├── entity/           # 实体类
├── repository/       # 数据访问层
├── service/          # 业务逻辑层
└── util/             # 工具类
```

## 部署架构

### 开发环境
- 使用 H2 内存数据库
- 端口: 8081
- 配置文件: application.yml

### 生产环境
- 使用 Docker 容器化部署
- 使用 H2 数据库
- 使用 docker-compose 管理
- 配置文件: application-prod.yml

## 容器化部署

### 部署文件
- **Dockerfile**: 定义应用容器镜像
- **docker-compose.yml**: 定义服务编排

### 部署命令
```bash
# 构建并启动
docker-compose up --build

# 后台运行
docker-compose up -d

# 停止服务
docker-compose down
```

## 财务模块详情

### 学费管理模块
- **前端页面**: `src/main/resources/static/finance/tuition-manage.html`
  - 功能: 学费记录管理，包括添加、查询、删除学费记录
  - 依赖: Thymeleaf模板引擎，finance.css样式

### 奖学金管理模块
- **前端页面**: `src/main/resources/static/finance/scholarship-manage.html`
  - 功能: 奖学金信息管理，包括添加、编辑、删除奖学金记录
  - 依赖: Thymeleaf模板引擎，finance.css样式

### 助学金管理模块
- **前端页面**: `src/main/resources/static/finance/financialaid-manage.html`
  - 功能: 助学金申请与管理，支持根据学生学号查询和添加记录
  - 依赖: Thymeleaf模板引擎，finance.css样式

### 缴费记录模块
- **前端页面**: `src/main/resources/static/finance/payment-records.html`
  - 功能: 缴费记录查询与管理
  - 依赖: Thymeleaf模板引擎，finance.css样式

## API 文档

- Swagger UI: http://localhost:8081/swagger-ui.html
- API Docs: http://localhost:8081/v3/api-docs

```