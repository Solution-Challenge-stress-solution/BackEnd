package com.example.solutionchallenge.service;

import com.example.solutionchallenge.common.oauth.provider.KakaoUser;
import com.example.solutionchallenge.dto.KakaoUserInfoResponse;
import com.example.solutionchallenge.repository.UserRepository;
import jakarta.transaction.Transactional;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KakaoAuthService {

    private final KakaoUser kakaoUserInfo;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long isSignedUp(String token) {
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        User user = userRepository.findByKeyCode(userInfo.getId().toString()).orElseThrow(() -> new ExecutionControl.UserException(ResponseCode.USER_NOT_FOUND));
        return user.getId();
    }
}