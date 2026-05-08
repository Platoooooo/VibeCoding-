const api = require('../../request.js');

Page({
  data: {
    loading: true,
    contactInfo: null,
    markers: [],
    latitude: 39.9042,
    longitude: 116.4074,
    workdayTime: '',
    weekendTime: ''
  },

  onLoad() {
    this.loadContactInfo();
  },

  onShow() {
    // 每次显示页面时刷新数据
  },

  async loadContactInfo() {
    try {
      this.setData({ loading: true });
      
      const contactInfo = await api.getContactInfo();
      
      // 处理工作时间数据
      let workdayTime = '';
      let weekendTime = '';
      
      if (contactInfo.workTime) {
        // 如果是嵌套对象格式
        workdayTime = contactInfo.workTime.workday || '';
        weekendTime = contactInfo.workTime.weekend || '';
      } else {
        // 如果是直接字段格式
        workdayTime = contactInfo.workdayTime || '';
        weekendTime = contactInfo.weekendTime || '';
      }
      
      // 设置地图标记点
      const markers = [];
      if (contactInfo.latitude && contactInfo.longitude) {
        markers.push({
          id: 1,
          latitude: parseFloat(contactInfo.latitude),
          longitude: parseFloat(contactInfo.longitude),
          title: contactInfo.companyName || '公司位置',
          iconPath: '/images/marker.png',
          width: 30,
          height: 30
        });
      }

      this.setData({
        loading: false,
        contactInfo,
        markers,
        latitude: parseFloat(contactInfo.latitude) || 39.9042,
        longitude: parseFloat(contactInfo.longitude) || 116.4074,
        workdayTime,
        weekendTime
      });
    } catch (err) {
      console.error('加载联系信息失败:', err);
      this.setData({ loading: false });
    }
  },

  // 拨打电话
  onCallPhone() {
    const phone = this.data.contactInfo && this.data.contactInfo.phone;
    if (phone) {
      wx.makePhoneCall({
        phoneNumber: phone,
        fail: () => {
          wx.showToast({
            title: '拨打电话失败',
            icon: 'none'
          });
        }
      });
    } else {
      wx.showToast({
        title: '暂无联系电话',
        icon: 'none'
      });
    }
  },

  // 导航到这里
  onNavigate() {
    const contactInfo = this.data.contactInfo || {};
    const latitude = contactInfo.latitude;
    const longitude = contactInfo.longitude;
    const address = contactInfo.address;
    
    if (latitude && longitude) {
      wx.openLocation({
        latitude: parseFloat(latitude),
        longitude: parseFloat(longitude),
        name: contactInfo.companyName || '公司位置',
        address: address || '公司地址',
        scale: 18,
        fail: () => {
          wx.showToast({
            title: '打开地图失败',
            icon: 'none'
          });
        }
      });
    } else {
      wx.showToast({
        title: '暂无位置信息',
        icon: 'none'
      });
    }
  },

  // 复制邮箱
  onCopyEmail() {
    const email = this.data.contactInfo && this.data.contactInfo.email;
    if (email) {
      wx.setClipboardData({
        data: email,
        success: () => {
          wx.showToast({
            title: '邮箱已复制',
            icon: 'success'
          });
        }
      });
    }
  },

  // 复制微信
  onCopyWechat() {
    const wechat = this.data.contactInfo && this.data.contactInfo.wechat;
    if (wechat) {
      wx.setClipboardData({
        data: wechat,
        success: () => {
          wx.showToast({
            title: '微信号已复制',
            icon: 'success'
          });
        }
      });
    }
  }
});
