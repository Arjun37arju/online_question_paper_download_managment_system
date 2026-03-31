package com.online_question_paper.online_question_paper_download_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // Admin login
        registry.addViewController("/admin/login")
                .setViewName("admin-login");

        // ✅ Principal login
        registry.addViewController("/principal/login")
                .setViewName("principal-login");

        // OTP page
        registry.addViewController("/principal/otp")
                .setViewName("principal-otp");
    }
}