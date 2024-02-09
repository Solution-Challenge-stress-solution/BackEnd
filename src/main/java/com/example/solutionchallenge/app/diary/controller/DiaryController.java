package com.example.solutionchallenge.app.diary.controller;

import com.example.solutionchallenge.app.diary.dto.response.DiaryResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseUtil;
import com.example.solutionchallenge.app.diary.dto.request.DiarySaveRequestDto;
import com.example.solutionchallenge.app.diary.service.DiaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "일기", description = "일기 API")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "API 정상 작동"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "일기 저장", description = "일기 생성 API, 오디오파일 확장자는 .flac")
    @PostMapping("/activities/{activityId}") //415 에러 발생 시 @RequestBody 지워서 ㄱㄱ
    public ResponseDto<Long> save(HttpServletRequest request,
                                  @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
                                  DiarySaveRequestDto requestDto,
                                  @Parameter(description = "추천 활동 인덱스") @PathVariable("activityId") Long activityId) {
        Long diaryId = diaryService.save(request.getHeader("Authorization"), audioFile, requestDto, activityId);
        if (diaryId == 0) {
            return ResponseUtil.FAILURE("일기 저장에 실패하였습니다.", 0L);
        }
        return ResponseUtil.SUCCESS("일기 저장에 성공하였습니다.", diaryId);
    }

    @Operation(summary = "일기 조회", description = "일기 조회 API")
    @GetMapping("/{diaryId}")
    public ResponseDto<DiaryResponseDto> findById(HttpServletRequest request,
                                                  @Parameter(description = "일기 인덱스") @PathVariable("diaryId") Long diaryId) {
        return ResponseUtil.SUCCESS("일기 조회에 성공하였습니다.", diaryService.findById(request.getHeader("Authorization"), diaryId));
    }

    @Operation(summary = "일기 삭제", description = "일기 삭제 API")
    @DeleteMapping("/{diaryId}")
    public ResponseDto<Long> deleteDiary(HttpServletRequest request,
                                         @Parameter(description = "일기 인덱스") @PathVariable("diaryId") Long diaryId) {
        diaryService.deleteDiary(request.getHeader("Authorization"), diaryId);
        return ResponseUtil.SUCCESS("일기 삭제에 성공하였습니다.", diaryId);
    }

}
