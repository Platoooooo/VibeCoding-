package com.company.miniprogram.service;

import com.company.miniprogram.model.ProductCategory;
import com.company.miniprogram.repository.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductCategoryService productCategoryService;

    private ProductCategory sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new ProductCategory();
        sampleCategory.setId("cat-001");
        sampleCategory.setName("针织面料");
        sampleCategory.setImage("/uploads/category1.jpg");
        sampleCategory.setParentId(null);
        sampleCategory.setSortOrder(1);
        sampleCategory.setStatus(1);
    }

    @Test
    void getActiveCategories_shouldReturnList() {
        when(productCategoryRepository.findByStatusOrderBySortOrderAsc(1))
                .thenReturn(List.of(sampleCategory));

        List<ProductCategory> result = productCategoryService.getActiveCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("针织面料", result.get(0).getName());
    }

    @Test
    void getActiveCategories_shouldReturnEmpty_whenNoData() {
        when(productCategoryRepository.findByStatusOrderBySortOrderAsc(1))
                .thenReturn(Collections.emptyList());

        List<ProductCategory> result = productCategoryService.getActiveCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createCategory_shouldSetDefaultsAndSave() {
        ProductCategory unsaved = new ProductCategory();
        unsaved.setId("cat-002");
        unsaved.setName("新分类");

        when(productCategoryRepository.save(any(ProductCategory.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductCategory result = productCategoryService.createCategory(unsaved);

        assertEquals(0, result.getSortOrder());
        assertEquals(1, result.getStatus());
        assertEquals("新分类", result.getName());
    }

    @Test
    void createCategory_shouldKeepProvidedValues() {
        ProductCategory unsaved = new ProductCategory();
        unsaved.setId("cat-003");
        unsaved.setName("带排序分类");
        unsaved.setSortOrder(5);
        unsaved.setStatus(1);

        when(productCategoryRepository.save(any(ProductCategory.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductCategory result = productCategoryService.createCategory(unsaved);

        assertEquals(5, result.getSortOrder());
        assertEquals(1, result.getStatus());
    }

    @Test
    void updateCategory_shouldUpdateFields() {
        when(productCategoryRepository.findById("cat-001")).thenReturn(Optional.of(sampleCategory));
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(sampleCategory);

        ProductCategory update = new ProductCategory();
        update.setName("更新分类");
        update.setImage("/uploads/new.jpg");
        update.setParentId("parent-1");
        update.setSortOrder(10);
        update.setStatus(0);

        ProductCategory result = productCategoryService.updateCategory("cat-001", update);

        assertEquals("更新分类", result.getName());
        assertEquals("/uploads/new.jpg", result.getImage());
        assertEquals("parent-1", result.getParentId());
        assertEquals(10, result.getSortOrder());
        assertEquals(0, result.getStatus());
    }

    @Test
    void updateCategory_shouldThrow_whenNotFound() {
        when(productCategoryRepository.findById("non-existent")).thenReturn(Optional.empty());

        ProductCategory update = new ProductCategory();
        update.setName("更新");

        assertThrows(RuntimeException.class, () -> productCategoryService.updateCategory("non-existent", update));
    }

    @Test
    void deleteCategory_shouldCallRepository() {
        productCategoryService.deleteCategory("cat-001");

        verify(productCategoryRepository).deleteById("cat-001");
    }
}
