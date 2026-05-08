package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.ProductCategory;
import com.company.miniprogram.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final ProductCategoryService categoryService;

    @PostMapping
    public ApiResponse<ProductCategory> createCategory(@RequestBody ProductCategory category) {
        log.info("创建分类，接收到的数据: {}", category);
        ProductCategory created = categoryService.createCategory(category);
        log.info("创建分类成功，返回数据: {}", created);
        return ApiResponse.success(created);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductCategory> updateCategory(@PathVariable String id, @RequestBody ProductCategory category) {
        log.info("更新分类 ID: {}, 接收到的数据: {}", id, category);
        ProductCategory updated = categoryService.updateCategory(id, category);
        log.info("更新分类成功，返回数据: {}", updated);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success();
    }
}
