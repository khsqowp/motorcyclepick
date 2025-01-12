package com.example.motorcycle.service;

import com.example.motorcycle.domain.MotorcycleDomain;
import com.example.motorcycle.dto.BoardDTO;
import com.example.motorcycle.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardAnalService {

    private final BoardMapper boardMapper;

    public void saveRecommendationStats(BoardDTO dto, MotorcycleDomain recommendedBike) {
        try {
            // 현재 날짜 설정
            String surveyDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

            // 설문 응답 데이터 저장 로직
            saveRecommendationStatsData(dto, surveyDate);

            // 모델 추천 데이터 저장 로직
            if (recommendedBike != null) {
                saveModelRecommendationData(recommendedBike, surveyDate);
            }

        } catch (Exception e) {
            log.error("추천 통계 저장 중 오류 발생: ", e);
            throw new RuntimeException("추천 통계 저장 실패", e);
        }
    }

    private void saveRecommendationStatsData(BoardDTO dto, String surveyDate) {
        try {
            // 설문 응답 데이터 준비
            Integer q1Value = dto.getQuestion1() != null ? Integer.parseInt(dto.getQuestion1()) : null;
            String q2Value = dto.getQuestion2();
            String q3Value = dto.getQuestion3();
            String q4Value = dto.getQuestion4();
            String q5Value = dto.getQuestion5();
            String q6Value = dto.getQuestion6();

            // Question7 split (콤마로 구분된 두 개의 값)
            String[] q7Values = dto.getQuestion7() != null ? dto.getQuestion7().split(",") : new String[2];
            String q7Value1 = q7Values.length > 0 ? q7Values[0] : null;
            String q7Value2 = q7Values.length > 1 ? q7Values[1] : null;

            // 중복 체크
            Integer duplicateId = boardMapper.checkDuplicateRecommendation(
                    q1Value, q2Value, q3Value, q4Value, q5Value, q6Value, q7Value1, q7Value2
            );

            if (duplicateId != null) {
                // 중복된 응답이 있으면 count 증가
                boardMapper.updateRecommendationStatsCount(
                        q1Value, q2Value, q3Value, q4Value, q5Value, q6Value, q7Value1, q7Value2
                );
            } else {
                // 새로운 응답 저장
                boardMapper.insertRecommendationStats(
                        surveyDate, q1Value, q2Value, q3Value, q4Value, q5Value, q6Value, q7Value1, q7Value2, 1
                );
            }

        } catch (Exception e) {
            log.error("설문 응답 저장 중 오류 발생: ", e);
            throw new RuntimeException("설문 응답 저장 실패", e);
        }
    }

    private void saveModelRecommendationData(MotorcycleDomain motorcycle, String surveyDate) {
        try {
            // 모델 추천 데이터 준비
            String brand = motorcycle.getBrand();
            String model = motorcycle.getModel();

            // 중복 체크
            Integer duplicateId = boardMapper.checkDuplicateModelRecommendation(surveyDate, brand, model);

            if (duplicateId != null) {
                // 중복된 추천이 있으면 count 증가
                boardMapper.updateModelRecommendationCount(surveyDate, brand, model);
            } else {
                // 새로운 추천 저장
                boardMapper.insertModelRecommendationStats(surveyDate, brand, model, 1);
            }

        } catch (Exception e) {
            log.error("모델 추천 저장 중 오류 발생: ", e);
            throw new RuntimeException("모델 추천 저장 실패", e);
        }
    }
}