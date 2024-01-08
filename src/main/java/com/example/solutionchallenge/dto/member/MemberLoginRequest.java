package com.example.solutionchallenge.dto.member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginRequest {
    private String loginId;
    private String password;
}
