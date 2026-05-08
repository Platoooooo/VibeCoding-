<<<<<<< HEAD
# VibeCoding实战——自定义轻量级业务系统开发
鑫川针织公司产品展示系统
福软vibecoding课程, 第一阶段作业
=======
# 企业信息展示小程序工程

基于 Spring Boot 后端和微信小程序前端的企业信息展示系统，支持 Docker 容器化部署和微信云托管。

## 项目结构

```
FR_AI_TEST/
├── backend/                    # Spring Boot 后端服务
│   ├── src/main/java/com/company/miniprogram/
│   │   ├── controller/         # REST API 控制器
│   │   ├── service/            # 业务逻辑层
│   │   ├── repository/         # 数据访问层
│   │   ├── model/              # 实体类
│   │   ├── dto/                # 数据传输对象
│   │   ├── config/             # 配置类
│   │   ├── exception/          # 异常处理
│   │   └── util/               # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml     # 开发环境配置
│   │   └── static/             # 静态资源（管理后台）
│   ├── uploads/                # 上传文件存储目录
│   ├── target/                 # 编译输出目录
│   ├── application-prod.yml    # 生产环境配置
│   ├── Dockerfile              # Docker 镜像构建文件
│   ├── cloudbaserc.json        # 微信云托管配置
│   ├── nginx.conf              # Nginx 反向代理配置
│   └── pom.xml                 # Maven 依赖配置
├── miniprogram/                # 微信小程序前端
│   ├── app.js                  # 应用入口
│   ├── app.json                # 应用配置
│   ├── app.wxss                # 全局样式
│   ├── request.js              # API 请求封装
│   ├── utils.wxs               # WXS 工具模块
│   ├── sitemap.json            # sitemap 配置
│   ├── project.config.json     # 项目配置
│   ├── pages/                  # 页面文件
│   │   ├── index/              # 首页
│   │   ├── category/           # 产品分类
│   │   ├── products/           # 所有产品
│   │   ├── contact/            # 联系我们
│   │   ├── product-detail/     # 产品详情
│   │   ├── news-list/          # 新闻列表
│   │   ├── news-detail/        # 新闻详情
│   │   ├── announcement-list/  # 公告列表
│   │   ├── announcement-detail/ # 公告详情
│   │   └── company-info/       # 公司详情
│   └── images/                 # 图片资源
└── README/                     # 辅助脚本
    ├── start/                  # 部署脚本
    │   └── deploy-windows.bat
    └── test/                   # 测试资源
        ├── Postman_Collection.json
        ├── Postman_Test_Guide.md
        └── service-start.vbs
```

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.5
- **语言**: Java 17
- **数据库**: MySQL 5.7+ / 8.0+
- **ORM**: Spring Data JPA + Hibernate
- **安全**: Spring Security + JWT
- **构建工具**: Maven 3.8+
- **容器化**: Docker
- **云托管**: 微信云托管（CloudBase）

### 前端
- **框架**: 微信小程序
- **开发工具**: 微信开发者工具
- **AppID**: `wx77470e2eabd31695`

## 功能模块

### 小程序端
| 页面 | 功能 |
|------|------|
| 首页 | 轮播图、公司信息、新闻资讯、公告、快捷入口 |
| 产品分类 | 产品分类列表及分类下的产品 |
| 所有产品 | 产品搜索、筛选、分页展示、产品详情 |
| 新闻列表/详情 | 新闻资讯列表及详情展示 |
| 公告列表/详情 | 公告列表及详情展示 |
| 公司详情 | 企业详细信息展示 |
| 联系我们 | 联系方式、地图展示、拨打电话、导航 |

### 管理端
| 模块 | 功能 |
|------|------|
| 管理员认证 | JWT Token 登录 |
| 产品管理 | 新增、编辑、删除产品 |
| 分类管理 | 新增、编辑、删除产品分类 |
| 新闻管理 | 新增、编辑、删除新闻 |
| 公告管理 | 新增、编辑、删除公告 |
| 轮播图管理 | 新增、编辑、删除轮播图 |
| 文件上传 | 图片上传 |

## 环境要求

### 后端
- JDK 17 或更高版本
- Maven 3.8+
- MySQL 5.7+ 或 MySQL 8.0+

### 前端
- 微信开发者工具

## 运行说明

### 1. 数据库配置

创建数据库：
```sql
CREATE DATABASE company_miniprogram CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 后端运行

```bash
cd backend

# 修改 src/main/resources/application.yml 中的数据库配置
# 配置 MySQL 连接信息：
#   - 数据库名: company_miniprogram
#   - 用户名: your_username
#   - 密码: your_password

# 编译并启动
mvn spring-boot:run
```

后端启动后访问：
- API 地址: http://localhost:8080
- 管理后台: http://localhost:8080/admin/index.html

### 3. 小程序前端运行

1. 安装微信开发者工具
2. 打开 `miniprogram` 项目目录
3. 设置项目 AppID（可使用测试号）
4. 修改 `app.js` 中的后端地址（如需要）：
   ```javascript
   globalData: {
     baseUrl: 'http://localhost:8080'  // 修改为实际后端地址
   }
   ```
5. 在微信开发者工具中勾选"不校验合法域名"（开发环境）
6. 编译运行

### 4. 默认账号

- **用户名**: `admin`
- **密码**: `admin123`

> 首次登录后请立即修改默认密码！

## 部署说明

### 方式一：Windows 服务器部署

```cmd
# 使用部署脚本（以管理员身份运行）
README\start\deploy-windows.bat

# 或手动部署
cd backend
mvn clean package -DskipTests
java -jar target\wxzz-prod-1.0.0.jar --server.port=8080
```

### 方式二：Docker 部署

```bash
cd backend

# 构建镜像
mvn clean package -DskipTests
docker build -t miniprogram-backend .

# 运行容器
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/company_miniprogram \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  -v ./uploads:/app/uploads \
  miniprogram-backend
```

### 方式三：微信云托管部署

项目已配置 `cloudbaserc.json`，可直接通过微信云托管控制台部署，数据库连接通过 Secret 配置。

### Nginx 反向代理（可选）

项目提供 `nginx.conf` 配置文件，可用于 Nginx 反向代理：
```bash
# 复制配置到 Nginx
sudo cp nginx.conf /etc/nginx/sites-available/miniprogram-backend
sudo ln -s /etc/nginx/sites-available/miniprogram-backend /etc/nginx/sites-enabled/
sudo nginx -t && sudo systemctl restart nginx
```

## 主要配置

### 后端配置

| 配置文件 | 用途 |
|----------|------|
| `application.yml` | 开发环境默认配置 |
| `application-prod.yml` | 生产环境配置（支持环境变量覆盖） |

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| server.port | 8080 | 服务端口 |
| spring.datasource.url | jdbc:mysql://localhost:3306/company_miniprogram | 数据库连接 |
| spring.datasource.username | root | 数据库用户名 |
| spring.datasource.password | root | 数据库密码 |
| jwt.secret | miniprogram-jwt-secret-key-2024... | JWT 密钥 |
| jwt.expiration | 86400000 | Token 有效期（毫秒，24小时） |
| admin.username | admin | 管理员用户名 |
| admin.password | admin123 | 管理员密码 |

### 前端配置 (app.js)

| 配置项 | 说明 |
|--------|------|
| baseUrl | 后端 API 地址，默认为 http://localhost:8080 |

## API 接口

### 小程序端接口
- `GET /api/banner/list` - 获取轮播图
- `GET /api/company/info` - 获取公司信息
- `GET /api/company/news` - 获取新闻列表
- `GET /api/company/announcement` - 获取公告列表
- `GET /api/product/categories` - 获取产品分类
- `GET /api/product/list` - 获取产品列表
- `GET /api/product/detail/{id}` - 获取产品详情
- `GET /api/contact/info` - 获取联系方式

### 管理端接口
- `POST /api/admin/login` - 管理员登录
- 产品、分类、新闻、公告、轮播图的增删改查接口
- `POST /api/upload` - 文件上传

## 数据库表结构

| 表名 | 说明 |
|------|------|
| company_info | 公司基本信息 |
| product_category | 产品分类 |
| product | 产品信息 |
| news | 新闻资讯 |
| announcement | 公告信息 |
| banner | 轮播图 |

## 注意事项

1. **图片路径**: 后端返回的图片路径为相对路径，前端会自动拼接基础 URL
2. **分页加载**: 产品列表使用分页加载，页码从 1 开始
3. **网络请求**: 开发环境下需在微信开发者工具中勾选"不校验合法域名"
4. **地图功能**: 生产环境需要配置地图服务权限
5. **安全提醒**: 生产环境务必修改默认管理员密码和 JWT 密钥
6. **生产配置**: 生产环境建议使用 `application-prod.yml`，通过环境变量覆盖敏感配置

## 许可证

本项目仅供学习和内部使用。
>>>>>>> 304918d (init: 企业信息展示小程序工程)
