package com.example.solutionchallenge.app.handler;

import com.example.solutionchallenge.app.common.dto.response.ErrorResponse;
import com.example.solutionchallenge.app.common.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(ApiException ex) {
        logger.error("[CustomException] errCode : " + ex.getErrorCode());
        logger.error("[CustomException] errMsg : " + ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(ex.getMessage()),
                ex.getErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        logger.error("[RuntimeException] errMsg : " + ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException ex) {
        logger.error("[Exception] errMsg : " + ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}