package com.company.miniprogram.controller;

import com.company.miniprogram.model.Product;
import com.company.miniprogram.model.ProductCategory;
import com.company.miniprogram.repository.ProductCategoryRepository;
import com.company.miniprogram.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublicApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        ProductCategory category = new ProductCategory();
        category.setId("cat-1");
        category.setName("测试分类");
        categoryRepository.save(category);

        Product product = new Product();
        product.setProductNo("P001");
        product.setName("测试产品");
        product.setPrice(new BigDecimal("99.00"));
        product.setCategoryId("cat-1");
        product.setStatus(1);
        productRepository.save(product);
    }

    @Test
    void getCategories_shouldReturnCategoryList() throws Exception {
        mockMvc.perform(get("/api/product/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name").value("测试分类"));
    }

    @Test
    void getProductList_shouldReturnPagedProducts() throws Exception {
        mockMvc.perform(get("/api/product/list")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.list[0].name").value("测试产品"));
    }

    @Test
    void getProductDetail_shouldReturnProduct() throws Exception {
        Product saved = productRepository.findAll().get(0);

        mockMvc.perform(get("/api/product/detail/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.productNo").value("P001"));
    }

    @Test
    void getProductDetail_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/product/detail/99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1));
    }

    @Test
    void getCompanyInfo_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/company/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("公司信息不存在"));
    }
}
