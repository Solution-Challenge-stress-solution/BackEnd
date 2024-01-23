package com.example.solutionchallenge.app.auth.controller;

import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.common.constant.ErrorCode;
import com.example.solutionchallenge.app.auth.dto.request.OauthRequestDto;
import com.example.solutionchallenge.app.auth.dto.response.OauthResponseDto;
import com.example.solutionchallenge.app.user.dto.RefreshTokenResponseDto;
import com.example.solutionchallenge.app.auth.service.OauthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("/login/oauth/{provider}")
    public OauthResponseDto login(@PathVariable String provider, @RequestBody OauthRequestDto oauthRequestDto,
                                  HttpServletResponse response) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        switch (provider) {
            case "kakao":
                String accessToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), response);
                oauthResponseDto.setAccessToken(accessToken);
        }
        return oauthResponseDto;
    }

    // 리프레시 토큰으로 액세스토큰 재발급 받는 로직
    @PostMapping("/token/refresh")
    public RefreshTokenResponseDto tokenRefresh(HttpServletRequest request) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();
        if(list == null) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(Collectors.toList()).get(0);

        if(refreshTokenCookie == null) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponseDto.setAccessToken(accessToken);
        return refreshTokenResponseDto;
    }
}

