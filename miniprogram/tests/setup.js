// Mock WeChat global APIs for test environment
global.wx = {
  showLoading: jest.fn(),
  hideLoading: jest.fn(),
  showToast: jest.fn(),
  request: jest.fn(),
  getStorageSync: jest.fn(function () { return []; }),
  setStorageSync: jest.fn(),
  navigateTo: jest.fn(),
  switchTab: jest.fn()
};

global.getApp = jest.fn(function () {
  return {
    globalData: {
      baseUrl: 'http://localhost:8080',
      userInfo: null
    }
  };
});
