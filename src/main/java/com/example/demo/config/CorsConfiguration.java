package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*") // Cho phép truy cập từ origin http://localhost:3000
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Cho phép các phương thức yêu cầu
                        .allowCredentials(true); // Cho phép gửi cookie khi yêu cầu
            }
        };
    }
}
