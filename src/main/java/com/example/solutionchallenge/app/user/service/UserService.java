package com.example.solutionchallenge.app.user.service;

import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.mapper.UserMapper;
import com.example.solutionchallenge.app.user.repository.UsersRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private AtomicInteger dbCount = new AtomicInteger(0);

    public void save(UserDto userDto) {
        userMapper.save(userDto);
    }

    public UserDto findById(Long id) {
        return userMapper.findById(id);
    }

    public UserDto findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public void update(UserDto userDto) {
        UserDto existingUser = findByEmail(userDto.getEmail());

        if (existingUser == null) {
            save(userDto);
        } else {
            existingUser.setName(userDto.getName());
            existingUser.setProfileImage(userDto.getProfileImage());
            existingUser.setGender(userDto.getGender());
            existingUser.setAge(userDto.getAge());
            existingUser.setPlatform(userDto.getPlatform());
            userMapper.update(existingUser);
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
}