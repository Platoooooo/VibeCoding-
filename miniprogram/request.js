// API 请求封装模块
// 注意：这里不能直接使用 getApp()，因为模块加载时机可能早于 app 初始化

// 获取 baseUrl 的函数
function getBaseUrl() {
  try {
    const app = getApp();
    return app && app.globalData && app.globalData.baseUrl || 'http://localhost:8080';
  } catch (e) {
    return 'http://localhost:8080';
  }
}

function request(url, method = 'GET', data = {}) {
  return new Promise((resolve, reject) => {
    wx.showLoading({
      title: '加载中...',
      mask: true
    });

    wx.request({
      url: getBaseUrl() + url,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        wx.hideLoading();
        // 修改判断逻辑：支持 code 为 0 或 200 都算成功
        const code = res.data && res.data.code;
        console.log('API响应:', res.data);
        if (code === 200 || code === 0 || code === '0') {
          resolve(res.data.data);
        } else if (code === 401) {
          wx.showToast({
            title: '请先登录',
            icon: 'none'
          });
          reject(res.data);
        } else {
          wx.showToast({
            title: res.data && res.data.message || '请求失败',
            icon: 'none'
          });
          reject(res.data);
        }
      },
      fail(err) {
        wx.hideLoading();
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
}

// GET请求
function get(url, data) {
  return request(url, 'GET', data);
}

// POST请求
function post(url, data) {
  return request(url, 'POST', data);
}

// 获取轮播图
function getBanners() {
  return get('/api/banner/list');
}

// 获取公司信息
function getCompanyInfo() {
  return get('/api/company/info');
}

// 获取新闻列表
function getNewsList(page = 1, size = 10) {
  return get('/api/company/news', { page, size });
}

// 获取公告列表
function getAnnouncementList(page = 1, size = 10) {
  return get('/api/company/announcement', { page, size });
}

// 获取产品分类
function getCategories() {
  return get('/api/product/categories');
}

// 获取产品列表
function getProductList(params = {}) {
  const data = {
    page: params.page || 1,
    size: params.size || 10,
    keyword: params.keyword || '',
    categoryId: params.categoryId || '',
    spec: params.spec || '',
    priceRange: params.priceRange || ''
  };
  return get('/api/product/list', data);
}

// 获取产品详情
function getProductDetail(id) {
  return get('/api/product/detail/' + id);
}

// 获取联系方式
function getContactInfo() {
  return get('/api/contact/info');
}

module.exports = {
  request,
  get,
  post,
  getBanners,
  getCompanyInfo,
  getNewsList,
  getAnnouncementList,
  getCategories,
  getProductList,
  getProductDetail,
  getContactInfo,
  getBaseUrl
};
