package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.model.Announcement;
import com.company.miniprogram.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/announcement")
@RequiredArgsConstructor
public class AdminAnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    public ApiResponse<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        Announcement created = announcementService.createAnnouncement(announcement);
        return ApiResponse.success(created);
    }

    @PutMapping("/{id}")
    public ApiResponse<Announcement> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        Announcement updated = announcementService.updateAnnouncement(id, announcement);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ApiResponse.success();
    }
}
