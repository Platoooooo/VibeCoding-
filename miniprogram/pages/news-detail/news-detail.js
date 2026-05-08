const api = require('../../request.js');

Page({
  data: {
    news: null,
    loading: true
  },

  onLoad(options) {
    const { id } = options;
    if (id) {
      this.loadNewsDetail(id);
    } else {
      wx.showToast({
        title: '新闻不存在',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  async loadNewsDetail(id) {
    try {
      this.setData({ loading: true });
      
      // 获取新闻列表，查找对应ID的新闻
      const newsData = await api.getNewsList(1, 100);
      const newsList = (newsData && newsData.content) || (newsData && newsData.list) || [];
      const news = newsList.find(item => item.id == id);

      if (news) {
        this.setData({
          loading: false,
          news
        });
        // 设置页面标题
        wx.setNavigationBarTitle({
          title: news.title
        });
      } else {
        wx.showToast({
          title: '新闻不存在',
          icon: 'none'
        });
        wx.navigateBack();
      }
    } catch (err) {
      console.error('加载新闻详情失败:', err);
      this.setData({ loading: false });
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  // 预览封面图
  onCoverImageTap() {
    const news = this.data.news || {};
    const coverImage = news.coverImage || news.image;
    if (coverImage) {
      wx.previewImage({
        current: api.BASE_URL + coverImage,
        urls: [api.BASE_URL + coverImage]
      });
    }
  }
});
