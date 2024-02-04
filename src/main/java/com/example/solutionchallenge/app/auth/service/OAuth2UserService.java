package com.example.solutionchallenge.app.auth.service;

import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import com.example.solutionchallenge.app.user.domain.Gender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //유저정보를 담은 객체를 반환
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return saveOrUpdate(oAuth2User, userRequest);
    }


    private OAuth2User saveOrUpdate(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String platform = userRequest.getClientRegistration().getRegistrationId(); // OAuth2 서비스 제공자를 플랫폼으로 사용
        String profileImage = (String) attributes.get("picture");
        //Gender gender = (Gender) attributes.get("gender");
        //int age = (int) attributes.get("age");

        //이메일을 이용해 사용자 찾기
        UserDto userDto = userService.findByEmail(email);


        //사용자가 db에 없으면 userDto생성 후 저장, 있으면 업데이트
        if (userDto == null) {
            //정보가 없을 경우 기본 설정사항
            String defaultProfileImage = "/resources/default.jpg";
            Gender defaultGender = Gender.UNKNOWN;
            int defaultAge = 0;

            userDto = UserDto.builder()
                    .email(email)
                    .name(name)
                    .profileImage(profileImage != null ? profileImage : defaultProfileImage)
                    //.gender(attributes.get("gender") != null ? (Gender) attributes.get("gender") : defaultGender)
                    //.age(attributes.get("age") != null ? (int) attributes.get("age") : defaultAge)
                    .platform(platform)
                    .build();
            userService.save(userDto);
        } else {
            userDto.setName(name);
            userDto.setProfileImage(profileImage);
            //userDto.setGender(gender);
            //userDto.setAge(age);
            userDto.setPlatform(platform);
            userService.update(userDto);
        }

        return oAuth2User;
    }

}
