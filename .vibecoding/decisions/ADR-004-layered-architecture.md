# ADR-004: 分层架构选型 (Controller-Service-Repository)

## 状态

已接受 (2025-01)

## 背景

后端需要清晰的代码组织方式。需要在传统分层架构、DDD 领域驱动设计、CQRS 之间做出选择。

## 决策

**采用经典三层架构 Controller → Service → Repository**，不使用 DDD 或 CQRS。

## 理由

1. **项目规模适配**: 6 实体 CRUD 系统，业务逻辑以查询为主，不需要 DDD 的聚合根/领域事件等复杂模式
2. **团队熟悉度**: Spring Boot 三层架构是最常见的 Java Web 模式，学习成本最低
3. **可测试性**: 每层职责单一，Service 层可独立 Mock Repository 进行单元测试
4. **Spring 生态天然支持**: `@Controller` / `@Service` / `@Repository` 注解开箱即用
5. **快速交付**: 避免过度设计，符合课程作业的时间约束

## 约束

- Controller 严禁直接调用 Repository，必须通过 Service
- Service 层处理业务逻辑（默认值设置、状态管理、缓存标注）
- Repository 仅定义查询方法，不写实现代码（Spring Data JPA 自动生成）
- 全局异常由 `GlobalExceptionHandler` 统一拦截处理

## 后果

- 简单 CRUD 场景下 Service 层可能显得"薄"，但仍保持了一层抽象
- 未来如需引入读写分离（CQRS），需要重构 Service 层
- 所有 SQL 查询依赖 JPA 方法命名约定或 `@Query` 注解，复杂统计查询不够灵活
