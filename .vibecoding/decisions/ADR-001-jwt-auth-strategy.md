# ADR-001: JWT 无状态认证策略

**状态**: 已采纳

**日期**: 2026-05-22

## 背景

管理后台需要保护 `/api/admin/**` 接口，同时小程序端接口保持公开访问。项目已有 `JwtUtil` 工具类和 Spring Security 依赖。

## 决策

- 使用 `JwtAuthenticationFilter` (继承 `OncePerRequestFilter`) 拦截所有请求
- 从 `Authorization: Bearer <token>` 头提取 Token，验证通过后写入 `SecurityContext`
- `/api/admin/login` 公开；`/api/admin/**` 需认证；`/api/**` 和静态资源公开
- 不使用 Session（`SessionCreationPolicy.STATELESS`），Token 24 小时过期

## 替代方案

1. **Session + Cookie 认证** — 不适合小程序场景，且与 REST 风格不一致
2. **OAuth2 / Spring Authorization Server** — 过度设计，6 实体 CRUD 系统无需授权服务器
3. **API Key 简单令牌** — 安全性不如 JWT

## 后果

- 每个请求需验证 JWT 签名，轻微性能开销（可接受）
- Token 泄露后无内置吊销机制，需配合短有效期缓解
- 前端需在请求头中携带 Token
