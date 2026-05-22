package com.company.miniprogram.service;

import com.company.miniprogram.dto.PageResponse;
import com.company.miniprogram.model.Announcement;
import com.company.miniprogram.repository.AnnouncementRepository;
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
class AnnouncementServiceTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementService announcementService;

    private Announcement sampleAnnouncement;

    @BeforeEach
    void setUp() {
        sampleAnnouncement = new Announcement();
        sampleAnnouncement.setId(1L);
        sampleAnnouncement.setTitle("测试公告");
        sampleAnnouncement.setContent("公告内容");
        sampleAnnouncement.setIsTop(false);
        sampleAnnouncement.setStatus(1);
    }

    @Test
    void getAnnouncements_shouldReturnPageResponse() {
        Page<Announcement> page = new PageImpl<>(List.of(sampleAnnouncement));
        when(announcementRepository.findByStatusOrderByIsTopDescCreatedAtDesc(eq(1), any(Pageable.class)))
                .thenReturn(page);

        PageResponse<Announcement> result = announcementService.getAnnouncements(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
        assertEquals("测试公告", result.getList().get(0).getTitle());
    }

    @Test
    void getAnnouncements_shouldReturnEmpty_whenNoData() {
        Page<Announcement> emptyPage = new PageImpl<>(Collections.emptyList());
        when(announcementRepository.findByStatusOrderByIsTopDescCreatedAtDesc(eq(1), any(Pageable.class)))
                .thenReturn(emptyPage);

        PageResponse<Announcement> result = announcementService.getAnnouncements(1, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getList().isEmpty());
    }

    @Test
    void createAnnouncement_shouldSetDefaultsAndSave() {
        Announcement unsaved = new Announcement();
        unsaved.setTitle("新公告");
        unsaved.setContent("内容");

        when(announcementRepository.save(any(Announcement.class))).thenAnswer(inv -> {
            Announcement a = inv.getArgument(0);
            a.setId(2L);
            return a;
        });

        Announcement result = announcementService.createAnnouncement(unsaved);

        assertEquals(1, result.getStatus());
        assertFalse(result.getIsTop());
        assertNotNull(result.getId());
    }

    @Test
    void createAnnouncement_shouldKeepProvidedValues() {
        Announcement unsaved = new Announcement();
        unsaved.setTitle("置顶公告");
        unsaved.setContent("内容");
        unsaved.setIsTop(true);
        unsaved.setStatus(1);

        when(announcementRepository.save(any(Announcement.class))).thenAnswer(inv -> {
            Announcement a = inv.getArgument(0);
            a.setId(3L);
            return a;
        });

        Announcement result = announcementService.createAnnouncement(unsaved);

        assertTrue(result.getIsTop());
        assertEquals(1, result.getStatus());
    }

    @Test
    void updateAnnouncement_shouldUpdateFields() {
        when(announcementRepository.findById(1L)).thenReturn(Optional.of(sampleAnnouncement));
        when(announcementRepository.save(any(Announcement.class))).thenReturn(sampleAnnouncement);

        Announcement update = new Announcement();
        update.setTitle("更新标题");
        update.setContent("更新内容");
        update.setIsTop(true);
        update.setStatus(0);

        Announcement result = announcementService.updateAnnouncement(1L, update);

        assertEquals("更新标题", result.getTitle());
        assertEquals("更新内容", result.getContent());
        assertTrue(result.getIsTop());
        assertEquals(0, result.getStatus());
    }

    @Test
    void updateAnnouncement_shouldThrow_whenNotFound() {
        when(announcementRepository.findById(999L)).thenReturn(Optional.empty());

        Announcement update = new Announcement();
        update.setTitle("更新");

        assertThrows(RuntimeException.class, () -> announcementService.updateAnnouncement(999L, update));
    }

    @Test
    void deleteAnnouncement_shouldCallRepository() {
        announcementService.deleteAnnouncement(1L);

        verify(announcementRepository).deleteById(1L);
    }
}
