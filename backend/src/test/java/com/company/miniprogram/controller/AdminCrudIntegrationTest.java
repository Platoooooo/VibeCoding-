package com.company.miniprogram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AdminCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void loginAndGetToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"test123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        token = extractToken(response);
    }

    // ==================== Banner CRUD ====================

    @Test
    void banner_shouldFullCrudFlow() throws Exception {
        // Create
        MvcResult createResult = mockMvc.perform(post("/api/admin/banner")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"image\":\"/uploads/banner.jpg\",\"title\":\"测试轮播图\",\"sortOrder\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id", greaterThan(0)))
                .andExpect(jsonPath("$.data.title").value("测试轮播图"))
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("data").get("id").asLong();

        // Read (via public API)
        mockMvc.perform(get("/api/banner/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        // Update
        mockMvc.perform(put("/api/admin/banner/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"image\":\"/uploads/banner-updated.jpg\",\"title\":\"更新轮播图\",\"sortOrder\":2,\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.title").value("更新轮播图"));

        // Delete
        mockMvc.perform(delete("/api/admin/banner/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    // ==================== News CRUD ====================

    @Test
    void news_shouldFullCrudFlow() throws Exception {
        // Create
        MvcResult createResult = mockMvc.perform(post("/api/admin/news")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"测试新闻\",\"summary\":\"摘要\",\"content\":\"<p>内容</p>\",\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id", greaterThan(0)))
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("data").get("id").asLong();

        // Update
        mockMvc.perform(put("/api/admin/news/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"更新新闻\",\"summary\":\"新摘要\",\"content\":\"<p>新内容</p>\",\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.title").value("更新新闻"));

        // Delete
        mockMvc.perform(delete("/api/admin/news/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    // ==================== Announcement CRUD ====================

    @Test
    void announcement_shouldFullCrudFlow() throws Exception {
        // Create
        MvcResult createResult = mockMvc.perform(post("/api/admin/announcement")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"测试公告\",\"content\":\"公告内容\",\"isTop\":true,\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.isTop").value(true))
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("data").get("id").asLong();

        // Update
        mockMvc.perform(put("/api/admin/announcement/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"更新公告\",\"content\":\"新公告内容\",\"isTop\":false,\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.isTop").value(false));

        // Delete
        mockMvc.perform(delete("/api/admin/announcement/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    // ==================== Category CRUD ====================

    @Test
    void category_shouldFullCrudFlow() throws Exception {
        // Create (String ID)
        mockMvc.perform(post("/api/admin/category")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"cat-test\",\"name\":\"测试分类\",\"sortOrder\":1,\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.name").value("测试分类"));

        // Update
        mockMvc.perform(put("/api/admin/category/cat-test")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"cat-test\",\"name\":\"更新分类\",\"sortOrder\":2,\"status\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.name").value("更新分类"));

        // Delete
        mockMvc.perform(delete("/api/admin/category/cat-test")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    // ==================== Product CRUD ====================

    @Test
    void product_shouldFullCrudFlow() throws Exception {
        // Create
        MvcResult createResult = mockMvc.perform(post("/api/admin/product")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productNo\":\"P-TEST\",\"name\":\"测试产品\",\"price\":88.00,\"categoryId\":\"cat-test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.productNo").value("P-TEST"))
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("data").get("id").asLong();

        // Update
        mockMvc.perform(put("/api/admin/product/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productNo\":\"P-TEST\",\"name\":\"更新产品\",\"price\":99.00,\"categoryId\":\"cat-test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.name").value("更新产品"));

        // Delete
        mockMvc.perform(delete("/api/admin/product/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    // ==================== Company Info ====================

    @Test
    void company_shouldSaveInfo() throws Exception {
        mockMvc.perform(post("/api/admin/company")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\":\"测试公司\",\"description\":\"公司描述\",\"address\":\"测试地址\",\"phone\":\"13800001111\",\"email\":\"test@company.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.companyName").value("测试公司"));
    }

    // ==================== Auth edge cases ====================

    @Test
    void adminEndpoint_shouldReturn403_withInvalidToken() throws Exception {
        mockMvc.perform(post("/api/admin/banner")
                        .header("Authorization", "Bearer invalid-token-here")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoint_shouldReturn403_withoutToken() throws Exception {
        mockMvc.perform(post("/api/admin/banner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateNonexistent_shouldReturnErrorCode() throws Exception {
        mockMvc.perform(put("/api/admin/banner/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"image\":\"/x.jpg\",\"title\":\"x\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(-1));
    }

    private String extractToken(String json) {
        int tokenStart = json.indexOf("\"token\":\"") + 9;
        int tokenEnd = json.indexOf("\"", tokenStart);
        return json.substring(tokenStart, tokenEnd);
    }
}
