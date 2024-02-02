package com.example.solutionchallenge.app.auth.domain.oauth2.kakao;

import java.util.Map;

import com.example.solutionchallenge.app.user.dto.KakaoInfoDto;
import com.example.solutionchallenge.app.user.dto.UserDto;

import com.example.solutionchallenge.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final UserService userService;

    // 카카오API 호출해서 AccessToken으로 유저정보 가져오기
    public Map<String, Object> getUserAttributesByToken(String accessToken){
        System.out.println("33333333333333333333333333333333333");

        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

//        return WebClient.create()
//                .get()
//                .uri("https://kapi.kakao.com/v2/user/me")
//                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
//                .block();

    }

    // 카카오API에서 가져온 유저정보를 DB에 저장
    public UserDto getUserProfileByToken(String accessToken){
        System.out.println("accessToken2: " + accessToken);
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);
        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);
        UserDto userDto = UserDto.builder()
                .id(kakaoInfoDto.getId())
                .email(kakaoInfoDto.getEmail())
                .platform("kakao")
                .build();

        System.out.println("email: " + userDto.getEmail());
        if(userService.findById(userDto.getId()) != null) {
            userService.update(userDto);
        } else {
            userService.save(userDto);
        }
        return userDto;
    }
}
