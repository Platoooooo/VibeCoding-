const api = require('../../request.js');

Page({
  data: {
    loading: true,
    product: null,
    images: [],
    currentImageIndex: 0,
    showImagePreview: false,
    previewImages: [],
    baseUrl: 'http://localhost:8080'
  },

  onLoad(options) {
    const { id } = options;
    // 获取 baseUrl
    const app = getApp();
    const baseUrl = app.globalData.baseUrl || 'http://localhost:8080';
    this.setData({ baseUrl });
    
    if (id) {
      this.loadProductDetail(id, baseUrl);
    } else {
      wx.showToast({
        title: '产品不存在',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  async loadProductDetail(id, baseUrl) {
    try {
      this.setData({ loading: true });
      
      const product = await api.getProductDetail(id);
      // 使用传入的 baseUrl，确保能立即使用
      if (!baseUrl) {
        const app = getApp();
        baseUrl = app.globalData.baseUrl || 'http://localhost:8080';
      }
      
      // 处理图片
      let images = [];
      if (product.images) {
        // 如果是JSON字符串，解析
        try {
          images = JSON.parse(product.images);
        } catch (e) {
          // 如果不是JSON，可能是逗号分隔的字符串
          images = product.images.split(',').filter(img => img.trim());
        }
      }
      
      // 处理特性
      let features = [];
      if (product.features) {
        try {
          features = JSON.parse(product.features);
        } catch (e) {
          features = product.features.split(',').filter(f => f.trim());
        }
      }

      // 准备预览图片（完整的URL）
      const previewImages = images.map(img => baseUrl + img);

      this.setData({
        loading: false,
        product,
        images,
        features,
        previewImages,
        baseUrl
      });
    } catch (err) {
      console.error('加载产品详情失败:', err);
      this.setData({ loading: false });
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  // 图片切换
  onImageChange(e) {
    const { current } = e.detail;
    this.setData({
      currentImageIndex: current
    });
  },

  // 点击图片，预览大图
  onImageTap(e) {
    const { index } = e.currentTarget.dataset;
    if (this.data.previewImages.length > 0) {
      wx.previewImage({
        current: this.data.previewImages[index],
        urls: this.data.previewImages
      });
    }
  },

  // 长按图片保存
  onImageLongPress(e) {
    const { index } = e.currentTarget.dataset;
    const url = this.data.previewImages[index];
    
    wx.showModal({
      title: '保存图片',
      content: '是否保存该图片到相册？',
      success: (res) => {
        if (res.confirm) {
          wx.downloadFile({
            url: url,
            success: (downloadRes) => {
              if (downloadRes.statusCode === 200) {
                wx.saveImageToPhotosAlbum({
                  filePath: downloadRes.tempFilePath,
                  success: () => {
                    wx.showToast({
                      title: '保存成功',
                      icon: 'success'
                    });
                  },
                  fail: () => {
                    wx.showToast({
                      title: '保存失败',
                      icon: 'none'
                    });
                  }
                });
              }
            },
            fail: () => {
              wx.showToast({
                title: '下载失败',
                icon: 'none'
              });
            }
          });
        }
      }
    });
  },

  // 返回上一页
  onBack() {
    wx.navigateBack();
  }
});
