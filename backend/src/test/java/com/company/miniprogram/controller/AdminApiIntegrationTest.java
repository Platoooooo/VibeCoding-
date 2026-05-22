package com.company.miniprogram.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_shouldReturnToken_withValidCredentials() throws Exception {
        mockMvc.perform(post("/admin/login")
                        .contentType("application/json")
                        .content("{\"username\":\"admin\",\"password\":\"test123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.token").isString());
    }

    @Test
    void login_shouldReturn401_withInvalidCredentials() throws Exception {
        mockMvc.perform(post("/admin/login")
                        .contentType("application/json")
                        .content("{\"username\":\"wrong\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void adminEndpoint_shouldReturnForbidden_withoutToken() throws Exception {
        mockMvc.perform(get("/api/admin/news/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoint_shouldSucceed_withValidToken() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/admin/login")
                        .contentType("application/json")
                        .content("{\"username\":\"admin\",\"password\":\"test123\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();
        String token = extractToken(response);

        mockMvc.perform(post("/api/admin/product")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content("{\"productNo\":\"P999\",\"name\":\"测试产品\",\"price\":99.00,\"categoryId\":\"cat-1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    private String extractToken(String json) {
        int tokenStart = json.indexOf("\"token\":\"") + 9;
        int tokenEnd = json.indexOf("\"", tokenStart);
        return json.substring(tokenStart, tokenEnd);
    }
}
