package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.Announcement;
import com.company.miniprogram.model.CompanyInfo;
import com.company.miniprogram.model.News;
import com.company.miniprogram.service.AnnouncementService;
import com.company.miniprogram.service.CompanyInfoService;
import com.company.miniprogram.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyInfoService companyInfoService;
    private final NewsService newsService;
    private final AnnouncementService announcementService;

    @GetMapping("/info")
    public ApiResponse<CompanyInfo> getCompanyInfo() {
        return companyInfoService.getCompanyInfo()
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("公司信息不存在"));
    }

    @GetMapping("/news")
    public ApiResponse<PageResponse<News>> getNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<News> news = newsService.getPublishedNews(page, size);
        return ApiResponse.success(news);
    }

    @GetMapping("/announcement")
    public ApiResponse<PageResponse<Announcement>> getAnnouncements(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<Announcement> announcements = announcementService.getAnnouncements(page, size);
        return ApiResponse.success(announcements);
    }
}
