package com.company.miniprogram.service;

import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.News;
import com.company.miniprogram.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public PageResponse<News> getPublishedNews(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<News> newsPage = newsRepository.findByStatusOrderByCreatedAtDesc(1, pageable);
        return new PageResponse<>(newsPage.getContent(), newsPage.getTotalElements(), page, size);
    }

    @Transactional
    public News createNews(News news) {
        if (news.getStatus() == null) {
            news.setStatus(0);
        }
        return newsRepository.save(news);
    }

    @Transactional
    public News updateNews(Long id, News news) {
        News existing = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("新闻不存在"));
        existing.setTitle(news.getTitle());
        existing.setSummary(news.getSummary());
        existing.setContent(news.getContent());
        existing.setImage(news.getImage());
        existing.setStatus(news.getStatus());
        return newsRepository.save(existing);
    }

    @Transactional
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}
