package com.example.solutionchallenge.app.auth.controller;

import com.example.solutionchallenge.app.auth.domain.oauth2.kakao.KakaoApiService;
import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.auth.domain.constant.ErrorCode;
import com.example.solutionchallenge.app.auth.dto.request.OauthRequestDto;
import com.example.solutionchallenge.app.auth.dto.response.OauthResponseDto;
import com.example.solutionchallenge.app.user.dto.RefreshTokenResponseDto;
import com.example.solutionchallenge.app.auth.service.OauthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.example.solutionchallenge.app.auth.domain.constant.ErrorCode.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;
    private final KakaoApiService kakao;

    @PostMapping("/login/oauth/{provider}")
    public OauthResponseDto login(@PathVariable String provider, @RequestBody OauthRequestDto oauthRequestDto,
                                  HttpServletResponse response) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        switch (provider) {
            case "kakao":
                System.out.println("1111111111111111111");
                String accessToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), response);
                oauthResponseDto.setAccessToken(accessToken);
                break;
            default:
                throw new ApiException(UNAUTHORIZED);
        }
        return oauthResponseDto;
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login(@RequestParam("code") String code) {
        String access_Token = kakao.getAccessToken(code);
        System.out.println("controller access_token : " + access_Token);
        return "index";
    }

    // 리프레시 토큰으로 액세스토큰 재발급 받는 로직
    @PostMapping("/token/refresh")
    public RefreshTokenResponseDto tokenRefresh(HttpServletRequest request) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();
        if (list == null) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token"))
                .collect(Collectors.toList()).get(0);

        if (refreshTokenCookie == null) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponseDto.setAccessToken(accessToken);
        return refreshTokenResponseDto;
    }
}
