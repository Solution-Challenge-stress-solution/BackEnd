package com.example.solutionchallenge.app.diary.controller;

import com.example.solutionchallenge.app.common.dto.response.ResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseUtil;
import com.example.solutionchallenge.app.diary.dto.request.DiarySaveRequestDto;
import com.example.solutionchallenge.app.diary.dto.response.DiaryResponseDto;
import com.example.solutionchallenge.app.diary.service.DiaryService;
import com.example.solutionchallenge.utils.SecurityUtil;
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

@Tag(name = "일기", description = "일기 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

//    @PostMapping("") //415 에러 발생 시 @RequestBody 지워서 ㄱㄱ
//    public ResponseDto<Long> save(HttpServletRequest request,
//                                  @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
//                                  DiarySaveRequestDto requestDto) {
//        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
//        Long diaryId = diaryService.save(audioFile, requestDto, userId);
//        if (diaryId == 0) {
//            return ResponseUtil.FAILURE("일기 저장에 실패하였습니다.", 0L);
//        }
//        return ResponseUtil.SUCCESS("일기 저장에 성공하였습니다.", diaryId);
//    }
//
//    @GetMapping("/{diaryId}")
//    public ResponseDto<DiaryResponseDto> findById(HttpServletRequest request, @PathVariable("diaryId") Long diaryId) {
//        return ResponseUtil.SUCCESS("일기 조회에 성공하였습니다.", diaryService.findById(diaryId));
//    }
//
//    @DeleteMapping("/{diaryId}")
//    public ResponseDto<Long> deleteDiary(HttpServletRequest request, @PathVariable("diaryId") Long diaryId) {
//        diaryService.deleteDiary(diaryId);
//        return ResponseUtil.SUCCESS("일기 삭제에 성공하였습니다.", diaryId);
//    }

}
