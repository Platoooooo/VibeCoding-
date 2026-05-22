const request = require('../request.js');

describe('request module', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    global.getApp.mockImplementation(() => ({
      globalData: {
        baseUrl: 'http://localhost:8080',
        userInfo: null
      }
    }));
  });

  describe('getBaseUrl', () => {
    test('should fall back to config baseUrl when getApp unavailable', () => {
      global.getApp.mockImplementation(() => {
        throw new Error('getApp not available');
      });
      const url = request.getBaseUrl();
      expect(url).toBe('http://localhost:8080');
    });

    test('should use app globalData baseUrl when available', () => {
      global.getApp.mockImplementation(() => ({
        globalData: { baseUrl: 'https://api.example.com' }
      }));
      const url = request.getBaseUrl();
      expect(url).toBe('https://api.example.com');
    });
  });

  describe('get', () => {
    test('should call wx.request with GET method', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 0, data: { items: [] } } });
      });

      return request.get('/api/banner/list').then((result) => {
        expect(wx.request).toHaveBeenCalledWith(
          expect.objectContaining({
            url: 'http://localhost:8080/api/banner/list',
            method: 'GET'
          })
        );
        expect(result).toEqual({ items: [] });
      });
    });

    test('should resolve with data when code is 0', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 0, data: { name: 'test' } } });
      });

      return request.get('/api/test').then((result) => {
        expect(result).toEqual({ name: 'test' });
      });
    });

    test('should resolve with data when code is 200', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 200, data: { name: 'test' } } });
      });

      return request.get('/api/test').then((result) => {
        expect(result).toEqual({ name: 'test' });
      });
    });

    test('should reject when code is not 0 or 200', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 500, message: 'Server error' } });
      });

      return request.get('/api/test').catch((err) => {
        expect(err.code).toBe(500);
        expect(err.message).toBe('Server error');
      });
    });
  });

  describe('post', () => {
    test('should call wx.request with POST method and body', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 0, data: { id: 1 } } });
      });

      return request.post('/api/submit', { name: 'x' }).then((result) => {
        expect(wx.request).toHaveBeenCalledWith(
          expect.objectContaining({
            url: 'http://localhost:8080/api/submit',
            method: 'POST'
          })
        );
        expect(result).toEqual({ id: 1 });
      });
    });
  });

  describe('API wrappers', () => {
    test('getBanners should call correct endpoint', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 0, data: [] } });
      });
      return request.getBanners().then(() => {
        expect(wx.request).toHaveBeenCalledWith(
          expect.objectContaining({ url: 'http://localhost:8080/api/banner/list' })
        );
      });
    });

    test('getCompanyInfo should call correct endpoint', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 0, data: {} } });
      });
      return request.getCompanyInfo().then(() => {
        expect(wx.request).toHaveBeenCalledWith(
          expect.objectContaining({ url: 'http://localhost:8080/api/company/info' })
        );
      });
    });

    test('getProductList should pass query params', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 0, data: [] } });
      });
      return request.getProductList({ page: 2, size: 5, categoryId: 'cat-1' }).then(() => {
        expect(wx.request).toHaveBeenCalledWith(
          expect.objectContaining({
            url: 'http://localhost:8080/api/product/list',
            method: 'GET'
          })
        );
        const callArgs = wx.request.mock.calls[0][0];
        expect(callArgs.data.page).toBe(2);
        expect(callArgs.data.size).toBe(5);
        expect(callArgs.data.categoryId).toBe('cat-1');
      });
    });

    test('getProductDetail should include id in URL', () => {
      wx.request.mockImplementation((opts) => {
        opts.success({ data: { code: 0, data: {} } });
      });
      return request.getProductDetail(42).then(() => {
        expect(wx.request).toHaveBeenCalledWith(
          expect.objectContaining({ url: 'http://localhost:8080/api/product/detail/42' })
        );
      });
    });
  });

  describe('network failure', () => {
    test('should reject on wx.request fail', () => {
      wx.request.mockImplementation((opts) => {
        opts.fail({ errMsg: 'timeout' });
      });

      return request.get('/api/test').catch((err) => {
        expect(err.errMsg).toBe('timeout');
      });
    });
  });
});
