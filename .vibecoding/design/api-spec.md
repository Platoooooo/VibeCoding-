# API 规范

## 1. 统一响应格式

所有 API 返回值使用 `ApiResponse<T>` 包装：

```json
{
  "code": 0,
  "message": "success",
  "data": { }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 0 = 成功，-1 = 业务错误，其他正数 = 特定错误码 |
| message | string | 提示信息 |
| data | T / null | 响应数据，错误时为 null |

## 2. 分页响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "list": [ ],
    "total": 100,
    "page": 1,
    "size": 10
  }
}
```

分页参数：`page` 从 1 开始，`size` 默认 10。

## 3. 错误码规范

| 错误码 | 说明 | HTTP 状态码 |
|--------|------|-------------|
| 0 | 成功 | 200 |
| -1 | 通用业务错误 | 400 |
| 401 | 未认证 | 401 |
| 403 | 无权限 | 403 |
| 404 | 资源不存在 | 404 |
| 500 | 服务器内部错误 | 500 |

## 4. REST 端点命名规范

### 公开接口前缀: `/api/`
- GET `/api/{resource}/list` — 分页列表
- GET `/api/{resource}/detail/{id}` — 详情
- GET `/api/{resource}/categories` — 分类列表

### 管理端接口前缀: `/api/admin/`
- POST `/api/admin/login` — 登录
- GET `/api/admin/{resource}s?page=1&size=10` — 分页列表
- POST `/api/admin/{resource}s` — 新增
- PUT `/api/admin/{resource}s/{id}` — 更新
- DELETE `/api/admin/{resource}s/{id}` — 删除

### 文件上传
- POST `/api/upload/image` — 图片上传 (multipart/form-data)

## 5. 认证方式

管理端请求需携带 JWT Token：
```
Authorization: Bearer <token>
```

Token 通过 `/api/admin/login` 获取，有效期 24 小时。

## 6. 文件上传规范

- 请求方式: `multipart/form-data`
- 字段名: `file`
- 允许类型: `image/*`
- 大小限制: 10MB
- 返回格式: `{ url: "/uploads/uuid.jpg", filename: "uuid.jpg" }`
