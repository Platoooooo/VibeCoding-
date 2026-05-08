package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.CompanyInfo;
import com.company.miniprogram.service.CompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/company")
@RequiredArgsConstructor
public class AdminCompanyController {

    private final CompanyInfoService companyInfoService;

    @PostMapping
    public ApiResponse<CompanyInfo> saveCompanyInfo(@RequestBody CompanyInfo companyInfo) {
        CompanyInfo saved = companyInfoService.saveCompanyInfo(companyInfo);
        return ApiResponse.success(saved);
    }
}
