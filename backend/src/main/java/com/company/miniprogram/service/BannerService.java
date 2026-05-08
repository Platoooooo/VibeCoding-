package com.company.miniprogram.service;

import com.company.miniprogram.model.Banner;
import com.company.miniprogram.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<Banner> getActiveBanners() {
        return bannerRepository.findByStatusOrderBySortOrderAsc(1);
    }

    @Transactional
    public Banner createBanner(Banner banner) {
        if (banner.getSortOrder() == null) {
            banner.setSortOrder(0);
        }
        if (banner.getStatus() == null) {
            banner.setStatus(1);
        }
        return bannerRepository.save(banner);
    }

    @Transactional
    public Banner updateBanner(Long id, Banner banner) {
        Banner existing = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("轮播图不存在"));
        existing.setImage(banner.getImage());
        existing.setTitle(banner.getTitle());
        existing.setLink(banner.getLink());
        existing.setSortOrder(banner.getSortOrder());
        existing.setStatus(banner.getStatus());
        return bannerRepository.save(existing);
    }

    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
