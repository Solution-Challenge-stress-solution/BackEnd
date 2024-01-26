package com.example.solutionchallenge.app.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    @Schema(description = "상태 코드")
    private final ResponseStatus status;
    @Schema(description = "상태 메세지")
    private final String message;
    @Schema(description = "데이터")
    private final T data;
}
