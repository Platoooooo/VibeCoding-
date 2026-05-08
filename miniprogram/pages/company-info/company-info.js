const api = require('../../request.js');

Page({
  data: {
    loading: true,
    companyInfo: null
  },

  onLoad() {
    this.loadCompanyInfo();
  },

  async loadCompanyInfo() {
    try {
      this.setData({ loading: true });
      const companyInfo = await api.getCompanyInfo();
      this.setData({
        companyInfo: companyInfo,
        loading: false
      });
    } catch (err) {
      console.error('加载公司信息失败:', err);
      this.setData({ loading: false });
    }
  }
});
