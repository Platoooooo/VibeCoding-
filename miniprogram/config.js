// 小程序环境配置
// 开发环境：修改为本地后端地址
// 生产环境：修改为线上后端地址（如 https://api.your-domain.com）

const ENV = {
  development: {
    baseUrl: 'http://localhost:8080'
  },
  production: {
    baseUrl: 'https://api.your-domain.com'  // 部署前修改为实际域名
  }
};

// 切换环境：development | production
const CURRENT_ENV = 'development';

const config = ENV[CURRENT_ENV];

module.exports = config;
