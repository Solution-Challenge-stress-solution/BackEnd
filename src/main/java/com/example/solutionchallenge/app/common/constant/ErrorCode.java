package com.example.solutionchallenge.app.common.constant;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNKNOWN_ERROR(500, "토큰이 존재하지 않습니다.", 1001),
    WRONG_TYPE_TOKEN(500, "변조된 토큰입니다.", 1002),
    EXPIRED_TOKEN(500, "만료된 토큰입니다.", 1003),
    UNSUPPORTED_TOKEN(500, "변조된 토큰입니다.", 1004),
    ACCESS_DENIED(500, "권한이 없습니다.", 1005),
    NO_INFO(500, "토큰에 해당하는 정보가 없습니다.", 1006),
    UNAUTHORIZED("인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_USER("존재하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED);

    private final int status;
    private final String message;
    private final HttpStatus httpStatus;
    private final int code;

    ErrorCode(int status, String message, int code) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.httpStatus = null;
    }

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.code = -1;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }
}

