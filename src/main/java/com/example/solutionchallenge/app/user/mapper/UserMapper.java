package com.example.solutionchallenge.app.user.mapper;

import com.example.solutionchallenge.app.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void save(UserDto userDto);
    UserDto findById(Long id);
    UserDto findByEmail(String email);
    void update(UserDto userDto);

}