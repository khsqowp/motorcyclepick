// 게시판 분석 기능을 처리하는 컨트롤러 패키지
package com.example.motorcyclepick.controller;

// 필요한 의존성들을 임포트

import com.example.motorcyclepick.domain.MotorcycleDomain;
import com.example.motorcyclepick.dto.BoardDTO;
import com.example.motorcyclepick.exception.DataAccessException;
import com.example.motorcyclepick.exception.DataIntegrityException;
import com.example.motorcyclepick.exception.MotorcycleValidationException;
import com.example.motorcyclepick.service.BoardAnalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Lombok을 사용한 로깅 기능 활성화
@Slf4j
// 스프링 MVC 컨트롤러 선언
@Controller
// 기본 URL 경로를 /analysis로 지정
@RequestMapping("/analysis")
// Lombok을 사용하여 필수 생성자 자동 생성
@RequiredArgsConstructor
public class BoardAnalController {

    // 게시판 분석 서비스 의존성 주입
    private final BoardAnalService boardAnalService;

    // 추천 데이터 저장 API 엔드포인트 (모든 사용자 접근 가능)
    @PostMapping("/save-recommendation")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> saveRecommendationData(
            @RequestBody BoardDTO boardDTO,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            @SuppressWarnings("unchecked")
            List<MotorcycleDomain> results = (List<MotorcycleDomain>) session.getAttribute("results");

            if (results == null || results.isEmpty()) {
                response.put("success", false);
                response.put("message", "추천 결과를 찾을 수 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            MotorcycleDomain recommendedBike = results.get(0);
            boardAnalService.saveRecommendationStats(boardDTO, recommendedBike);

            response.put("success", true);
            response.put("message", "설문 응답과 추천 데이터가 성공적으로 저장되었습니다.");
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            log.error("추천 데이터 저장 중 데이터베이스 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "데이터베이스 저장 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        } catch (DataIntegrityException e) {
            log.error("추천 데이터 무결성 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "데이터 무결성 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        } catch (MotorcycleValidationException e) {
            log.error("오토바이 데이터 유효성 검증 오류: ", e);
            response.put("success", false);
            response.put("message", "오토바이 데이터 검증 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 에러 응답을 생성하는 유틸리티 메서드
    private ResponseEntity<?> createErrorResponse(String message, Exception e) {
        // 에러 응답 데이터를 저장할 Map 생성
        Map<String, Object> response = new HashMap<>();
        // 실패 상태 설정
        response.put("success", false);
        // 에러 메시지 설정
        response.put("message", message);
        // 상세 에러 내용 설정
        response.put("error", e.getMessage());

        // 에러 로깅
        log.error(message, e);

        // 서버 오류 응답 반환
        return ResponseEntity.internalServerError().body(response);
    }
}