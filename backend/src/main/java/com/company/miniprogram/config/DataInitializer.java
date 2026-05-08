package com.company.miniprogram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        System.out.println("===========================================");
        System.out.println("企业管理小程序后端启动成功！");
        System.out.println("管理后台地址: http://localhost:8080/admin/index.html");
        System.out.println("默认管理员账号: " + adminUsername);
        System.out.println("默认管理员密码: " + adminPassword);
        System.out.println("===========================================");
    }
}
