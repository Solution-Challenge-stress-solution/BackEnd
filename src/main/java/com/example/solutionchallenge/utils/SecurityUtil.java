package com.example.solutionchallenge.utils;

import com.example.solutionchallenge.common.config.exception.ApiException;
import com.example.solutionchallenge.common.config.exception.ErrorCode;
import com.sun.security.auth.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static long getCurrentUserId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        long userId;
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getId();
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        return userId;
    }
}

