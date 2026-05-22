package com.company.miniprogram.service;

import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.Product;
import com.company.miniprogram.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setProductNo("P001");
        sampleProduct.setName("测试产品");
        sampleProduct.setPrice(new BigDecimal("99.00"));
        sampleProduct.setCategoryId("cat-1");
        sampleProduct.setStatus(1);
    }

    @Test
    void getProducts_shouldReturnPageResponse() {
        Page<Product> page = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.searchProducts(eq(null), eq(null), any(Pageable.class)))
                .thenReturn(page);

        PageResponse<Product> result = productService.getProducts(null, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
        assertEquals("测试产品", result.getList().get(0).getName());
    }

    @Test
    void getProductById_shouldReturnProduct_whenFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("P001", result.getProductNo());
    }

    @Test
    void getProductById_shouldThrow_whenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(999L));
    }

    @Test
    void createProduct_shouldSetDefaultStatusAndSave() {
        Product unsaved = new Product();
        unsaved.setName("新产品");
        unsaved.setProductNo("P002");
        unsaved.setPrice(new BigDecimal("50.00"));
        unsaved.setCategoryId("cat-2");

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(2L);
            return p;
        });

        Product result = productService.createProduct(unsaved);

        assertNotNull(result.getId());
        assertEquals(1, result.getStatus());
        verify(productRepository).save(unsaved);
    }

    @Test
    void updateProduct_shouldUpdateAllFields() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product update = new Product();
        update.setName("更新名称");
        update.setPrice(new BigDecimal("199.00"));

        productService.updateProduct(1L, update);

        assertEquals("更新名称", sampleProduct.getName());
        assertEquals(new BigDecimal("199.00"), sampleProduct.getPrice());
    }

    @Test
    void deleteProduct_shouldCallRepository() {
        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void getProducts_shouldReturnEmpty_whenCategoryFilterNoResults() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productRepository.searchProducts(eq(null), eq("non-existent-category"), any(Pageable.class)))
                .thenReturn(emptyPage);

        PageResponse<Product> result = productService.getProducts(null, "non-existent-category", 1, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getList().isEmpty());
    }

    @Test
    void getProducts_shouldReturnEmpty_whenPageOutOfRange() {
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productRepository.searchProducts(eq(null), eq(null), any(Pageable.class)))
                .thenReturn(emptyPage);

        PageResponse<Product> result = productService.getProducts(null, null, 999, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotal());
    }
}
