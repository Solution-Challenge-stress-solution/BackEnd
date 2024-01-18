package com.example.solutionchallenge.service;

import com.example.solutionchallenge.domain.diary.Diary;
import com.example.solutionchallenge.domain.users.Users;
import com.example.solutionchallenge.dto.diary.DiarySaveRequestDto;
import com.example.solutionchallenge.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public Long save(DiarySaveRequestDto requestDto) {
        /** jwt 토큰 완성되면 작업
        Users users =
        Diary diary = requestDto.toEntity()
        **/
        return 1L;
    }
}
