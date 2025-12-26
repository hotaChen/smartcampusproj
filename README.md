# 智慧校园系统

这是一个基于Spring Boot开发的智慧校园管理系统，提供学生管理、教师管理、课程管理、成绩管理、预约服务等综合功能。

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

## 快速开始

### 环境要求

- JDK 17或更高版本
- Maven 3.6或更高版本
- Docker（可选，用于容器化部署）

### 本地运行

1. 克隆项目到本地
2. 在项目根目录执行以下命令：

```bash
# 编译项目
mvn clean package

# 运行项目
mvn spring-boot:run
```

或者直接运行打包后的JAR文件：

```bash
java -jar target/smart-campus-0.0.1-SNAPSHOT.jar
```

### Docker 部署

#### 方式一：使用 Docker Compose（推荐）

```bash
# 构建并启动
docker-compose up --build

# 后台运行
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

#### 方式二：使用 Maven 构建 Docker 镜像

```bash
# 打包并构建 Docker 镜像
mvn clean package -DskipTests docker:build

# 运行容器
docker run -p 8081:8081 smart-campus:0.0.1-SNAPSHOT
```

## 配置说明

### 端口配置

项目的端口配置在 `src/main/resources/application.yml` 文件中：

```yaml
server:
  port: 8081  # 当前使用的端口
```

如需修改端口，可以直接编辑该文件中的 `server.port` 值。

### 数据库配置

项目默认使用H2数据库，配置同样在 `src/main/resources/application.yml` 文件中：

```yaml
spring:
  datasource:
    url: jdbc:h2:~/smartcampus  # 数据库文件位置
    username: sa
    password:
    driver-class-name: org.h2.Driver
```

#### 查看数据库

项目启动后，可以通过以下方式访问数据库：

1. **H2控制台**：
   - 访问 http://localhost:8081/h2-console
   - JDBC URL: `jdbc:h2:~/smartcampus`
   - 用户名: `sa`
   - 密码: (留空)

2. **数据库文件位置**：
   - 数据库文件默认存储在用户主目录下的 `smartcampus.mv.db` 文件中

### 共享数据库配置

本项目支持配置共享H2数据库服务器，方便多设备协同开发。

#### 修改数据库连接

在设备的 `src/main/resources/application.yml` 中修改数据库连接:

```yaml
spring:
  datasource:
    url: jdbc:h2:tcp://172.28.96.1:9092/smartcampus;DB_CLOSE_DELAY=-1;MODE=MYSQL;DATABASE_TO_LOWER=TRUE
    username: sa
    password:
    driver-class-name: org.h2.Driver
```

#### H2 控制台访问

**服务器端访问**

直接访问: http://localhost:8081/h2-console

连接配置:
- Saved Settings: Generic H2(Server)
- JDBC URL: `jdbc:h2:\\172.28.96.1\h2-shared\smartcampus`
- 用户名: `sa`
- 密码: (空)

**客户端访问**

客户端访问 H2 控制台需要修改 application.yml:

```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true
```

然后访问: http://本机IP:8081/h2-console

连接配置:
- JDBC URL: `jdbc:h2:tcp://172.28.96.1:9092/smartcampus`
- 用户名: `sa`
- 密码: (空)

#### 常见问题

**Q1: 连接被拒绝**

*原因*: 防火墙阻止连接

*解决方案*:
1. 在服务器上开放 9092 端口
2. 或临时关闭防火墙（不推荐）

**Q2: 数据库文件被锁定**

*原因*: 多个进程同时访问数据库文件

*解决方案*:
1. 确保只有一个 H2 服务器实例运行
2. 使用 TCP 模式连接（而非直接文件访问）

**Q3: 找不到数据库文件**

*原因*: 共享文件夹路径错误或未共享

*解决方案*:
1. 确认共享文件夹路径正确
2. 确认文件夹已设置为共享
3. 测试从其他设备访问共享文件夹

**Q4: 数据不同步**

*原因*: 客户端使用了本地数据库

*解决方案*:
1. 确认 application.properties 中的 URL 以 `tcp://` 开头
2. 确认 application.yml 中的 URL 也以 `tcp://` 开头
2. 重启 Spring Boot 使配置生效

#### 数据库备份

**方法1: 复制文件**

直接复制共享文件夹中的 `smartcampus.mv.db` 文件。

**方法2: H2 导出**

访问 H2 控制台，执行:

```sql
BACKUP TO 'backup.zip';
```

**方法3: SQL 导出**

```bash
# 导出
java -cp h2*.jar org.h2.tools.Shell -url jdbc:h2:tcp://172.28.96.1:9092/smartcampus -user sa -sql "SCRIPT TO 'backup.sql'"
```

## API文档与测试

### Swagger可视化测试

项目已集成Swagger/OpenAPI 3，提供可视化的API文档和测试界面。

1. **访问Swagger UI**：
   - 启动应用后，访问 http://localhost:8081/swagger-ui.html

2. **API文档JSON格式**：
   - 访问 http://localhost:8081/v3/api-docs

### 使用Swagger进行API测试

1. **用户认证**：
   - 在Swagger UI中找到"认证管理"部分
   - 点击"用户登录"接口
   - 点击"Try it out"
   - 输入用户名和密码（例如：student/password）
   - 执行请求，获取JWT Token

2. **设置认证Token**：
   - 点击页面右上角的"Authorize"按钮
   - 在弹出框中输入获取到的JWT Token（格式：`Bearer <token>`）
   - 点击"Authorize"确认

3. **测试其他API**：
   - 现在可以测试需要认证的API，如成绩管理、课程管理等

## 项目结构

```
src/main/java/com/example/smartcampus/
├── config/          # 配置类（安全、Swagger等）
├── controller/      # 控制器层
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── repository/     # 数据访问层
├── security/       # 安全相关类
├── service/        # 业务逻辑层
└── util/           # 工具类
```

## 模块结构

```
src/main/java/com/example/smartcampus/
├── config/           # 配置类
│   ├── SecurityConfig.java       # Spring Security 配置
│   ├── SwaggerConfig.java        # Swagger/OpenAPI 配置
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

## 主要功能

- 用户认证与授权
- 学生信息管理
- 教师信息管理
- 课程管理
- 成绩管理
- 补考管理
- 预约服务
- 教室管理
- 课程表管理
- 教学大纲管理

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

## 开发指南

### 添加新的API接口

1. 在相应的Controller类中添加新的接口方法
2. 在对应的Service接口和实现类中添加业务逻辑
3. 如需数据库操作，在Repository接口中定义方法
4. 更新Swagger文档注解

### 数据库变更

项目使用JPA的Hibernate，默认配置为`ddl-auto: update`，会自动更新数据库结构。

## 常见问题

### 如何修改端口？

编辑 `src/main/resources/application.yml` 文件，修改 `server.port` 的值。

### 如何重置数据库？

删除数据库文件 `smartcampus.mv.db`，重启应用后会自动创建新的数据库。

### 如何查看SQL日志？

在 `application.yml` 中，以下配置已启用SQL日志：

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
```

SQL语句会在控制台输出。

### 状态码debug

- 405 Method Not Allowed：请求方法不被允许。
   - 检查请求方法是否为POST或PUT，是否与后端接口一致。
- 400 Bad Request：请求参数错误。
   - 检查DTO与Service的Impl中是否有字段类型错误。
- 401 Unauthorized：未认证。
- 403 Forbidden：无权限。
   - token过期了，需要重新登录获取新的token。
- 404 Not Found：资源不存在。
- 500 Internal Server Error：服务器内部错误。

### 字段类型debug

- 字段类型不正确，在`dto`下修改创建的字段
- 在`Impl`中修改操作的方法

## 联系人

如有问题，请联系系统管理员。

## 许可证

本项目采用MIT许可证。
