package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/image")
    public ApiResponse<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ApiResponse.error("文件不能为空");
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ApiResponse.error("只支持图片文件");
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;

            // 确保目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            // 返回访问URL
            String url = "/uploads/" + filename;
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("filename", filename);

            return ApiResponse.success(data);
        } catch (IOException e) {
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        }
    }
}
