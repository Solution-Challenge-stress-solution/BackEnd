package com.example.solutionchallenge.app.auth.controller;

import com.example.solutionchallenge.app.auth.domain.oauth2.kakao.KakaoApi;
import com.example.solutionchallenge.app.auth.domain.oauth2.kakao.KakaoProfile;
import com.example.solutionchallenge.app.auth.dto.request.OauthRequestDto;
import com.example.solutionchallenge.app.auth.dto.response.OauthResponseDto;
import com.example.solutionchallenge.app.common.exception.ApiException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OauthController {
    @ResponseBody
    @GetMapping("/kakao")
    public void  kakaoCallback(@RequestParam String code) throws ApiException {

        String access_Token = KakaoApi.getKaKaoAccessToken(code);
        KakaoApi.createKakaoUser(access_Token);

    }


}
