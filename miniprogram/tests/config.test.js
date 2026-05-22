const config = require('../config.js');

describe('config', () => {
  test('should export baseUrl as a string', () => {
    expect(typeof config.baseUrl).toBe('string');
    expect(config.baseUrl.length).toBeGreaterThan(0);
  });

  test('baseUrl should be a valid URL format', () => {
    expect(config.baseUrl).toMatch(/^https?:\/\/.+/);
  });

  test('development baseUrl should be localhost', () => {
    expect(config.baseUrl).toBe('http://localhost:8080');
  });
});
