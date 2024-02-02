package com.example.solutionchallenge.app.auth.domain.jwt;

import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.common.constant.ErrorCode;
import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.model.UserPrincipal;
import com.example.solutionchallenge.app.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final UserService userService;


    //Filter 에서 액세스토큰이 유효한지 확인 후 SecurityContext에 계정정보 저장
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        System.out.println(((HttpServletRequest) servletRequest).getRequestURL());
        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());
        String jwt = resolveToken(httpServletRequest);
        System.out.println("jwt: " + jwt);

       if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
            Long userId = Long.valueOf(jwtTokenService.getPayload(jwt)); // 토큰에 있는 userId 가져오기
            UserDto user = userService.findById(userId); // userId로
            if(user == null) {
                throw new ApiException(ErrorCode.NOT_EXIST_USER);
            }
            UserDetails userDetails = UserPrincipal.create(user);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new ApiException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());
        String jwt = resolveToken(httpServletRequest);

        if (StringUtils.hasText(jwt)) {
            if (jwtTokenService.validateToken(jwt)) {
                String payload = jwtTokenService.getPayload(jwt);
                if (payload != null) {
                    try {
                        Long userId = Long.valueOf(payload); // 토큰에 있는 userId 가져오기
                        UserDto user = userService.findById(userId); // userId로
                        if (user != null) {
                            UserDetails userDetails = UserPrincipal.create(user);
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            logger.error("User not found with id: " + userId);
                            throw new ApiException(ErrorCode.NOT_EXIST_USER);
                        }
                    } catch (NumberFormatException e) {
                        logger.error("Invalid user id in JWT token");
                        throw new ApiException(ErrorCode.INVALID_ACCESS_TOKEN);
                    }
                } else {
                    logger.error("Payload is null");
                    throw new ApiException(ErrorCode.INVALID_ACCESS_TOKEN);
                }
            } else {
                logger.error("Invalid JWT token");
                throw new ApiException(ErrorCode.INVALID_ACCESS_TOKEN);
            }
        } else {
            logger.error("JWT token is missing");
            throw new ApiException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


    // Header에서 Access Token 가져오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}