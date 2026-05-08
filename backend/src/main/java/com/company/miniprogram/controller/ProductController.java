package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.Product;
import com.company.miniprogram.model.ProductCategory;
import com.company.miniprogram.service.ProductCategoryService;
import com.company.miniprogram.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductCategoryService categoryService;

    @GetMapping("/categories")
    public ApiResponse<List<ProductCategory>> getCategories() {
        List<ProductCategory> categories = categoryService.getActiveCategories();
        return ApiResponse.success(categories);
    }

    @GetMapping("/list")
    public ApiResponse<PageResponse<Product>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String spec,
            @RequestParam(required = false) String priceRange) {
        // log.info("【产品列表请求】page={}, size={}, keyword={}, categoryId={}", page, size,
        // keyword, categoryId);
        PageResponse<Product> products = productService.getProducts(keyword, categoryId, page, size);
        // log.info("【产品列表结果】total={}, listSize={}", products.getTotal(),
        // products.getList().size());
        return ApiResponse.success(products);
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<Product> getProductDetail(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ApiResponse.success(product);
    }
}
