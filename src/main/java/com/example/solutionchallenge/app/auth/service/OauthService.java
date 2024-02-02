package com.example.solutionchallenge.app.auth.service;

import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenService;
import com.example.solutionchallenge.app.auth.domain.oauth2.kakao.KakaoOauthService;
import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.common.constant.ErrorCode;
import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OauthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final KakaoOauthService kakaoOauthService;

    //카카오 로그인
    public String loginWithKakao(String accessToken, HttpServletResponse response) {
        System.out.println("accessToken: " + accessToken);
        UserDto userDto = kakaoOauthService.getUserProfileByToken(accessToken);
        return getTokens(userDto.getId(), response);
    }

    //액세스토큰, 리프레시토큰 생성
    public String getTokens(Long id, HttpServletResponse response) {
        final String accessToken = jwtTokenService.createAccessToken(id.toString());
        final String refreshToken = jwtTokenService.createRefreshToken();

        UserDto userDto = userService.findById(id);
        userDto.setRefreshToken(refreshToken);
        userService.updateRefreshToken(userDto);

        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
        return accessToken;
    }

    // 리프레시 토큰으로 액세스토큰 새로 갱신
    public String refreshAccessToken(String refreshToken) {
        UserDto userDto = userService.findByRefreshToken(refreshToken);
        if(userDto == null) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if(!jwtTokenService.validateToken(refreshToken)) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return jwtTokenService.createAccessToken(userDto.getId().toString());
    }
}
