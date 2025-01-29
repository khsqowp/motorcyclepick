package com.example.motorcyclepick.dto;

import com.example.motorcyclepick.domain.BikeAnalyticsDomain;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BikeAnalyticsDTO {
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

    public static BikeAnalyticsDTO fromDomain(BikeAnalyticsDomain domain) {
        BikeAnalyticsDTO dto = new BikeAnalyticsDTO();
        dto.setRecommendationStatsId(domain.getRecommendationStatsId());
        dto.setRecommendationStatsCreateDate(domain.getRecommendationStatsCreateDate());
        dto.setSurveyDate(domain.getSurveyDate());
        dto.setQ1Value(domain.getQ1Value());
        dto.setQ2Value(domain.getQ2Value());
        dto.setQ3Value(domain.getQ3Value());
        dto.setQ4Value(domain.getQ4Value());
        dto.setQ5Value(domain.getQ5Value());
        dto.setQ6Value(domain.getQ6Value());
        dto.setQ7Value1(domain.getQ7Value1());
        dto.setQ7Value2(domain.getQ7Value2());
        dto.setCount(domain.getCount());
        dto.setModelRecommendationStatsId(domain.getModelRecommendationStatsId());
        dto.setModelRecommendationStatsCreateDate(domain.getModelRecommendationStatsCreateDate());
        dto.setBrand(domain.getBrand());
        dto.setModel(domain.getModel());
        dto.setRecommendationCount(domain.getRecommendationCount());
        dto.setBudgetRange(domain.getBudgetRange());
        dto.setTotalRecommendations(domain.getTotalRecommendations());
        dto.setRidingPurpose(domain.getRidingPurpose());
        dto.setRidingStyle(domain.getRidingStyle());
        dto.setSpeedRange(domain.getSpeedRange());
        dto.setRidingDistance(domain.getRidingDistance());
        dto.setRpmRange(domain.getRpmRange());
        dto.setSeason(domain.getSeason());
        dto.setMonthlyCount(domain.getMonthlyCount());
        dto.setDailyCount(domain.getDailyCount());
        dto.setPreferenceCount(domain.getPreferenceCount());
        dto.setCombinationCount(domain.getCombinationCount());
        return dto;
    }

    public BikeAnalyticsDomain toDomain() {
        BikeAnalyticsDomain domain = new BikeAnalyticsDomain();
        domain.setRecommendationStatsId(this.recommendationStatsId);
        domain.setRecommendationStatsCreateDate(this.recommendationStatsCreateDate);
        domain.setSurveyDate(this.surveyDate);
        domain.setQ1Value(this.q1Value);
        domain.setQ2Value(this.q2Value);
        domain.setQ3Value(this.q3Value);
        domain.setQ4Value(this.q4Value);
        domain.setQ5Value(this.q5Value);
        domain.setQ6Value(this.q6Value);
        domain.setQ7Value1(this.q7Value1);
        domain.setQ7Value2(this.q7Value2);
        domain.setCount(this.count);
        domain.setModelRecommendationStatsId(this.modelRecommendationStatsId);
        domain.setModelRecommendationStatsCreateDate(this.modelRecommendationStatsCreateDate);
        domain.setBrand(this.brand);
        domain.setModel(this.model);
        domain.setRecommendationCount(this.recommendationCount);
        domain.setBudgetRange(this.budgetRange);
        domain.setTotalRecommendations(this.totalRecommendations);
        domain.setRidingPurpose(this.ridingPurpose);
        domain.setRidingStyle(this.ridingStyle);
        domain.setSpeedRange(this.speedRange);
        domain.setRidingDistance(this.ridingDistance);
        domain.setRpmRange(this.rpmRange);
        domain.setSeason(this.season);
        domain.setMonthlyCount(this.monthlyCount);
        domain.setDailyCount(this.dailyCount);
        domain.setPreferenceCount(this.preferenceCount);
        domain.setCombinationCount(this.combinationCount);
        return domain;
    }
}