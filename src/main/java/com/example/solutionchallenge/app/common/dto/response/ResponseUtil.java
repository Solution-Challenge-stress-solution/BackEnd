package com.example.solutionchallenge.app.common.dto.response;

import com.example.solutionchallenge.app.common.dto.response.ResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseStatus;

public class ResponseUtil {

    public static <T> ResponseDto<T> SUCCESS(String message, T data) {
        return new ResponseDto(ResponseStatus.SUCCESS, message, data);
    }

    public static <T> ResponseDto<T> FAILURE(String message, T data) {
        return new ResponseDto(ResponseStatus.FAILURE, message, data);
    }

    public static <T> ResponseDto<T> ERROR(String message, T data) {
        return new ResponseDto(ResponseStatus.ERROR, message, data);
    }

}
