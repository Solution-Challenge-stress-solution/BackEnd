package com.example.solutionchallenge.app.user.service;

import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    public void save(UserDto userDto) {
        userMapper.save(userDto);
    }

    public UserDto findById(Long id) {
        return userMapper.findById(id);
    }
    public UserDto findByRefreshToken(String refreshToken) {
        return userMapper.findByRefreshToken(refreshToken);
    }

    public void update(UserDto userDto) {
        userMapper.update(userDto);
    }

    public void updateRefreshToken(UserDto userDto) {
        userMapper.updateRefreshToken(userDto);
    }
}

