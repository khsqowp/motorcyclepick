package com.example.motorcyclepick.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Mapper
public interface BoardMapper {
    // 설문 응답 통계 저장
    void insertRecommendationStats(
            @Param("surveyDate") String surveyDate,
            @Param("q1Value") Integer q1Value,
            @Param("q2Value") String q2Value,
            @Param("q3Value") String q3Value,
            @Param("q4Value") String q4Value,
            @Param("q5Value") String q5Value,
            @Param("q6Value") String q6Value,
            @Param("q7Value1") String q7Value1,
            @Param("q7Value2") String q7Value2,
            @Param("count") Integer count
    );

    // 모델 추천 통계 저장
    void insertModelRecommendationStats(
            @Param("surveyDate") String surveyDate,
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("recommendationCount") Integer recommendationCount
    );

    // 설문 응답 중복 체크 (동일한 설문 응답이 있는지 확인)
    Integer checkDuplicateRecommendation(
            @Param("q1Value") Integer q1Value,
            @Param("q2Value") String q2Value,
            @Param("q3Value") String q3Value,
            @Param("q4Value") String q4Value,
            @Param("q5Value") String q5Value,
            @Param("q6Value") String q6Value,
            @Param("q7Value1") String q7Value1,
            @Param("q7Value2") String q7Value2
    );

    // 기존 설문 응답의 count 업데이트
    void updateRecommendationStatsCount(
            @Param("q1Value") Integer q1Value,
            @Param("q2Value") String q2Value,
            @Param("q3Value") String q3Value,
            @Param("q4Value") String q4Value,
            @Param("q5Value") String q5Value,
            @Param("q6Value") String q6Value,
            @Param("q7Value1") String q7Value1,
            @Param("q7Value2") String q7Value2
    );

    // 특정 모델의 추천 횟수 업데이트
    void updateModelRecommendationCount(
            @Param("surveyDate") String surveyDate,
            @Param("brand") String brand,
            @Param("model") String model
    );

    // 모델 추천 중복 체크
    Integer checkDuplicateModelRecommendation(
            @Param("surveyDate") String surveyDate,
            @Param("brand") String brand,
            @Param("model") String model
    );
}