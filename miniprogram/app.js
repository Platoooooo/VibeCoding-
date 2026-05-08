App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
  },
  globalData: {
    userInfo: null,
    baseUrl: 'http://localhost:8080',
    selectedCategory: null  // 保存选中的分类
  }
})

// 全局获取 baseUrl 的方法
function getBaseUrl() {
  const app = getApp();
  return app.globalData.baseUrl || 'http://localhost:8080';
}

module.exports = {
  getBaseUrl
}
