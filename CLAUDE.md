# CLAUDE.md — 企业信息展示小程序工程

## 项目概要

鑫川针织公司产品展示系统，基于 Spring Boot 3.2.5 后端 + 微信小程序前端。支持产品管理、新闻 CMS、公告、Banner 轮播等 6 实体完整 CRUD，Docker 容器化部署。

## 技术栈与版本约束

- **后端**: Java 17, Spring Boot 3.2.5, Spring Data JPA + Hibernate, Spring Security + JWT (jjwt 0.12.5)
- **数据库**: MySQL 8.0+
- **前端**: 微信小程序原生框架
- **构建**: Maven 3.8+
- **部署**: Docker, 微信云托管 (CloudBase)

## 目录结构约定

```
backend/src/main/java/com/company/miniprogram/
├── config/         # SecurityConfig, JwtFilter, WebConfig, DataInitializer
├── controller/     # REST API 控制器 (公有: /api/*, 管理: /api/admin/*)
├── service/        # 业务逻辑层
├── repository/     # Spring Data JPA Repository (数据访问)
├── model/          # JPA 实体类 (@Entity)
├── dto/            # ApiResponse<T>, PageResponse<T> — 统一响应格式
├── exception/      # GlobalExceptionHandler — 全局异常处理
└── util/           # JwtUtil — JWT 令牌工具
```

## 代码规范

### 后端
- 分层架构：Controller → Service → Repository，严禁 Controller 直接调用 Repository
- 所有 API 返回值使用 `ApiResponse<T>` 包装：`ApiResponse.success(data)` / `ApiResponse.error(code, msg)`
- 异常统一由 `GlobalExceptionHandler` 处理，不吞异常
- 实体使用 Lombok `@Data`，Repository 使用 `@Repository`，Service 使用 `@Service`
- 管理端接口统一前缀 `/api/admin/`，公开接口 `/api/`
- 管理端请求需携带 `Authorization: Bearer <token>` 头

### 前端
- 使用 `var` 声明变量（小程序不支持 ES6 `let/const` 的所有特性）
- API 调用统一通过 `request.js` 封装
- 图片 URL 拼接使用 `config.js` 中的 `baseUrl`
- 每页一个目录，包含 `.js` / `.wxml` / `.wxss` / `.json`

## 常用命令

```bash
cd backend
mvn spring-boot:run              # 本地启动 (端口 8080)
mvn clean package -DskipTests    # 编译打包
mvn test                          # 运行测试
java -jar target/wxzz-prod-1.0.0.jar
docker build -t miniprogram-backend .
```

## 数据库

- Hibernate `ddl-auto: update`（开发）/ `validate`（生产）
- 表结构由 JPA 实体自动生成，初始化数据由 `DataInitializer.java` 插入
- 部署前确保 MySQL 已创建 `company_miniprogram` 库 (utf8mb4)

## 安全注意事项

- 生产环境通过环境变量注入所有敏感配置：`SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `ADMIN_PASSWORD`
- JWT 密钥至少 256 位，使用 `openssl rand -base64 32` 生成
- 管理端接口由 `JwtAuthenticationFilter` 保护，公开接口 `permitAll`
- 禁止在代码或配置文件中提交明文密码

## 约束

- 默认使用简体中文进行对话和生成内容，除非用户明确要求其他语言
