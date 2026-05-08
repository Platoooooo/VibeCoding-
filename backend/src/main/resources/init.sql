-- 创建数据库
CREATE DATABASE IF NOT EXISTS company_miniprogram DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE company_miniprogram;

-- 轮播图表
CREATE TABLE IF NOT EXISTS banner (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    image VARCHAR(500) NOT NULL COMMENT '图片URL',
    title VARCHAR(100) COMMENT '标题',
    link VARCHAR(500) COMMENT '跳转链接',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- 公司信息表
CREATE TABLE IF NOT EXISTS company_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(200) NOT NULL COMMENT '公司名称',
    description TEXT COMMENT '公司简介',
    logo VARCHAR(500) COMMENT 'logo图片',
    address VARCHAR(500) COMMENT '地址',
    phone VARCHAR(50) COMMENT '电话',
    email VARCHAR(100) COMMENT '邮箱',
    wechat VARCHAR(100) COMMENT '微信号',
    latitude DECIMAL(10,7) COMMENT '纬度',
    longitude DECIMAL(10,7) COMMENT '经度',
    workday_time VARCHAR(100) COMMENT '工作日时间',
    weekend_time VARCHAR(100) COMMENT '周末时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司信息表';

-- 新闻表
CREATE TABLE IF NOT EXISTS news (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    summary VARCHAR(500) COMMENT '摘要',
    content LONGTEXT NOT NULL COMMENT '内容',
    image VARCHAR(500) COMMENT '封面图',
    status INT DEFAULT 0 COMMENT '状态：0-草稿，1-发布',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻表';

-- 公告表
CREATE TABLE IF NOT EXISTS announcement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶',
    status INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 产品分类表
CREATE TABLE IF NOT EXISTS product_category (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    image VARCHAR(500) COMMENT '分类图片',
    parent_id VARCHAR(50) COMMENT '父分类ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品分类表';

-- 产品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_no VARCHAR(50) UNIQUE NOT NULL COMMENT '产品编号',
    name VARCHAR(200) NOT NULL COMMENT '产品名称',
    spec VARCHAR(100) COMMENT '规格',
    model VARCHAR(100) COMMENT '型号',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    images TEXT COMMENT '图片JSON数组',
    description TEXT COMMENT '描述',
    features TEXT COMMENT '特性JSON数组',
    category_id VARCHAR(50) NOT NULL COMMENT '分类ID',
    status INT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- 插入测试数据
INSERT INTO company_info (company_name, description, logo, address, phone, email, wechat, latitude, longitude, workday_time, weekend_time)
VALUES ('示例企业', '这是一家示例企业的简介...', 'https://example.com/logo.png', '深圳市南山区科技园', '0755-88888888', 'contact@example.com', 'CompanyWechat', 22.5431000, 114.0579000, '周一至周五 09:00 - 18:00', '周六 09:00 - 12:00');

INSERT INTO banner (image, title, link, sort_order, status)
VALUES
('https://example.com/banner1.jpg', '轮播图1', '/pages/detail/1', 1, 1),
('https://example.com/banner2.jpg', '轮播图2', '/pages/detail/2', 2, 1);

INSERT INTO product_category (id, name, parent_id, sort_order, status)
VALUES
('cat001', '产品分类1', NULL, 1, 1),
('cat002', '产品分类2', NULL, 2, 1);

INSERT INTO news (title, summary, content, image, status)
VALUES ('企业新闻标题', '新闻摘要...', '新闻详情内容...', 'https://example.com/news1.jpg', 1);

INSERT INTO announcement (title, content, is_top, status)
VALUES ('重要公告', '这是公告内容...', 1, 1);

INSERT INTO product (product_no, name, spec, model, price, images, description, features, category_id, status)
VALUES ('PRD001', '示例产品', '规格A', '型号X', 580.00, '["https://example.com/p1.jpg"]', '产品描述...', '["特性1", "特性2"]', 'cat001', 1);
