const api = require('../../request.js');

Page({
  data: {
    loading: true,
    banners: [],
    companyInfo: null,
    newsList: [],
    announcementList: []
  },

  onLoad() {
    this.loadData();
  },

  onShow() {
    // 每次显示页面时刷新数据
  },

  onPullDownRefresh() {
    this.loadData();
    wx.stopPullDownRefresh();
  },

  async loadData() {
    try {
      this.setData({ loading: true });
      
      const [banners, companyInfo, newsData, announcementData] = await Promise.all([
        api.getBanners(),
        api.getCompanyInfo(),
        api.getNewsList(1, 3),
        api.getAnnouncementList(1, 3)
      ]);

      // 兼容两种格式：content/totalElements 或 list/total
      const newsList = (newsData && newsData.content) || (newsData && newsData.list) || [];
      const announcementList = (announcementData && announcementData.content) || (announcementData && announcementData.list) || [];

      this.setData({
        loading: false,
        banners: banners || [],
        companyInfo: companyInfo,
        newsList: newsList,
        announcementList: announcementList
      });
    } catch (err) {
      console.error('加载数据失败:', err);
      this.setData({ loading: false });
    }
  },

  // 点击轮播图
  onBannerTap(e) {
    const { link, title } = e.currentTarget.dataset;
    if (link) {
      // 如果有链接，可以跳转或者打开
      wx.showModal({
        title: title || '提示',
        content: '即将跳转到: ' + link,
        showCancel: false
      });
    }
  },

  // 跳转到产品分类
  goToCategory() {
    wx.switchTab({
      url: '/pages/category/category'
    });
  },

  // 跳转到所有产品
  goToProducts() {
    wx.switchTab({
      url: '/pages/products/products'
    });
  },

  // 跳转到联系我们
  goToContact() {
    wx.switchTab({
      url: '/pages/contact/contact'
    });
  },

  // 跳转到新闻列表
  goToNewsList() {
    wx.navigateTo({
      url: '/pages/news-list/news-list'
    });
  },

  // 跳转到公告列表
  goToAnnouncementList() {
    wx.navigateTo({
      url: '/pages/announcement-list/announcement-list'
    });
  },

  // 查看新闻详情
  goToNewsDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/pages/news-detail/news-detail?id=' + id
    });
  },

  // 查看公告详情
  goToAnnouncementDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/pages/announcement-detail/announcement-detail?id=' + id
    });
  },

  // 跳转到公司简介详情
  goToCompanyInfo() {
    wx.navigateTo({
      url: '/pages/company-info/company-info'
    });
  }
});
