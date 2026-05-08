package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.service.CompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final CompanyInfoService companyInfoService;

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getContactInfo() {
        return companyInfoService.getCompanyInfo()
                .map(info -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("companyName", info.getCompanyName());
                    data.put("address", info.getAddress());
                    data.put("phone", info.getPhone());
                    data.put("email", info.getEmail());
                    data.put("wechat", info.getWechat());
                    data.put("latitude", info.getLatitude());
                    data.put("longitude", info.getLongitude());

                    Map<String, String> workTime = new HashMap<>();
                    workTime.put("workday", info.getWorkdayTime() != null ? info.getWorkdayTime() : "");
                    workTime.put("weekend", info.getWeekendTime() != null ? info.getWeekendTime() : "");
                    data.put("workTime", workTime);

                    return ApiResponse.success(data);
                })
                .orElse(ApiResponse.error("联系信息不存在"));
    }
}
