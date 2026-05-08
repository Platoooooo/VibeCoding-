const api = require('../../request.js');

Page({
  data: {
    products: [],
    categories: [],
    page: 1,
    size: 10,
    hasMore: true,
    loading: false,
    keyword: '',
    categoryId: '',
    categoryName: '',
    showFilter: false,
    selectedCategory: '',
    filteredCategories: []
  },

  onLoad(options) {
    console.log('产品页面onLoad，接收到的参数:', options);
    // 如果从分类页面跳转过来，会带有categoryId和categoryName参数
    if (options.categoryId) {
      this.setData({
        categoryId: options.categoryId,
        categoryName: decodeURIComponent(options.categoryName) || ''
      });
    }
    // 先加载分类，再加载产品
    this.loadCategories().then(() => {
      this.loadProducts();
    });
  },

  onShow() {
    // 检查是否有从分类页面传来的参数（通过storage）
    const selectedCategory = wx.getStorageSync('selectedCategory');
    if (selectedCategory && selectedCategory.id) {
      console.log('从storage获取分类:', selectedCategory);
      this.setData({
        categoryId: selectedCategory.id,
        categoryName: selectedCategory.name || ''
      });
      // 清除storage中的分类
      wx.removeStorageSync('selectedCategory');
      // 重新加载产品列表
      this.refreshProducts();
    }
  },

  onPullDownRefresh() {
    this.refreshProducts();
    wx.stopPullDownRefresh();
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadMoreProducts();
    }
  },

  // 加载分类列表
  async loadCategories() {
    try {
      const categories = await api.getCategories();
      const activeCategories = (categories || []).filter(cat => cat.active !== false && cat.status !== 0);
      this.setData({
        categories: activeCategories,
        filteredCategories: activeCategories
      });
    } catch (err) {
      console.error('加载分类失败:', err);
    }
  },

  // 加载产品列表
  async loadProducts() {
    try {
      this.setData({ loading: true });
      
      const params = {
        page: this.data.page,
        size: this.data.size,
        keyword: this.data.keyword,
        categoryId: this.data.categoryId
      };

      console.log('请求参数:', params);
      const result = await api.getProductList(params);
      console.log('产品列表结果:', result);
      
      // 获取分类映射
      const categoryMap = {};
      this.data.categories.forEach(cat => {
        categoryMap[cat.id] = cat.name;
      });
      console.log('分类映射:', categoryMap);
      console.log('当前categoryId:', this.data.categoryId);
      
      // 解析产品数据，处理 images 字段和分类名称
      // 兼容两种格式：content/totalElements 或 list/total
      const productsList = (result && result.content) || (result && result.list) || [];
      const totalElements = (result && result.totalElements) || (result && result.total) || 0;
      console.log('产品数量:', productsList.length, '总数量:', totalElements);
      console.log('原始result:', JSON.stringify(result).substring(0, 500));
      
      const newProducts = productsList.map(product => {
        let firstImage = '';
        if (product.images) {
          try {
            // images 可能是 JSON 字符串数组，需要解析
            const images = typeof product.images === 'string' ? JSON.parse(product.images) : product.images;
            if (Array.isArray(images) && images.length > 0) {
              firstImage = images[0];
            } else if (typeof images === 'string' && images) {
              // 如果是逗号分隔的字符串
              firstImage = images.split(',')[0];
            }
          } catch (e) {
            // 解析失败，尝试直接使用
            firstImage = product.images;
          }
        }
        // 关联分类名称
        const categoryName = categoryMap[product.categoryId] || '未分类';
        console.log('产品:', product.name, 'categoryId:', product.categoryId, '符合筛选:', !this.data.categoryId || product.categoryId === this.data.categoryId);
        return { ...product, firstImage, categoryName };
      });
      console.log('解析后的产品:', newProducts);

      this.setData({
        products: newProducts,
        hasMore: this.data.page * this.data.size < totalElements,
        loading: false
      });
    } catch (err) {
      console.error('加载产品失败:', err);
      this.setData({ loading: false, products: [] });
    }
  },

  // 刷新产品列表
  async refreshProducts() {
    this.setData({ page: 1 });
    await this.loadProducts();
  },

  // 加载更多产品
  async loadMoreProducts() {
    if (!this.data.hasMore || this.data.loading) return;

    try {
      this.setData({ loading: true });

      const params = {
        page: this.data.page + 1,
        size: this.data.size,
        keyword: this.data.keyword,
        categoryId: this.data.categoryId
      };

      const result = await api.getProductList(params);
      // 兼容两种格式
      const productsList = (result && result.content) || (result && result.list) || [];
      const totalElements = (result && result.totalElements) || (result && result.total) || 0;

      this.setData({
        products: [...this.data.products, ...productsList],
        page: this.data.page + 1,
        hasMore: this.data.page * this.data.size < totalElements,
        loading: false
      });
    } catch (err) {
      console.error('加载更多产品失败:', err);
      this.setData({ loading: false });
    }
  },

  // 搜索输入（实时搜索）
  onSearchInput(e) {
    const keyword = e.detail.value;
    this.setData({
      keyword: keyword
    });
    // 实时搜索，延迟300ms避免频繁请求
    if (this.searchTimer) {
      clearTimeout(this.searchTimer);
    }
    this.searchTimer = setTimeout(() => {
      this.refreshProducts();
    }, 300);
  },

  // 搜索确认
  onSearchConfirm() {
    if (this.searchTimer) {
      clearTimeout(this.searchTimer);
    }
    this.refreshProducts();
  },

  // 清空搜索
  onSearchClear() {
    console.log('清空搜索被点击');
    this.setData({ keyword: '' });
    // 清除定时器
    if (this.searchTimer) {
      clearTimeout(this.searchTimer);
    }
    // 清空后立即刷新
    this.refreshProducts();
  },

  // 切换筛选面板
  toggleFilter() {
    this.setData({
      showFilter: !this.data.showFilter
    });
  },

  // 选择分类（点击后立即筛选）
  onSelectCategory(e) {
    const { id, name } = e.currentTarget.dataset;
    // 如果点击的是已选中的分类，则取消选择
    const selectedCategory = this.data.selectedCategory === id ? '' : id;
    this.setData({
      selectedCategory,
      categoryId: selectedCategory,
      categoryName: selectedCategory ? name : '',
      showFilter: false, // 选择后关闭筛选面板
      page: 1
    });
    // 立即加载筛选后的产品
    this.loadProducts();
  },

  // 应用筛选
  applyFilter() {
    this.setData({
      showFilter: false,
      page: 1
    });
    this.loadProducts();
  },

  // 重置筛选
  resetFilter() {
    this.setData({
      selectedCategory: '',
      categoryId: '',
      categoryName: '',
      page: 1
    });
    // 重置后刷新列表
    this.loadProducts();
  },

  // 点击产品，跳转到详情页
  onProductTap(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/pages/product-detail/product-detail?id=' + id
    });
  }
});
