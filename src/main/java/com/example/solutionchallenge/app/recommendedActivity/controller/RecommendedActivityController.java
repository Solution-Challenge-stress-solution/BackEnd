package com.example.solutionchallenge.app.recommendedActivity.controller;

import com.example.solutionchallenge.app.common.dto.response.ResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseUtil;
import com.example.solutionchallenge.app.recommendedActivity.dto.ActivityResponseDto;
import com.example.solutionchallenge.app.recommendedActivity.service.RecommendedActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "추천 활동", description = "추천 활동 API")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "API 정상 작동"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/activities")
public class RecommendedActivityController {

    private final RecommendedActivityService recommendedActivityService;

    @Operation(summary = "추천 활동 조회", description = "랜덤으로 생성한 추천활동 번호로 조회")
    @GetMapping("")
    public ResponseDto<ActivityResponseDto> findById() {
        return ResponseUtil.SUCCESS("추천 활동 조회에 성공하였습니다.", recommendedActivityService.findRandom());
    }

}
