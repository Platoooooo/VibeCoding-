const api = require('../../request.js');

Page({
  data: {
    announcement: null,
    loading: true
  },

  onLoad(options) {
    const { id } = options;
    if (id) {
      this.loadAnnouncementDetail(id);
    } else {
      wx.showToast({
        title: '公告不存在',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  async loadAnnouncementDetail(id) {
    try {
      this.setData({ loading: true });
      
      // 获取公告列表，查找对应ID的公告
      const announcementData = await api.getAnnouncementList(1, 100);
      const announcementList = (announcementData && announcementData.content) || (announcementData && announcementData.list) || [];
      const announcement = announcementList.find(item => item.id == id);

      if (announcement) {
        this.setData({
          loading: false,
          announcement
        });
        // 设置页面标题
        wx.setNavigationBarTitle({
          title: announcement.title
        });
      } else {
        wx.showToast({
          title: '公告不存在',
          icon: 'none'
        });
        wx.navigateBack();
      }
    } catch (err) {
      console.error('加载公告详情失败:', err);
      this.setData({ loading: false });
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
      wx.navigateBack();
    }
  }
});
