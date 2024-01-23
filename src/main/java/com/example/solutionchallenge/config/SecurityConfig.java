package com.example.solutionchallenge.config;

import com.example.solutionchallenge.app.handler.ExceptionHandlerFilter;
import com.example.solutionchallenge.app.user.entity.UserRole;
import com.example.solutionchallenge.app.auth.domain.jwt.JwtFilter;
import com.example.solutionchallenge.app.auth.domain.oauth2.firebase.FirebaseTokenFilter;
import com.example.solutionchallenge.app.auth.domain.oauth2.firebase.FirebaseUserDetailsService;
import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenService;
import com.example.solutionchallenge.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final FirebaseTokenFilter firebaseTokenFilter;
    private final FirebaseUserDetailsService firebaseUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login/**", "/token/refresh").permitAll()
                        .requestMatchers("/user/**").hasAuthority(UserRole.USER.getRole())
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable()) // 로그인 폼 미사용
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()) // http basic 미사용
                .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class) //FirebaseTokenFilter 추가
                .addFilterBefore(new JwtFilter(jwtTokenService, userService), UsernamePasswordAuthenticationFilter.class) // JWT Filter 추가
                .addFilterBefore(new ExceptionHandlerFilter(), JwtFilter.class) // Security Filter 에서 CustomException 사용하기 위해 추가
                .userDetailsService(firebaseUserDetailsService) //FirebaseUserDetailsSErvice 설정
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // 아래 url은 filter 에서 제외
        return web ->
                web.ignoring()
                        .requestMatchers("/login/**", "/token/refresh");
    }
}