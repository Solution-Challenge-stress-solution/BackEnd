package com.example.solutionchallenge.app.common.dto.response;

import com.example.solutionchallenge.app.auth.domain.constant.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String errMsg;
    private String message;
    private int code;
    private int status;

    public ErrorResponse(String errMsg) {
        this.errMsg = errMsg;
    }

    @Builder
    public ErrorResponse(String message, int code, int status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .build();
    }

    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
