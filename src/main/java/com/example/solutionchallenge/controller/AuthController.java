package com.example.solutionchallenge.controller;

import com.example.solutionchallenge.repository.MemberRepository;
import com.example.solutionchallenge.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("auth")
public class AuthController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    @PostMapping("join")
    public ResponseEntity<Void> join(@RequestBody Member member) {

        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword())); //나중에 회원가입 구현 예정
        member.setRole("ROLE_USER");
        memberRepository.save(member);

        return new ResponseEntity(HttpStatus.OK);
    }
}
