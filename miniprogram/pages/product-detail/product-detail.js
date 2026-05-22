var api = require('../../request.js');
var config = require('../../config.js');

Page({
  data: {
    loading: true,
    product: null,
    images: [],
    currentImageIndex: 0,
    showImagePreview: false,
    previewImages: [],
    baseUrl: config.baseUrl
  },

  onLoad: function(options) {
    var id = options.id;
    var self = this;
    var app = getApp();
    var baseUrl = (app.globalData && app.globalData.baseUrl) || config.baseUrl;
    self.setData({ baseUrl: baseUrl });

    if (id) {
      self.loadProductDetail(id, baseUrl);
    } else {
      wx.showToast({
        title: '产品不存在',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  loadProductDetail: function(id, baseUrl) {
    var self = this;
    try {
      self.setData({ loading: true });

      api.getProductDetail(id).then(function(product) {
        if (!baseUrl) {
          var app = getApp();
          baseUrl = (app.globalData && app.globalData.baseUrl) || config.baseUrl;
        }

        var images = [];
        if (product.images) {
          try {
            images = JSON.parse(product.images);
          } catch (e) {
            images = product.images.split(',').filter(function(img) { return img.trim(); });
          }
        }

        var features = [];
        if (product.features) {
          try {
            features = JSON.parse(product.features);
          } catch (e) {
            features = product.features.split(',').filter(function(f) { return f.trim(); });
          }
        }

        var previewImages = images.map(function(img) { return baseUrl + img; });

        self.setData({
          loading: false,
          product: product,
          images: images,
          features: features,
          previewImages: previewImages,
          baseUrl: baseUrl
        });
      }).catch(function(err) {
        console.error('加载产品详情失败:', err);
        self.setData({ loading: false });
        wx.showToast({
          title: '加载失败',
          icon: 'none'
        });
        wx.navigateBack();
      });
    } catch (e) {
      console.error('加载产品详情异常:', e);
      self.setData({ loading: false });
    }
  },

  onImageChange: function(e) {
    var current = e.detail.current;
    this.setData({
      currentImageIndex: current
    });
  },

  onImageTap: function(e) {
    var index = e.currentTarget.dataset.index;
    if (this.data.previewImages.length > 0) {
      wx.previewImage({
        current: this.data.previewImages[index],
        urls: this.data.previewImages
      });
    }
  },

  onImageLongPress: function(e) {
    var index = e.currentTarget.dataset.index;
    var url = this.data.previewImages[index];
    var self = this;

    wx.showModal({
      title: '保存图片',
      content: '是否保存该图片到相册？',
      success: function(res) {
        if (res.confirm) {
          wx.downloadFile({
            url: url,
            success: function(downloadRes) {
              if (downloadRes.statusCode === 200) {
                wx.saveImageToPhotosAlbum({
                  filePath: downloadRes.tempFilePath,
                  success: function() {
                    wx.showToast({
                      title: '保存成功',
                      icon: 'success'
                    });
                  },
                  fail: function() {
                    wx.showToast({
                      title: '保存失败',
                      icon: 'none'
                    });
                  }
                });
              }
            },
            fail: function() {
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

  onBack: function() {
    wx.navigateBack();
  }
});
