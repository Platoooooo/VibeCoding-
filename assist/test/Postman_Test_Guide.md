# Postman 接口测试指南

## 一、导入测试集合

### 方法1：直接导入文件
1. 打开 Postman
2. 点击左上角 **"Import"** 按钮
3. 选择文件 `Postman_Collection.json`
4. 点击 **"Import"** 完成导入

### 方法2：复制粘贴 JSON
1. 打开 Postman
2. 点击 **"Import"** -> **"Raw text"**
3. 复制 `Postman_Collection.json` 的内容并粘贴
4. 点击 **"Continue"** -> **"Import"**

## 二、配置环境变量

导入后，集合中已包含默认变量：

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `baseUrl` | `http://localhost:8080` | API 基础地址 |
| `token` | (空) | JWT Token（登录后自动填充） |

### 修改 baseUrl

**本地测试**：
```
http://localhost:8080
```

**服务器测试**：
```
http://localhost:8080
```

修改方法：
1. 在 Collection 列表中点击集合名称
2. 点击 **"Variables"** 标签
3. 修改 `baseUrl` 的 **Current Value**

## 三、测试步骤

### 第一步：测试小程序端接口（无需认证）

按顺序测试以下接口：

#### 1. 轮播图
- 请求：**小程序端接口 > 1. 轮播图 > 获取轮播图列表**
- 方法：GET
- 预期响应：
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "image": "/uploads/banner1.jpg",
      "title": "轮播图1",
      "link": "/pages/detail/1",
      "sortOrder": 1,
      "status": 1
    }
  ]
}
```

#### 2. 公司信息
- 请求：**小程序端接口 > 2. 公司信息 > 获取公司简介**
- 方法：GET
- 预期响应：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "companyName": "示例企业",
    "description": "企业简介...",
    "logo": "/uploads/logo.png",
    "phone": "0755-88888888"
  }
}
```

#### 3. 新闻列表
- 请求：**小程序端接口 > 2. 公司信息 > 获取新闻列表**
- 方法：GET
- 预期响应：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "list": [...],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

#### 4. 产品分类
- 请求：**小程序端接口 > 3. 产品 > 获取产品分类**
- 方法：GET

#### 5. 产品列表
- 请求：**小程序端接口 > 3. 产品 > 获取产品列表**
- 方法：GET

#### 6. 联系方式
- 请求：**小程序端接口 > 4. 联系方式 > 获取联系信息**
- 方法：GET

### 第二步：管理端接口测试（需要登录）

#### 1. 管理员登录（必须先执行）

- 请求：**管理端接口 > 0. 登录认证 > 管理员登录**
- 方法：POST
- 请求体：
```json
{
  "username": "admin",
  "password": "admin123"
}
```
- 预期响应：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin"
  }
}
```

**重要**：登录成功后，Token 会自动保存到环境变量，后续请求会自动携带。

#### 2. 测试管理端接口

登录后可以测试以下接口：

- 新增轮播图
- 更新轮播图
- 删除轮播图
- 新增新闻
- 新增产品
- 等等...

## 四、完整测试流程示例

### 测试场景：添加一个产品

1. **先登录**
   - 执行：管理端接口 > 0. 登录认证 > 管理员登录

2. **查看现有分类**
   - 执行：小程序端接口 > 3. 产品 > 获取产品分类
   - 记录一个分类ID，如 `cat001`

3. **上传图片（可选）**
   - 执行：小程序端接口 > 5. 文件上传 > 上传图片
   - 在 **Body** 标签中选择图片文件
   - 记录返回的 URL，如 `/uploads/abc123.jpg`

4. **新增产品**
   - 执行：管理端接口 > 3. 产品管理 > 新增产品
   - 修改请求体中的 `categoryId` 和 `images`
   ```json
   {
     "productNo": "PRD002",
     "name": "测试产品",
     "spec": "规格A",
     "model": "型号X",
     "price": 999.00,
     "categoryId": "cat001",
     "images": "[\"/uploads/abc123.jpg\"]",
     "description": "这是一个测试产品",
     "status": 1
   }
   ```

5. **验证产品是否添加成功**
   - 执行：小程序端接口 > 3. 产品 > 获取产品列表
   - 检查返回数据中是否包含新添加的产品

## 五、常见问题

### 1. 返回 401 未授权
**原因**：Token 无效或过期  
**解决**：重新执行登录接口获取新 Token

### 2. 返回 404 资源不存在
**原因**：接口路径错误或资源ID不存在  
**解决**：检查 URL 和 ID 是否正确

### 3. 返回数据库错误
**原因**：数据库未初始化或连接失败  
**解决**：
- 确认 MySQL 已启动
- 执行 `init.sql` 初始化数据库
- 检查 `application.yml` 中的数据库配置

### 4. 图片上传失败
**原因**：`uploads/` 目录不存在或无权限  
**解决**：
```bash
# Linux/Mac
mkdir -p uploads
chmod 755 uploads

# Windows
# 确保项目根目录有 uploads 文件夹
```

### 5. Token 没有自动保存
**原因**：Postman 脚本执行失败  
**解决**：
- 手动复制登录响应中的 token
- 在环境变量中设置 `token` 的值

## 六、接口清单速查

### 小程序端（无需认证）

| 接口 | 方法 | 路径 |
|------|------|------|
| 获取轮播图 | GET | `/api/banner/list` |
| 获取公司信息 | GET | `/api/company/info` |
| 获取新闻列表 | GET | `/api/company/news?page=1&size=10` |
| 获取公告列表 | GET | `/api/company/announcement?page=1&size=10` |
| 获取产品分类 | GET | `/api/product/categories` |
| 获取产品列表 | GET | `/api/product/list?page=1&size=10` |
| 获取产品详情 | GET | `/api/product/detail/{id}` |
| 获取联系方式 | GET | `/api/contact/info` |
| 上传图片 | POST | `/api/upload/image` |

### 管理端（需要 Token）

| 接口 | 方法 | 路径 |
|------|------|------|
| 管理员登录 | POST | `/admin/login` |
| 新增轮播图 | POST | `/api/admin/banner` |
| 更新轮播图 | PUT | `/api/admin/banner/{id}` |
| 删除轮播图 | DELETE | `/api/admin/banner/{id}` |
| 新增新闻 | POST | `/api/admin/news` |
| 更新新闻 | PUT | `/api/admin/news/{id}` |
| 删除新闻 | DELETE | `/api/admin/news/{id}` |
| 新增产品 | POST | `/api/admin/product` |
| 更新产品 | PUT | `/api/admin/product/{id}` |
| 删除产品 | DELETE | `/api/admin/product/{id}` |
| 新增分类 | POST | `/api/admin/category` |
| 更新分类 | PUT | `/api/admin/category/{id}` |
| 删除分类 | DELETE | `/api/admin/category/{id}` |
| 保存公司信息 | POST | `/api/admin/company` |

## 七、测试数据示例

### 轮播图数据
```json
{
  "image": "/uploads/banner1.jpg",
  "title": "春季促销",
  "link": "/pages/promotion/1",
  "sortOrder": 1,
  "status": 1
}
```

### 新闻数据
```json
{
  "title": "公司年会圆满举行",
  "summary": "2024年公司年会在酒店隆重举行",
  "content": "详细内容...",
  "image": "/uploads/news1.jpg",
  "status": 1
}
```

### 产品数据
```json
{
  "productNo": "PRD001",
  "name": "智能手机",
  "spec": "8GB+256GB",
  "model": "Phone X Pro",
  "price": 3999.00,
  "categoryId": "cat001",
  "images": "[\"/uploads/product1.jpg\", \"/uploads/product2.jpg\"]",
  "description": "高性能智能手机",
  "status": 1
}
```

### 分类数据
```json
{
  "id": "cat001",
  "name": "电子产品",
  "parentId": null,
  "sortOrder": 1,
  "status": 1
}
```

### 公司信息数据
```json
{
  "companyName": "科技有限公司",
  "description": "专注于科技创新...",
  "logo": "/uploads/logo.png",
  "address": "深圳市南山区科技园",
  "phone": "0755-88888888",
  "email": "info@example.com",
  "wechat": "CompanyWechat",
  "latitude": 22.5431,
  "longitude": 114.0579,
  "workdayTime": "周一至周五 09:00 - 18:00",
  "weekendTime": "周六 09:00 - 12:00"
}
```
