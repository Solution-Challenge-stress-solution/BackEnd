package com.example.solutionchallenge.security;

import com.example.solutionchallenge.domain.users.Gender;
import com.example.solutionchallenge.domain.users.Status;
import com.example.solutionchallenge.domain.users.Users;
import com.example.solutionchallenge.repository.UsersRepository;
import com.example.solutionchallenge.security.FirebaseUserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class FirebaseUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final FirebaseAuth firebaseAuth;

    public FirebaseUserDetailsService(UsersRepository usersRepository, FirebaseAuth firebaseAuth) {
        this.usersRepository = usersRepository;
        this.firebaseAuth = firebaseAuth;
    }

    //Firebase의 UID를 기반으로 사용자 정보를 로드하는 메소드
    //사용자 정보가 있는 경우 해당정보를 업데이트하고, 없는 경우는 새로운 사용자를 생성하는 로직을 구현하고자 함
    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        //FirebaseAuth 통해 Firebase와 연동, verifyIdToken 통해 Firebase토큰 검증
        Map<String, Object> claims = null;
        try {
            FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(uid);
            claims = firebaseToken.getClaims();
        } catch (FirebaseAuthException e) {
            throw new UsernameNotFoundException("Failed to verify Firebase token for UID: " + uid);
        }

        //UID를 기반으로 사용자 정보 조회
        Optional<Users> optionalUser = usersRepository.findByFirebaseUid(uid);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            //필요한 정보 업데이트
            if (claims.containsKey("email")) {
                user.changeEmail((String) claims.get("email"));
            }
            if (claims.containsKey("name")) {
                user.changeName((String) claims.get("name"));
            }
            if (claims.containsKey("profileImage")) {
                user.changeProfileImage((String) claims.get("profileImage"));
            }
            if (claims.containsKey("gender")) {
                user.changeGender(Gender.valueOf((String) claims.get("gender")));
            }
            if (claims.containsKey("age")) {
                user.changeAge((Integer) claims.get("age"));
            }
            if (claims.containsKey("status")) {
                user.changeStatus(Status.valueOf((String) claims.get("status")));
            }

            usersRepository.save(user);

            //사용자 인증 정보 생성
            //조회된 사용자 정보를 기반으로 FirebaseUserDetails 객체를 생성하여 반환
            return new FirebaseUserDetails(
                    user.getFirebaseUid(),
                    user.getEmail(),
                    user.getName(),
                    user.getProfileImage(),
                    user.getGender(),
                    user.getAge(),
                    user.getStatus(),
                    user
            );
        } else {
            //사용자 정보가 없다면 새로 생성
            Users newUser = new Users();
            newUser.setFirebaseUid(uid);

            //필요한 정보 설정
            if (claims.containsKey("name")) {
                newUser.changeName((String) claims.get("name"));
            }
            if (claims.containsKey("email")) {
                newUser.changeEmail((String) claims.get("email"));
            }
            if (claims.containsKey("profileImage")) {
                newUser.changeProfileImage((String) claims.get("profileImage"));
            }
            if (claims.containsKey("gender")) {
                newUser.changeGender(Gender.valueOf((String) claims.get("gender")));
            }
            if (claims.containsKey("age")) {
                newUser.changeAge((Integer) claims.get("age"));
            }
            if (claims.containsKey("status")) {
                newUser.changeStatus(Status.valueOf((String) claims.get("status")));
            }

            usersRepository.save(newUser);

            //사용자 인증 정보 생성
            //새로 생성된 사용자 정보를 기반으로 FirebaseUserDetails 객체를 생성하여 반환
            return new FirebaseUserDetails(
                    newUser.getFirebaseUid(),
                    newUser.getEmail(),
                    newUser.getName(),
                    newUser.getProfileImage(),
                    newUser.getGender(),
                    newUser.getAge(),
                    newUser.getStatus(),
                    newUser
            );
        }
    }
}
