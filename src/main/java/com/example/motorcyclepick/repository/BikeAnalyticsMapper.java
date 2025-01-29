package com.example.motorcyclepick.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface BikeAnalyticsMapper {
    // 기간별 응답 통계
    List<Map<String, Object>> findDailyResponseStats(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 투자 가능 비용 분포
    List<Map<String, Object>> getBudgetDistribution();

    // 배기량 선호도 분석
    List<Map<String, Object>> getEngineCapacityPreference();

    // 주행 스타일 선호도
    List<Map<String, Object>> getRidingStylePreference();

    // 속도 범위 선호도
    List<Map<String, Object>> getSpeedRangePreference();

    // 주행 거리 선호도
    List<Map<String, Object>> getRidingDistancePreference();

    // RPM 선호도
    List<Map<String, Object>> getRpmPreference();

    // 스타일 선호도 (q7) 분석
    List<Map<String, Object>> getStylePreference();

    // 가장 많이 추천된 모델 순위
    List<Map<String, Object>> getTopRecommendedModels(@Param("limit") int limit);

    // 브랜드별 추천 빈도
    List<Map<String, Object>> getBrandRecommendationFrequency();

    // 기간별 브랜드 추천 추이
    List<Map<String, Object>> getBrandTrendsByPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 가격대별 선호 모델
    List<Map<String, Object>> getPreferredModelsByBudget();

    // 주행목적별 선호 모델
    List<Map<String, Object>> getPreferredModelsByPurpose();

    // 스타일 조합 분석
    List<Map<String, Object>> getStyleCombinationAnalysis();

    // 월별 추천 트렌드
    List<Map<String, Object>> getMonthlyRecommendationTrends();

    // 계절별 선호도 분석
    List<Map<String, Object>> getSeasonalPreferences();
}