package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.CompanyInfo;
import com.company.miniprogram.service.CompanyInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private CompanyInfoService companyInfoService;

    @InjectMocks
    private ContactController contactController;

    @Test
    void getContactInfo_shouldReturnInfo_whenExists() {
        CompanyInfo info = new CompanyInfo();
        info.setId(1L);
        info.setCompanyName("测试公司");
        info.setAddress("测试地址");
        info.setPhone("13800138000");
        info.setEmail("test@company.com");
        info.setWechat("test_wechat");
        info.setLatitude(new BigDecimal("26.0745"));
        info.setLongitude(new BigDecimal("119.2965"));
        info.setWorkdayTime("09:00-18:00");
        info.setWeekendTime("10:00-17:00");

        when(companyInfoService.getCompanyInfo()).thenReturn(Optional.of(info));

        ApiResponse<Map<String, Object>> result = contactController.getContactInfo();

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertEquals("测试公司", result.getData().get("companyName"));
        assertEquals("测试地址", result.getData().get("address"));
        assertEquals("13800138000", result.getData().get("phone"));
        assertEquals("test@company.com", result.getData().get("email"));
        assertEquals("test_wechat", result.getData().get("wechat"));

        @SuppressWarnings("unchecked")
        Map<String, String> workTime = (Map<String, String>) result.getData().get("workTime");
        assertEquals("09:00-18:00", workTime.get("workday"));
        assertEquals("10:00-17:00", workTime.get("weekend"));
    }

    @Test
    void getContactInfo_shouldReturnError_whenNotExists() {
        when(companyInfoService.getCompanyInfo()).thenReturn(Optional.empty());

        ApiResponse<Map<String, Object>> result = contactController.getContactInfo();

        assertNotEquals(0, result.getCode());
        assertEquals("联系信息不存在", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void getContactInfo_shouldHandleNullWorkTime() {
        CompanyInfo info = new CompanyInfo();
        info.setId(1L);
        info.setCompanyName("测试公司");
        info.setAddress("测试地址");
        info.setWorkdayTime(null);
        info.setWeekendTime(null);

        when(companyInfoService.getCompanyInfo()).thenReturn(Optional.of(info));

        ApiResponse<Map<String, Object>> result = contactController.getContactInfo();

        assertEquals(0, result.getCode());

        @SuppressWarnings("unchecked")
        Map<String, String> workTime = (Map<String, String>) result.getData().get("workTime");
        assertEquals("", workTime.get("workday"));
        assertEquals("", workTime.get("weekend"));
    }
}
