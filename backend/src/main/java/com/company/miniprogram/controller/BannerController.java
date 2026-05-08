package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.Banner;
import com.company.miniprogram.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/list")
    public ApiResponse<List<Banner>> getBanners() {
        List<Banner> banners = bannerService.getActiveBanners();
        return ApiResponse.success(banners);
    }
}
