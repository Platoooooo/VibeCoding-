package com.company.miniprogram.service;

import com.company.miniprogram.model.ProductCategory;
import com.company.miniprogram.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> getActiveCategories() {
        return productCategoryRepository.findByStatusOrderBySortOrderAsc(1);
    }

    @Transactional
    public ProductCategory createCategory(ProductCategory category) {
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        return productCategoryRepository.save(category);
    }

    @Transactional
    public ProductCategory updateCategory(String id, ProductCategory category) {
        ProductCategory existing = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        existing.setName(category.getName());
        existing.setImage(category.getImage());
        existing.setParentId(category.getParentId());
        existing.setSortOrder(category.getSortOrder());
        existing.setStatus(category.getStatus());
        return productCategoryRepository.save(existing);
    }

    @Transactional
    public void deleteCategory(String id) {
        productCategoryRepository.deleteById(id);
    }
}
