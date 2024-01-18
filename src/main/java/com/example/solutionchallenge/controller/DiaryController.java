package com.example.solutionchallenge.controller;

import com.example.solutionchallenge.common.config.response.ResponseDto;
import com.example.solutionchallenge.common.config.response.ResponseUtil;
import com.example.solutionchallenge.dto.diary.DiarySaveRequestDto;
import com.example.solutionchallenge.service.DiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "일기", description = "일기 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("")
    public ResponseDto<Long> save(HttpServletRequest request, @RequestBody DiarySaveRequestDto requestDto) {
        return ResponseUtil.SUCCESS("일기 저장에 성공하였습니다.", diaryService.save(requestDto));
    }

}
