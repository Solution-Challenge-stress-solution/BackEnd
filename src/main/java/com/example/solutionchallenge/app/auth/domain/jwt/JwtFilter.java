package com.example.solutionchallenge.app.auth.domain.jwt;

import com.example.solutionchallenge.app.auth.domain.constant.ErrorCode;
import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.repository.UsersRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws RuntimeException, ServletException, IOException {
        String token = resolveToken((HttpServletRequest) request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (token == null) {
                throw new ApiException(ErrorCode.UNKNOWN_ERROR);
            }
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new ApiException(ErrorCode.WRONG_TYPE_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new ApiException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.UNKNOWN_ERROR);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}