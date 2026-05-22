# code-review — 代码审查助手

对当前分支的改动进行代码审查，检查潜在问题和改进点。

## 用法

调用此 skill 时，自动执行以下操作：

1. 运行 `git diff origin/main...HEAD` 获取当前分支所有改动
2. 按以下维度逐文件审查：
   - **安全性**：SQL 注入、XSS、敏感信息泄露、缺少认证授权
   - **空指针风险**：未检查 Optional、未处理 null 的级联调用
   - **事务边界**：@Transactional 注解是否遗漏
   - **分层规范**：Controller 是否直接调用 Repository
   - **API 规范**：返回值是否使用 ApiResponse 包装
3. 汇总输出：
   - 严重问题数（必须修复）
   - 警告数（建议修复）
   - 通过文件数

## 审查清单

- [ ] 所有管理端接口是否有 JWT 认证保护
- [ ] 所有 API 返回值是否用 ApiResponse 包装
- [ ] Service 层写操作是否加 @Transactional
- [ ] 异常是否正确抛出（不由 GlobalExceptionHandler 吞掉）
- [ ] 输入参数是否有基本校验
- [ ] 敏感配置是否通过环境变量注入
