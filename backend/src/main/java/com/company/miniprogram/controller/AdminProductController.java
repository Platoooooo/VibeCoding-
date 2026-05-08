package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.Product;
import com.company.miniprogram.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ApiResponse.success(created);
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success();
    }
}
