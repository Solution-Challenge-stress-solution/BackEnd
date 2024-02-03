package com.example.solutionchallenge.app.auth.service;

import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenProvider;
import com.example.solutionchallenge.app.auth.domain.refreshToken.RefreshToken;
import com.example.solutionchallenge.app.auth.domain.refreshToken.RefreshTokenRepository;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    public String createAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        Long userId = findByRefreshToken(refreshToken).getUserId();
        UserDto userDto = userService.findById(userId); // UserService의 findById 메소드가 UserDto를 반환하도록 변경
        Date expirationDate = new Date(System.currentTimeMillis() + Duration.ofHours(2).toMillis());

        return tokenProvider.createToken(expirationDate, userDto); // JwtTokenProvider의 createToken 메소드가 UserDto
    }
}