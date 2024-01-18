package com.example.solutionchallenge.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.solutionchallenge.service.KakaoService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private KakaoService kakaoService;

    @RequestMapping("test")
    @ResponseBody
    public String testConnect() {
        return "연결 성공";
    }

    @RequestMapping("kakao/sign_in")
    public String kakaoSignIn(@RequestParam("code") String code, HttpServletRequest request) {
        Map<String, Object> result = kakaoService.execKakaoLogin(code);
        String customToken = (String) request.getSession().getAttribute("customToken");
        return "redirect:webauthcallback://success?customToken=" + customToken;
    }
}