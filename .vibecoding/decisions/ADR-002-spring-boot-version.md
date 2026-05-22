# ADR-002: Spring Boot 3.2.5 与 Java 17

**状态**: 已采纳

**日期**: 2026-05-22

## 背景

项目需要选择 Java 框架版本，需权衡稳定性、社区支持和功能需求。

## 决策

- Spring Boot 3.2.5 + Java 17
- jakarta 命名空间（非 javax）
- jjwt 0.12.5（兼容 Spring Boot 3.x 的 Jakarta EE）

## 替代方案

1. **Spring Boot 2.7.x + Java 11** — 即将停止维护，不选
2. **Spring Boot 3.3.x / Java 21** — Virutal Threads 诱人但生态兼容性仍需验证

## 后果

- 需 Jakarta EE 迁移（`javax.*` → `jakarta.*`），依赖库必须支持 Jakarta
- Java 17 的 Records/Sealed Classes 可用于 DTO 简化
