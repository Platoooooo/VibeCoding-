package com.company.miniprogram.controller;

import com.company.miniprogram.dto.ApiResponse;
import com.company.miniprogram.util.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JwtUtil jwtUtil;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginRequest request) {
        if (adminUsername.equals(request.getUsername()) &&
            adminPassword.equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", request.getUsername());

            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "用户名或密码错误");
    }

    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }
}
