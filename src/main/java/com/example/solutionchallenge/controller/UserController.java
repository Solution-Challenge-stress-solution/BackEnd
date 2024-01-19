package com.example.solutionchallenge.controller;

import com.example.solutionchallenge.common.config.exception.ApiException;
import com.example.solutionchallenge.common.config.exception.ErrorCode;
import com.example.solutionchallenge.dto.user.UserDto;
import com.example.solutionchallenge.service.UserService;
import com.example.solutionchallenge.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // 유저정보 조회 API
    @GetMapping("/info")
    public UserDto info() {
        final String userId = SecurityUtil.getCurrentUserId();
        UserDto userDto = userService.findById(Long.valueOf(userId));
        if(userDto == null) {
            throw new ApiException(ErrorCode.NOT_EXIST_USER);
        }
        return userDto;
    }
}