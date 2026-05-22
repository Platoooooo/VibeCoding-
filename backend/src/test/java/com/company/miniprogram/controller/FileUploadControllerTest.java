package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadControllerTest {

    @InjectMocks
    private FileUploadController fileUploadController;

    @Test
    void uploadImage_shouldReturnError_whenFileEmpty() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", new byte[0]);

        ApiResponse<Map<String, String>> result = fileUploadController.uploadImage(emptyFile);

        assertEquals(-1, result.getCode());
        assertEquals("文件不能为空", result.getMessage());
    }

    @Test
    void uploadImage_shouldReturnError_whenNotImage() {
        MockMultipartFile textFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello".getBytes());

        ApiResponse<Map<String, String>> result = fileUploadController.uploadImage(textFile);

        assertEquals(-1, result.getCode());
        assertEquals("只支持图片文件", result.getMessage());
    }

    @Test
    void uploadImage_shouldReturnError_whenContentTypeNull() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", null, "content".getBytes());

        ApiResponse<Map<String, String>> result = fileUploadController.uploadImage(file);

        assertEquals(-1, result.getCode());
        assertEquals("只支持图片文件", result.getMessage());
    }

    @Test
    void uploadImage_shouldSucceed_whenValidImage() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "photo.jpg", "image/jpeg", "image-data".getBytes());

        try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class);
             MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {

            Path mockUploadPath = mock(Path.class);
            mockedPaths.when(() -> Paths.get("uploads/")).thenReturn(mockUploadPath);
            mockedFiles.when(() -> Files.exists(mockUploadPath)).thenReturn(true);

            Path mockFilePath = mock(Path.class);
            when(mockUploadPath.resolve(anyString())).thenReturn(mockFilePath);
            mockedFiles.when(() -> Files.write(eq(mockFilePath), any(byte[].class))).thenReturn(mockFilePath);

            ApiResponse<Map<String, String>> result = fileUploadController.uploadImage(imageFile);

            assertEquals(0, result.getCode());
            assertNotNull(result.getData());
            assertTrue(result.getData().get("url").startsWith("/uploads/"));
            assertNotNull(result.getData().get("filename"));
        }
    }

    @Test
    void uploadImage_shouldKeepOriginalExtension() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "photo.PNG", "image/png", "png-data".getBytes());

        try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class);
             MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {

            Path mockUploadPath = mock(Path.class);
            mockedPaths.when(() -> Paths.get("uploads/")).thenReturn(mockUploadPath);
            mockedFiles.when(() -> Files.exists(mockUploadPath)).thenReturn(true);

            Path mockFilePath = mock(Path.class);
            when(mockUploadPath.resolve(anyString())).thenReturn(mockFilePath);
            mockedFiles.when(() -> Files.write(eq(mockFilePath), any(byte[].class))).thenReturn(mockFilePath);

            ApiResponse<Map<String, String>> result = fileUploadController.uploadImage(imageFile);

            assertEquals(0, result.getCode());
            assertTrue(result.getData().get("filename").endsWith(".PNG"));
        }
    }

    @Test
    void uploadImage_shouldCreateDirectory_whenNotExists() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "img.jpg", "image/jpeg", "jpeg-data".getBytes());

        try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class);
             MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {

            Path mockUploadPath = mock(Path.class);
            mockedPaths.when(() -> Paths.get("uploads/")).thenReturn(mockUploadPath);
            mockedFiles.when(() -> Files.exists(mockUploadPath)).thenReturn(false);
            mockedFiles.when(() -> Files.createDirectories(mockUploadPath)).thenReturn(mockUploadPath);

            Path mockFilePath = mock(Path.class);
            when(mockUploadPath.resolve(anyString())).thenReturn(mockFilePath);
            mockedFiles.when(() -> Files.write(eq(mockFilePath), any(byte[].class))).thenReturn(mockFilePath);

            ApiResponse<Map<String, String>> result = fileUploadController.uploadImage(imageFile);

            mockedFiles.verify(() -> Files.createDirectories(mockUploadPath));
            assertEquals(0, result.getCode());
        }
    }
}
