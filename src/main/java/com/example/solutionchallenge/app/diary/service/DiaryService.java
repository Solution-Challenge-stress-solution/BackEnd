package com.example.solutionchallenge.app.diary.service;

import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenProvider;
import com.example.solutionchallenge.app.diary.dto.response.DiaryResponseDto;
import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.diary.dto.request.DiarySaveRequestDto;
import com.example.solutionchallenge.app.diary.repository.DiaryRepository;

import com.example.solutionchallenge.app.recommendedActivity.dto.ActivityResponseDto;
import com.example.solutionchallenge.app.recommendedActivity.repository.RecommendedActivityRepository;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import com.example.solutionchallenge.app.analysis.domain.StressLevel;
import com.example.solutionchallenge.app.analysis.repository.StressLevelRepository;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.repository.UsersRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UsersRepository usersRepository;
    private final RecommendedActivityRepository recommendedActivityRepository;
    private final StressLevelRepository stressLevelRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    public Long save(String accessToken, MultipartFile audioFile, DiarySaveRequestDto requestDto) {
        Users user = getUserByToken(accessToken);
        try {
            ClassPathResource resource = new ClassPathResource("strecording-upload.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials)
                    .build().getService();

            String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
            String extension = audioFile.getContentType(); //파일 형식 flac

            BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, uuid).setContentType(extension).build(), audioFile.getBytes());
            String audioUrl = "https://storage.googleapis.com/" + bucketName + "/" + uuid;

            double dValue = Math.random();
            long randomId = (long) ((dValue * 20) + 1);
            RecommendedActivity recommendedActivity = recommendedActivityRepository.findById(randomId).orElseThrow(
                    () -> new IllegalArgumentException("해당 추천 활동이 없습니다. id=" + randomId));


            Long diaryId = diaryRepository.save(requestDto.toEntity(requestDto.getContent(), user, recommendedActivity, audioUrl)).getId();
            return diaryId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public DiaryResponseDto findByCreatedDate(String accessToken, String diaryDate) {
        Users user = getUserByToken(accessToken);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(diaryDate, formatter);

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = startDateTime.plusDays(1);

        Optional<Diary> diary = diaryRepository.findDiaryByUsersAndCreatedDateBetween(user, startDateTime, endDateTime);
        if (diary.isEmpty()) {
            throw new IllegalArgumentException("해당 일기가 없습니다. createdDate=" + date);
        }
        Optional<StressLevel> stressLevel = stressLevelRepository.findByDiary(diary.get());
        if (stressLevel.isEmpty()) {
            throw new IllegalArgumentException("해당 스트레스 지수가 없습니다. diaryId=" + diary.get().getId());
        }
        DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
                .diary(diary.get())
                .stressLevel(stressLevel.get())
                .activityId(diary.get().getRecommendedActivity().getId())
                .build();
        return diaryResponseDto;
    }

    public void deleteDiary(String accessToken, Long diaryId) {
        getUserByToken(accessToken);
        diaryRepository.deleteById(diaryId);
    }

    public Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }

}
