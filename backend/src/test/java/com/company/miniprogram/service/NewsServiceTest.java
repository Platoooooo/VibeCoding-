package com.company.miniprogram.service;

import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.News;
import com.company.miniprogram.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsService newsService;

    private News sampleNews;

    @BeforeEach
    void setUp() {
        sampleNews = new News();
        sampleNews.setId(1L);
        sampleNews.setTitle("测试新闻");
        sampleNews.setSummary("摘要");
        sampleNews.setContent("详细内容");
        sampleNews.setStatus(1);
    }

    @Test
    void getPublishedNews_shouldReturnOnlyPublished() {
        Page<News> page = new PageImpl<>(List.of(sampleNews));
        when(newsRepository.findByStatusOrderByCreatedAtDesc(eq(1), any(Pageable.class)))
                .thenReturn(page);

        PageResponse<News> result = newsService.getPublishedNews(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void createNews_shouldSetDefaultDraftStatus() {
        News unsaved = new News();
        unsaved.setTitle("新新闻");
        unsaved.setContent("内容");

        when(newsRepository.save(any(News.class))).thenAnswer(inv -> {
            News n = inv.getArgument(0);
            n.setId(2L);
            return n;
        });

        News result = newsService.createNews(unsaved);

        assertEquals(0, result.getStatus());
        assertNotNull(result.getId());
    }

    @Test
    void updateNews_shouldUpdateFields() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(sampleNews));
        when(newsRepository.save(any(News.class))).thenReturn(sampleNews);

        News update = new News();
        update.setTitle("更新标题");
        update.setContent("更新内容");

        newsService.updateNews(1L, update);

        assertEquals("更新标题", sampleNews.getTitle());
        assertEquals("更新内容", sampleNews.getContent());
    }

    @Test
    void deleteNews_shouldCallRepository() {
        newsService.deleteNews(1L);
        verify(newsRepository).deleteById(1L);
    }

    @Test
    void getPublishedNews_shouldReturnEmpty_whenPageOutOfRange() {
        Page<News> emptyPage = new PageImpl<>(Collections.emptyList());
        when(newsRepository.findByStatusOrderByCreatedAtDesc(eq(1), any(Pageable.class)))
                .thenReturn(emptyPage);

        PageResponse<News> result = newsService.getPublishedNews(999, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getList().isEmpty());
    }

    @Test
    void updateNews_shouldThrow_whenNotFound() {
        when(newsRepository.findById(999L)).thenReturn(Optional.empty());

        News update = new News();
        update.setTitle("更新标题");

        assertThrows(RuntimeException.class, () -> newsService.updateNews(999L, update));
    }
}
