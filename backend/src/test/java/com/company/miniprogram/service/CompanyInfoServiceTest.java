package com.company.miniprogram.service;

import com.company.miniprogram.model.CompanyInfo;
import com.company.miniprogram.repository.CompanyInfoRepository;
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
class CompanyInfoServiceTest {

    @Mock
    private CompanyInfoRepository companyInfoRepository;

    @InjectMocks
    private CompanyInfoService companyInfoService;

    private CompanyInfo sampleInfo;

    @BeforeEach
    void setUp() {
        sampleInfo = new CompanyInfo();
        sampleInfo.setId(1L);
        sampleInfo.setCompanyName("测试公司");
        sampleInfo.setDescription("公司描述");
        sampleInfo.setPhone("13800138000");
    }

    @Test
    void getCompanyInfo_shouldReturnFirst_whenExists() {
        when(companyInfoRepository.findAll()).thenReturn(List.of(sampleInfo));

        Optional<CompanyInfo> result = companyInfoService.getCompanyInfo();

        assertTrue(result.isPresent());
        assertEquals("测试公司", result.get().getCompanyName());
    }

    @Test
    void getCompanyInfo_shouldReturnEmpty_whenNoData() {
        when(companyInfoRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<CompanyInfo> result = companyInfoService.getCompanyInfo();

        assertFalse(result.isPresent());
    }

    @Test
    void saveCompanyInfo_shouldCreate_whenNoExisting() {
        when(companyInfoRepository.findAll()).thenReturn(Collections.emptyList());
        when(companyInfoRepository.save(any(CompanyInfo.class))).thenReturn(sampleInfo);

        CompanyInfo result = companyInfoService.saveCompanyInfo(sampleInfo);

        assertNotNull(result);
        assertEquals("测试公司", result.getCompanyName());
    }

    @Test
    void getCompanyInfo_shouldReturnEmptyOptional_whenNoCompanyInfoExists() {
        when(companyInfoRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<CompanyInfo> result = companyInfoService.getCompanyInfo();

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    void saveCompanyInfo_shouldUpdate_whenExisting() {
        CompanyInfo existing = new CompanyInfo();
        existing.setId(1L);
        existing.setCompanyName("旧名称");

        when(companyInfoRepository.findAll()).thenReturn(List.of(existing));
        when(companyInfoRepository.save(any(CompanyInfo.class))).thenReturn(sampleInfo);

        CompanyInfo result = companyInfoService.saveCompanyInfo(sampleInfo);

        assertNotNull(result);
        verify(companyInfoRepository).save(any(CompanyInfo.class));
    }
}
