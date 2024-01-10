package com.example.solutionchallenge.controller;

import com.example.solutionchallenge.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/")
public class MemberController {

    private final MemberService memberService;
}
