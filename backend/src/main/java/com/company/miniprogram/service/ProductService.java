package com.company.miniprogram.service;

import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.Product;
import com.company.miniprogram.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PageResponse<Product> getProducts(String keyword, String categoryId, int page, int size) {
        log.info("【Service查询】keyword={}, categoryId={}, page={}, size={}", keyword, categoryId, page, size);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productRepository.searchProducts(keyword, categoryId, pageable);
        log.info("【Service结果】数据库返回 {} 条记录", productPage.getTotalElements());
        return new PageResponse<>(productPage.getContent(), productPage.getTotalElements(), page, size);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
    }

    @Transactional
    public Product createProduct(Product product) {
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
        existing.setProductNo(product.getProductNo());
        existing.setName(product.getName());
        existing.setSpec(product.getSpec());
        existing.setModel(product.getModel());
        existing.setPrice(product.getPrice());
        existing.setImages(product.getImages());
        existing.setDescription(product.getDescription());
        existing.setFeatures(product.getFeatures());
        existing.setCategoryId(product.getCategoryId());
        existing.setStatus(product.getStatus());
        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
