package com.company.miniprogram.service;

import com.company.miniprogram.model.Banner;
import com.company.miniprogram.repository.BannerRepository;
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
class BannerServiceTest {

    @Mock
    private BannerRepository bannerRepository;

    @InjectMocks
    private BannerService bannerService;

    private Banner sampleBanner;

    @BeforeEach
    void setUp() {
        sampleBanner = new Banner();
        sampleBanner.setId(1L);
        sampleBanner.setImage("/uploads/banner1.jpg");
        sampleBanner.setTitle("首页轮播");
        sampleBanner.setLink("/pages/product/list");
        sampleBanner.setSortOrder(1);
        sampleBanner.setStatus(1);
    }

    @Test
    void getActiveBanners_shouldReturnList() {
        when(bannerRepository.findByStatusOrderBySortOrderAsc(1))
                .thenReturn(List.of(sampleBanner));

        List<Banner> result = bannerService.getActiveBanners();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("首页轮播", result.get(0).getTitle());
    }

    @Test
    void getActiveBanners_shouldReturnEmpty_whenNoData() {
        when(bannerRepository.findByStatusOrderBySortOrderAsc(1))
                .thenReturn(Collections.emptyList());

        List<Banner> result = bannerService.getActiveBanners();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createBanner_shouldSetDefaultsAndSave() {
        Banner unsaved = new Banner();
        unsaved.setImage("/uploads/new.jpg");
        unsaved.setTitle("新轮播");

        when(bannerRepository.save(any(Banner.class))).thenAnswer(inv -> {
            Banner b = inv.getArgument(0);
            b.setId(2L);
            return b;
        });

        Banner result = bannerService.createBanner(unsaved);

        assertEquals(0, result.getSortOrder());
        assertEquals(1, result.getStatus());
        assertNotNull(result.getId());
    }

    @Test
    void createBanner_shouldKeepProvidedSortOrder() {
        Banner unsaved = new Banner();
        unsaved.setImage("/uploads/custom.jpg");
        unsaved.setTitle("自定义排序");
        unsaved.setSortOrder(5);
        unsaved.setStatus(1);

        when(bannerRepository.save(any(Banner.class))).thenAnswer(inv -> {
            Banner b = inv.getArgument(0);
            b.setId(3L);
            return b;
        });

        Banner result = bannerService.createBanner(unsaved);

        assertEquals(5, result.getSortOrder());
    }

    @Test
    void updateBanner_shouldUpdateFields() {
        when(bannerRepository.findById(1L)).thenReturn(Optional.of(sampleBanner));
        when(bannerRepository.save(any(Banner.class))).thenReturn(sampleBanner);

        Banner update = new Banner();
        update.setImage("/uploads/updated.jpg");
        update.setTitle("更新标题");
        update.setLink("/pages/new");
        update.setSortOrder(10);
        update.setStatus(0);

        Banner result = bannerService.updateBanner(1L, update);

        assertEquals("更新标题", result.getTitle());
        assertEquals("/uploads/updated.jpg", result.getImage());
        assertEquals(10, result.getSortOrder());
        assertEquals(0, result.getStatus());
    }

    @Test
    void updateBanner_shouldThrow_whenNotFound() {
        when(bannerRepository.findById(999L)).thenReturn(Optional.empty());

        Banner update = new Banner();
        update.setTitle("更新");

        assertThrows(RuntimeException.class, () -> bannerService.updateBanner(999L, update));
    }

    @Test
    void deleteBanner_shouldCallRepository() {
        bannerService.deleteBanner(1L);

        verify(bannerRepository).deleteById(1L);
    }
}
