package com.example.solutionchallenge.controller;

import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = ["유저 정보 조회 및 수정"])
@RestController
@RequestMapping("/api/v1/user")
class UserInfoController(
        private val userInfoService: UserInfoService
) : RestSupport() {

    @GetMapping
    @ApiImplicitParams(
            ApiImplicitParam(
                    name = "Authorization",
                    value = "",
                    required = true,
                    allowEmptyValue = false,
                    paramType = "header",
                    dataTypeClass = String::class,
            example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdHkiOlt7ImF1dGhvcml0eSI6IkxFVkVMMCJ9XSwic3ViIjoia2FrYW8xNTE0NjYxOTk4IiwiYXVkIjoibW9iaWxlIiwiaWF0IjoxNjAzODY4OTg3LCJleHAiOjE2MDUzNDg5ODd9.wf8la-S_BP011E6ufCAC7eOp3nJghZ5RbuZ57GmN9vD3bkdxH2aCRSoff6FTHYZs6L9urRdXS64Z2R4kWppKhA"
    )
    )
    @PreAuthorize("hasRole('ADMIN')")
    fun getUserInfo(user: AuthenticatedUser): ResponseEntity<Any> {
        userInfoService.getUserInfo(user);
        return response("ok");
    }
}
