package com.example.solutionchallenge.app.user.dto;

import com.example.solutionchallenge.app.user.domain.Gender;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String profileImage;
    private Gender gender;
    private int age;
    private String platform;
    private String accessToken;
    private String refreshToken;
}
