package com.example.solutionchallenge.app.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

    @GetMapping("/login/oauth/google")
    public String login(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        System.out.println("User email: " + email);
        return "로그인 성공! 사용자 이메일: " + email;
    }
}
