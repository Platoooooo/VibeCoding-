const api = require('../../request.js');

Page({
  data: {
    categories: [],
    loading: true
  },

  onLoad() {
    this.loadCategories();
  },

  onShow() {
    // 每次显示页面时刷新数据
    this.loadCategories();
  },

  onPullDownRefresh() {
    this.loadCategories();
    wx.stopPullDownRefresh();
  },

  async loadCategories() {
    try {
      wx.showLoading({ title: '加载中...' });
      this.setData({ loading: true });
      
      const categories = await api.getCategories();
      console.log('分类数据:', categories);
      
      // 过滤出启用的分类，并按sortOrder排序
      const activeCategories = (categories || []).filter(cat => cat.active !== false && cat.status !== 0);
      activeCategories.sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0));
      
      console.log('过滤后的分类:', activeCategories);
      
      this.setData({
        categories: activeCategories,
        loading: false
      });

      wx.hideLoading();
    } catch (err) {
      console.error('加载分类失败:', err);
      this.setData({ loading: false });
      wx.hideLoading();
    }
  },

  // 点击分类，跳转到产品列表（使用switchTab因为products是tabBar页面）
  onCategoryTap(e) {
    const { id, name } = e.currentTarget.dataset;
    // 使用 storage 传递参数
    wx.setStorageSync('selectedCategory', { id, name });
    wx.switchTab({
      url: '/pages/products/products'
    });
  }
});
