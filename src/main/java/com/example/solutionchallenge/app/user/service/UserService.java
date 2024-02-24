package com.example.solutionchallenge.app.user.service;

import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenProvider;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.dto.UserResponseDto;
import com.example.solutionchallenge.app.user.repository.UsersRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private AtomicInteger dbCount = new AtomicInteger(0);

    @Transactional(readOnly = true)
    public UserResponseDto findById(String accessToken) {
        Users user = getUserByToken(accessToken);
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfileImage()).build();
    }

    public Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }


    @Cacheable(key = "#size", value = "getUsers")
    public List<Users> getUsers(String size) {
        dbCount.incrementAndGet();
        ArrayList<Users> users = new ArrayList<Users>();
        int count = Integer.parseInt(size);

        for (int i = 0; i < count; i++) {
            users.add(new Users());
        }

        return users;
    }
    public int getDbCount() {
        return dbCount.get();
    }

    public void deleteUser(String accessToken) {
        Users user = getUserByToken(accessToken);
        usersRepository.delete(user);
    }

}