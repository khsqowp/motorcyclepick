package com.example.motorcyclepick.domain;

import com.example.motorcyclepick.dto.BikeAnalyticsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BikeAnalyticsDomain {
    // RecommendationStats 관련 필드
    private Long recommendationStatsId;
    private LocalDateTime recommendationStatsCreateDate;
    private LocalDate surveyDate;
    private Integer q1Value;
    private String q2Value;
    private String q3Value;
    private String q4Value;
    private String q5Value;
    private String q6Value;
    private String q7Value1;
    private String q7Value2;
    private Integer count;

    // ModelRecommendationStats 관련 필드
    private Long modelRecommendationStatsId;
    private LocalDateTime modelRecommendationStatsCreateDate;
    private String brand;
    private String model;
    private Integer recommendationCount;

    // 분석 결과 관련 필드
    private String budgetRange;
    private Integer totalRecommendations;
    private String ridingPurpose;
    private String ridingStyle;
    private String speedRange;
    private String ridingDistance;
    private String rpmRange;
    private String season;
    private Integer monthlyCount;
    private Integer dailyCount;
    private Integer preferenceCount;
    private Integer combinationCount;

    public static BikeAnalyticsDomain fromDTO(BikeAnalyticsDTO dto) {
        BikeAnalyticsDomain domain = new BikeAnalyticsDomain();
        domain.setRecommendationStatsId(dto.getRecommendationStatsId());
        domain.setRecommendationStatsCreateDate(dto.getRecommendationStatsCreateDate());
        domain.setSurveyDate(dto.getSurveyDate());
        domain.setQ1Value(dto.getQ1Value());
        domain.setQ2Value(dto.getQ2Value());
        domain.setQ3Value(dto.getQ3Value());
        domain.setQ4Value(dto.getQ4Value());
        domain.setQ5Value(dto.getQ5Value());
        domain.setQ6Value(dto.getQ6Value());
        domain.setQ7Value1(dto.getQ7Value1());
        domain.setQ7Value2(dto.getQ7Value2());
        domain.setCount(dto.getCount());
        domain.setModelRecommendationStatsId(dto.getModelRecommendationStatsId());
        domain.setModelRecommendationStatsCreateDate(dto.getModelRecommendationStatsCreateDate());
        domain.setBrand(dto.getBrand());
        domain.setModel(dto.getModel());
        domain.setRecommendationCount(dto.getRecommendationCount());
        domain.setBudgetRange(dto.getBudgetRange());
        domain.setTotalRecommendations(dto.getTotalRecommendations());
        domain.setRidingPurpose(dto.getRidingPurpose());
        domain.setRidingStyle(dto.getRidingStyle());
        domain.setSpeedRange(dto.getSpeedRange());
        domain.setRidingDistance(dto.getRidingDistance());
        domain.setRpmRange(dto.getRpmRange());
        domain.setSeason(dto.getSeason());
        domain.setMonthlyCount(dto.getMonthlyCount());
        domain.setDailyCount(dto.getDailyCount());
        domain.setPreferenceCount(dto.getPreferenceCount());
        domain.setCombinationCount(dto.getCombinationCount());
        return domain;
    }
}