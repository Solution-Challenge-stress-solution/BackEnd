package com.example.solutionchallenge.app.auth.controller;

import com.example.solutionchallenge.app.auth.dto.request.OauthRequestDto;
import com.example.solutionchallenge.app.auth.dto.response.OauthResponseDto;
import com.example.solutionchallenge.app.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<OauthResponseDto> createNewAccessToken(@RequestBody OauthRequestDto request) {
        String newAccessToken = tokenService.createAccessToken(request.getRefreshToken());
        OauthResponseDto responseDto = new OauthResponseDto();
        responseDto.setAccessToken(newAccessToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }
}

