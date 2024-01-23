package com.example.solutionchallenge.app.common.exception;

import com.example.solutionchallenge.app.common.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }

}
