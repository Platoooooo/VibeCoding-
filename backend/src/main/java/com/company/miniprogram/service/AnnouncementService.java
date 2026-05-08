package com.company.miniprogram.service;

import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.Announcement;
import com.company.miniprogram.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public PageResponse<Announcement> getAnnouncements(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Announcement> announcementPage = announcementRepository.findByStatusOrderByIsTopDescCreatedAtDesc(1, pageable);
        return new PageResponse<>(announcementPage.getContent(), announcementPage.getTotalElements(), page, size);
    }

    @Transactional
    public Announcement createAnnouncement(Announcement announcement) {
        if (announcement.getIsTop() == null) {
            announcement.setIsTop(false);
        }
        if (announcement.getStatus() == null) {
            announcement.setStatus(1);
        }
        return announcementRepository.save(announcement);
    }

    @Transactional
    public Announcement updateAnnouncement(Long id, Announcement announcement) {
        Announcement existing = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("公告不存在"));
        existing.setTitle(announcement.getTitle());
        existing.setContent(announcement.getContent());
        existing.setIsTop(announcement.getIsTop());
        existing.setStatus(announcement.getStatus());
        return announcementRepository.save(existing);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}
