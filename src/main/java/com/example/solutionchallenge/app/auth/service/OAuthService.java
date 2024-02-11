package com.example.solutionchallenge.app.auth.service;

import com.example.solutionchallenge.app.auth.domain.google.Constant;
import com.example.solutionchallenge.app.auth.domain.google.GoogleOAuthToken;
import com.example.solutionchallenge.app.auth.domain.google.GoogleOauth;
import com.example.solutionchallenge.app.auth.domain.google.GoogleUser;
import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenGenerator;
import com.example.solutionchallenge.app.auth.domain.jwt.TokenInfoDto;
import com.example.solutionchallenge.app.auth.domain.oauth2.kakao.KakaoApi;
import com.example.solutionchallenge.app.auth.domain.oauth2.kakao.KakaoProfile;
import com.example.solutionchallenge.app.user.domain.Status;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.repository.UsersRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final UsersRepository usersRepository;
    private final KakaoApi kakaoApi;

    public void request(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                redirectURL = googleOauth.getOauthRedirectURL();
            }
            break;
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
        response.sendRedirect(redirectURL);
    }

    @Transactional
    public TokenInfoDto oAuthLogin(Constant.SocialLoginType socialLoginType, String code) throws IOException {
        //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
        ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
        //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
        GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);

        //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
        ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
        //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
        GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

        Optional<Users> user = usersRepository.findByEmail(googleUser.getEmail());
        if (user.isEmpty()) {
            Users newUser = Users.builder()
                    .email(googleUser.email)
                    .name(googleUser.getName())
                    .profileImage(googleUser.getPicture())
                    .build();
            usersRepository.save(newUser);
        } else {
            user.get().update(googleUser.getName(), googleUser.getEmail(), googleUser.getPicture());
        }

        Users users = usersRepository.findByEmail(googleUser.getEmail()).get();
        TokenInfoDto tokenInfoDto = jwtTokenGenerator.generate(users.getId());
        users.updateToken(tokenInfoDto.getRefreshToken());
        return tokenInfoDto;
    }

    @Transactional
    public TokenInfoDto kakaoOAuthLogin(String code) throws IOException {
        // 카카오로부터 일회성 코드를 보내 액세스 토큰을 받아온다.
        String kakaoToken = kakaoApi.getKaKaoAccessToken(code);
        // 액세스 토큰을 다시 카카오로 보내 사용자 정보를 받아온다.
        KakaoProfile kakaoProfile = kakaoApi.createKakaoUser(kakaoToken);
        // JSON 형식의 응답 객체를 KakaoProfile 객체로 변환한다.
//        KakaoProfile kakaoProfile = new KakaoProfile(kakaoToken);

        Optional<Users> user = usersRepository.findByEmail(kakaoProfile.getEmail());
        if (user.isEmpty()) {
            Users newUser = Users.builder()
                    .email(kakaoProfile.getEmail())
                    .name(kakaoProfile.getNickname())
                    .profileImage(kakaoProfile.getProfileImage())
                    .build();
            usersRepository.save(newUser);
        } else {
            user.get().update(kakaoProfile.getNickname(), kakaoProfile.getEmail(), kakaoProfile.getProfileImage());
        }

        Users users = usersRepository.findByEmail(kakaoProfile.getEmail()).get();
        TokenInfoDto tokenInfoDto = jwtTokenGenerator.generate(users.getId());
        users.updateToken(tokenInfoDto.getRefreshToken());
        return tokenInfoDto;
    }
}

