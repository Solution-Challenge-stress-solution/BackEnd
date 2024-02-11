package com.example.solutionchallenge.app.auth.controller;

import com.example.solutionchallenge.app.auth.domain.google.Constant.SocialLoginType;
import com.example.solutionchallenge.app.auth.domain.jwt.TokenInfoDto;
import com.example.solutionchallenge.app.auth.service.OAuthService;
import com.example.solutionchallenge.app.common.dto.response.ResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인", description = "로그인 API")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "API 정상 작동"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class OauthController {

    private final OAuthService oAuthService;

    @Operation(summary = "카카오 로그인", description = "로그인 후 받은 code를 입력")
    @GetMapping("/kakao")
    public ResponseDto<TokenInfoDto> kakaoCallback(@Parameter(description = "Kakao authorizationCode") @RequestParam(name = "code") String code) throws IOException {
        return ResponseUtil.SUCCESS("카카오 로그인에 성공하였습니다.", oAuthService.kakaoOAuthLogin(code));
    }

    @Operation(summary = "구글 로그인창 연결")
    @GetMapping("/google")
    public void socialLoginRedirect() throws IOException {
//        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(SocialLoginType.GOOGLE);
    }

    @Operation(summary = "구글 로그인", description = "로그인 후 받은 code를 입력")
    @GetMapping("/google/callback")
    public ResponseDto<TokenInfoDto> callback(@Parameter(description = "Google authorizationCode") @RequestParam(name = "code") String code) throws IOException {
        //코드를 UTF-8로 인코딩 처리해야함
        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
//        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        return ResponseUtil.SUCCESS("구글 로그인에 성공하였습니다.", oAuthService.oAuthLogin(SocialLoginType.GOOGLE, decode));
    }

}
