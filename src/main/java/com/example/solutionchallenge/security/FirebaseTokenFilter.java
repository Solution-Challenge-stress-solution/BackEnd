package com.example.solutionchallenge.security;

import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.util.NoSuchElementException;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final FirebaseAuth firebaseAuth;

    //FirebaseAuth 주입받는 생성자
    public FirebaseTokenFilter(UserDetailsService userDetailsService, FirebaseAuth firebaseAuth) {
        this.userDetailsService = userDetailsService;
        this.firebaseAuth = firebaseAuth;  //FirebaseAuth 초기화
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // get the token from the request
        FirebaseToken decodedToken;
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            setUnauthorizedResponse(response, "INVALID_HEADER");
            return;
        }
        String token = header.substring(7);

        // Firbase Auth 사용해서 IdToken 인증
        try {
            decodedToken = firebaseAuth.verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            setUnauthorizedResponse(response, "INVALID_TOKEN");
            return;
        }

        // User 가져와서 SecurityContext에 저장
        try {
            UserDetails user = userDetailsService.loadUserByUsername(decodedToken.getUid());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (UsernameNotFoundException e) {
            setUnauthorizedResponse(response, "USER_NOT_FOUND");
            return;
        }
        filterChain.doFilter(request, response);
    }

    //Unauthorized 응답을 설정하는 메소드
    private void setUnauthorizedResponse(HttpServletResponse response, String code) throws IOException {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\":\"" + code + "\"}");
    }
}
