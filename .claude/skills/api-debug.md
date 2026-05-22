# api-debug — REST 接口调试工具

一键调试所有 REST API 接口，输出响应状态码和耗时。

## 用法

调用此 skill 时，自动执行以下操作：

1. 确认后端服务正在运行 (检查 `localhost:8080`)
2. 依次调用所有公开 API 端点，收集响应状态和耗时
3. 如果提供了 admin token，同时测试管理端接口
4. 汇总输出表格：
   - 端点路径
   - HTTP 方法
   - 响应状态码
   - 响应耗时 (ms)
   - 响应体大小 (bytes)
5. 标注异常端点 (非 2xx 或耗时 > 1000ms)

## 测试端点清单

### 公开接口
- GET /api/banner/list
- GET /api/company/info
- GET /api/company/news?page=1&size=10
- GET /api/company/announcement?page=1&size=10
- GET /api/product/categories
- GET /api/product/list?page=1&size=10
- GET /api/contact/info

### 管理端接口 (需 token)
- POST /api/admin/login
- GET /api/admin/products?page=1&size=10
- GET /api/admin/categories
- GET /api/admin/news?page=1&size=10
- GET /api/admin/announcements?page=1&size=10
- GET /api/admin/banners
