-- 为产品分类表添加图片字段
-- 执行此脚本以更新现有数据库结构

USE company_miniprogram;

-- 添加image字段到product_category表
ALTER TABLE product_category 
ADD COLUMN image VARCHAR(500) COMMENT '分类图片' AFTER name;

-- 验证字段是否添加成功
DESCRIBE product_category;
