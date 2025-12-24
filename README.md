# 智慧校园系统

这是一个基于Spring Boot开发的智慧校园管理系统，提供学生管理、教师管理、课程管理、成绩管理、预约服务等综合功能。

## 快速开始

### 环境要求

- JDK 17或更高版本
- Maven 3.6或更高版本

### 编译运行

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

## 许可证

本项目采用MIT许可证。