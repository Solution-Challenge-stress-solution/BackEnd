package com.example.solutionchallenge.app.auth.domain.oauth2.firebase;

import com.example.solutionchallenge.app.user.entity.Gender;
import com.example.solutionchallenge.app.user.entity.Status;
import com.example.solutionchallenge.app.user.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class FirebaseUserDetails implements UserDetails {

    private String uid;  // Firebase에서 받아온 고유 식별자
    private String email;
    private String name;
    private String profileImage;
    private Gender gender;
    private int age;
    private Status status;

    // Users 엔티티와의 연관
    private Users user;

    //생성자
    public FirebaseUserDetails(String uid, String email, String name, String profileImage,
                               Gender gender, int age, Status status, Users user) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.gender = gender;
        this.age = age;
        this.status = status;
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자 권한 설정
        // "ROLE_USER" 권한을 부여
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;  // Firebase에서는 패스워드를 사용하지 않음 -> null 반환
    }

    @Override
    public String getUsername() {
        return email;  // Firebase로부터 받아온 이메일을 사용하여 사용자를 식별
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 사용자 계정 만료 여부 반환
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 사용자 계정 잠김 여부 반환
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 사용자 계정 자격 증명 정보 만료 여부 반환
    }

    @Override
    public boolean isEnabled() {
        return true;  // 사용자 계정 활성화 여부 반환
    }

    //Uid getter 메소드
    public String getUid() {
        return uid; //firebase에서 받아오 고유식별자 반환
    }

    //Users Entity를 위한 getter 메소드
    public Users getUser() {
        return user;
    }
}
