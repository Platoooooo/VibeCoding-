const api = require('../../request.js');

Page({
  data: {
    announcementList: [],
    page: 1,
    size: 10,
    hasMore: true,
    loading: false,
    loadingMore: false
  },

  onLoad() {
    this.loadAnnouncements();
  },

  onPullDownRefresh() {
    this.setData({ page: 1, hasMore: true });
    this.loadAnnouncements();
    wx.stopPullDownRefresh();
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loadingMore) {
      this.loadMoreAnnouncements();
    }
  },

  async loadAnnouncements() {
    try {
      this.setData({ loading: true });
      const result = await api.getAnnouncementList(this.data.page, this.data.size);
      // 兼容两种格式：content/totalElements 或 list/total
      const announcementList = (result && result.content) || (result && result.list) || [];
      const total = (result && result.totalElements) || (result && result.total) || 0;

      this.setData({
        announcementList: announcementList,
        hasMore: this.data.page * this.data.size < total,
        loading: false
      });
    } catch (err) {
      console.error('加载公告失败:', err);
      this.setData({ loading: false });
    }
  },

  async loadMoreAnnouncements() {
    if (!this.data.hasMore || this.data.loadingMore) return;

    try {
      this.setData({ loadingMore: true });
      const result = await api.getAnnouncementList(this.data.page + 1, this.data.size);
      const announcementList = (result && result.content) || (result && result.list) || [];
      const total = (result && result.totalElements) || (result && result.total) || 0;

      this.setData({
        announcementList: [...this.data.announcementList, ...announcementList],
        page: this.data.page + 1,
        hasMore: this.data.page * this.data.size < total,
        loadingMore: false
      });
    } catch (err) {
      console.error('加载更多公告失败:', err);
      this.setData({ loadingMore: false });
    }
  },

  goToDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/pages/announcement-detail/announcement-detail?id=' + id
    });
  }
});
