package com.example.solutionchallenge.utils;

import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.common.constant.ErrorCode;
import com.sun.security.auth.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static String getCurrentUserId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        String userId;
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getName();
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        return userId;
    }
}

