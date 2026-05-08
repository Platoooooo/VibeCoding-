package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.Banner;
import com.company.miniprogram.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/banner")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @PostMapping
    public ApiResponse<Banner> createBanner(@RequestBody Banner banner) {
        Banner created = bannerService.createBanner(banner);
        return ApiResponse.success(created);
    }

    @PutMapping("/{id}")
    public ApiResponse<Banner> updateBanner(@PathVariable Long id, @RequestBody Banner banner) {
        Banner updated = bannerService.updateBanner(id, banner);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ApiResponse.success();
    }
}
