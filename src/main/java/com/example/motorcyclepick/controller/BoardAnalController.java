package com.example.motorcyclepick.controller;

import com.example.motorcyclepick.domain.MotorcycleDomain;
import com.example.motorcyclepick.dto.BoardDTO;
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

@Slf4j
@Controller
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class BoardAnalController {

    private final BoardAnalService boardAnalService;

    @PostMapping("/save-recommendation")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> saveRecommendationData(
            @RequestBody BoardDTO boardDTO,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 세션에서 추천된 오토바이 목록 가져오기
            @SuppressWarnings("unchecked")
            List<MotorcycleDomain> results = (List<MotorcycleDomain>) session.getAttribute("results");

            if (results == null || results.isEmpty()) {
                response.put("success", false);
                response.put("message", "추천 결과를 찾을 수 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 첫 번째 추천 모델 저장
            MotorcycleDomain recommendedBike = results.get(0);

            // 설문 응답과 추천 모델 저장
            boardAnalService.saveRecommendationStats(boardDTO, recommendedBike);

            response.put("success", true);
            response.put("message", "설문 응답과 추천 데이터가 성공적으로 저장되었습니다.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("추천 데이터 저장 중 오류 발생: ", e);

            response.put("success", false);
            response.put("message", "데이터 저장 중 오류가 발생했습니다: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 예외 발생 시 사용할 응답 생성 메소드
    private ResponseEntity<?> createErrorResponse(String message, Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("error", e.getMessage());

        log.error(message, e);

        return ResponseEntity.internalServerError().body(response);
    }
}