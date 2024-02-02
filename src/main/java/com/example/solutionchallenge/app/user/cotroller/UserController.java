package com.example.solutionchallenge.app.user.cotroller;

import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.auth.domain.constant.ErrorCode;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.service.UserService;
import com.example.solutionchallenge.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    UserService service;
    @GetMapping()
    public List<Users> Users(String size) {
        List<Users> users = service.getUsers(size);
        return users;
    }
    @GetMapping("count")
    public int count() {
        return service.getDbCount();
    }
}