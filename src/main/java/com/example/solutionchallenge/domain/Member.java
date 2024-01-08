package com.example.solutionchallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;
    private String email;
    private String nickname;
    private String image;
    private String roles; // USER or ADMIN

    @Builder.Default
    private Boolean profilePublic = true; // 프로필 공개 여부
}
