var config = require('./config.js');

function getBaseUrl() {
  try {
    var app = getApp();
    return (app && app.globalData && app.globalData.baseUrl) || config.baseUrl;
  } catch (e) {
    return config.baseUrl;
  }
}

function request(url, method, data) {
  method = method || 'GET';
  data = data || {};

  return new Promise(function(resolve, reject) {
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
      success: function(res) {
        wx.hideLoading();
        var code = res.data && res.data.code;
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
            title: (res.data && res.data.message) || '请求失败',
            icon: 'none'
          });
          reject(res.data);
        }
      },
      fail: function(err) {
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

function get(url, data) {
  return request(url, 'GET', data);
}

function post(url, data) {
  return request(url, 'POST', data);
}

function getBanners() {
  return get('/api/banner/list');
}

function getCompanyInfo() {
  return get('/api/company/info');
}

function getNewsList(page, size) {
  return get('/api/company/news', { page: page || 1, size: size || 10 });
}

function getAnnouncementList(page, size) {
  return get('/api/company/announcement', { page: page || 1, size: size || 10 });
}

function getCategories() {
  return get('/api/product/categories');
}

function getProductList(params) {
  params = params || {};
  var data = {
    page: params.page || 1,
    size: params.size || 10,
    keyword: params.keyword || '',
    categoryId: params.categoryId || '',
    spec: params.spec || '',
    priceRange: params.priceRange || ''
  };
  return get('/api/product/list', data);
}

function getProductDetail(id) {
  return get('/api/product/detail/' + id);
}

function getContactInfo() {
  return get('/api/contact/info');
}

module.exports = {
  request: request,
  get: get,
  post: post,
  getBanners: getBanners,
  getCompanyInfo: getCompanyInfo,
  getNewsList: getNewsList,
  getAnnouncementList: getAnnouncementList,
  getCategories: getCategories,
  getProductList: getProductList,
  getProductDetail: getProductDetail,
  getContactInfo: getContactInfo,
  getBaseUrl: getBaseUrl
};
