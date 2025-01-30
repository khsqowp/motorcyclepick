package com.example.motorcyclepick.service;

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.domain.MotorcycleDomain;
import com.example.motorcyclepick.dto.BoardDTO;
import com.example.motorcyclepick.exception.AuthorizationException;
import com.example.motorcyclepick.exception.DataAccessException;
import com.example.motorcyclepick.exception.DataIntegrityException;
import com.example.motorcyclepick.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardAnalService {
    private final BoardMapper boardMapper;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private SecurityLogger securityLogger;

    public void saveRecommendationStats(BoardDTO dto, MotorcycleDomain recommendedBike) {
        try {
            // 보안 검증 추가
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                throw new AccessDeniedException("인증되지 않은 접근입니다.");
            }

            securityLogger.logSecurityEvent(
                    "RECOMMENDATION_STATS_SAVE",
                    auth.getName(),
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr()
            );

            // 데이터 검증
            if (dto == null || (recommendedBike == null && dto.getQuestion1() == null)) {
                throw new IllegalArgumentException("유효하지 않은 데이터입니다.");
            }

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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.error("보안 위반 시도 감지: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "SECURITY_VIOLATION",
                    auth != null ? auth.getName() : "anonymous",
                    request.getRemoteAddr()
            );

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("추천 통계 저장 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("추천 통계 데이터 저장 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("추천 통계 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void saveRecommendationStatsData(BoardDTO dto, String surveyDate) {
        try {
            // 입력값 검증 및 살균
            if (dto.getQuestion1() != null) {
                dto.setQuestion1(securityService.sanitizeInput(dto.getQuestion1()));
            }
            dto.setQuestion2(securityService.sanitizeInput(dto.getQuestion2()));
            dto.setQuestion3(securityService.sanitizeInput(dto.getQuestion3()));
            dto.setQuestion4(securityService.sanitizeInput(dto.getQuestion4()));
            dto.setQuestion5(securityService.sanitizeInput(dto.getQuestion5()));
            dto.setQuestion6(securityService.sanitizeInput(dto.getQuestion6()));
            if (dto.getQuestion7() != null) {
                dto.setQuestion7(securityService.sanitizeInput(dto.getQuestion7()));
            }

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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.error("보안 위반 시도 감지: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "SECURITY_VIOLATION",
                    auth != null ? auth.getName() : "anonymous",
                    request.getRemoteAddr()
            );

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("설문 응답 저장 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("설문 응답 데이터 저장 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("설문 응답 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void saveModelRecommendationData(MotorcycleDomain motorcycle, String surveyDate) {
        try {
            // 데이터 검증
            if (motorcycle == null || motorcycle.getBrand() == null || motorcycle.getModel() == null) {
                throw new IllegalArgumentException("유효하지 않은 모터사이클 데이터입니다.");
            }

            // 입력값 살균
            String brand = securityService.sanitizeInput(motorcycle.getBrand());
            String model = securityService.sanitizeInput(motorcycle.getModel());

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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            log.error("보안 위반 시도 감지: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "SECURITY_VIOLATION",
                    auth != null ? auth.getName() : "anonymous",
                    request.getRemoteAddr()
            );

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("모델 추천 저장 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("모델 추천 데이터 저장 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("모델 추천 처리 중 오류가 발생했습니다.", e);
        }
    }
}