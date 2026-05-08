const api = require('../../request.js');

Page({
  data: {
    newsList: [],
    page: 1,
    size: 10,
    hasMore: true,
    loading: false,
    loadingMore: false
  },

  onLoad() {
    this.loadNews();
  },

  onPullDownRefresh() {
    this.setData({ page: 1, hasMore: true });
    this.loadNews();
    wx.stopPullDownRefresh();
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loadingMore) {
      this.loadMoreNews();
    }
  },

  async loadNews() {
    try {
      this.setData({ loading: true });
      const result = await api.getNewsList(this.data.page, this.data.size);
      // 兼容两种格式：content/totalElements 或 list/total
      const newsList = (result && result.content) || (result && result.list) || [];
      const total = (result && result.totalElements) || (result && result.total) || 0;

      this.setData({
        newsList: newsList,
        hasMore: this.data.page * this.data.size < total,
        loading: false
      });
    } catch (err) {
      console.error('加载新闻失败:', err);
      this.setData({ loading: false });
    }
  },

  async loadMoreNews() {
    if (!this.data.hasMore || this.data.loadingMore) return;

    try {
      this.setData({ loadingMore: true });
      const result = await api.getNewsList(this.data.page + 1, this.data.size);
      const newsList = (result && result.content) || (result && result.list) || [];
      const total = (result && result.totalElements) || (result && result.total) || 0;

      this.setData({
        newsList: [...this.data.newsList, ...newsList],
        page: this.data.page + 1,
        hasMore: this.data.page * this.data.size < total,
        loadingMore: false
      });
    } catch (err) {
      console.error('加载更多新闻失败:', err);
      this.setData({ loadingMore: false });
    }
  },

  goToDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/pages/news-detail/news-detail?id=' + id
    });
  }
});
