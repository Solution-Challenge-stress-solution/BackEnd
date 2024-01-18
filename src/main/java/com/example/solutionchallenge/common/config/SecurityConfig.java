package com.example.solutionchallenge.common.config;

import com.example.solutionchallenge.common.config.response.JwtAuthFilter;
import com.example.solutionchallenge.common.oauth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // 세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 회원가입, 로그인 관련 API는 Jwt 인증 없이 접근 가능
                .requestMatchers("/api/auth/**").permitAll()
                // 나머지 모든 API는 Jwt 인증 필요
                .anyRequest().authenticated()
                .and()
                // Http 요청에 대한 Jwt 유효성 선 검사
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
