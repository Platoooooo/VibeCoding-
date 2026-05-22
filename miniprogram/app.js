var config = require('./config.js');

App({
  onLaunch() {
    var logs = wx.getStorageSync('logs') || [];
    logs.unshift(Date.now());
    wx.setStorageSync('logs', logs);
  },
  globalData: {
    userInfo: null,
    baseUrl: config.baseUrl,
    selectedCategory: null
  }
});

function getBaseUrl() {
  var app = getApp();
  return app.globalData.baseUrl || config.baseUrl;
}

module.exports = {
  getBaseUrl: getBaseUrl
};
