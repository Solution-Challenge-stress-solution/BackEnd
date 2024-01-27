package com.example.solutionchallenge.app.recommendedActivity.domain;

import com.example.solutionchallenge.app.diary.domain.Diary;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class RecommendedActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;
    private String title;
    private String content;

    @OneToMany(mappedBy = "recommendedActivity", cascade = CascadeType.ALL)
    private List<Diary> diaryList = new ArrayList<>();


}
