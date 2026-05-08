package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.News;
import com.company.miniprogram.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/news")
@RequiredArgsConstructor
public class AdminNewsController {

    private final NewsService newsService;

    @PostMapping
    public ApiResponse<News> createNews(@RequestBody News news) {
        News created = newsService.createNews(news);
        return ApiResponse.success(created);
    }

    @PutMapping("/{id}")
    public ApiResponse<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        News updated = newsService.updateNews(id, news);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ApiResponse.success();
    }
}
