package com.example.solutionchallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.setAllowCredentials(true); // 서버 응답 시 json 자바스크립트에서 처리 허용
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*"); // 모든 header 응답 허용
        configuration.addAllowedMethod("*"); // 모든 post, get, put, delete, patch 요청 허용

        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}