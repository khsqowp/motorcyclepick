package com.example.motorcycle.controller;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.dto.BikeAnalyticsDTO;
import com.example.motorcycle.service.BikeAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BikeAnalyticsController {

    private final BikeAnalyticsService bikeAnalyticsService;
    private final SecurityLogger securityLogger;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics")
    public String showAnalytics(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String remoteAddr = request.getRemoteAddr();
        String username = auth.getName();

        try {
            securityLogger.logSecurityEvent(
                    "ANALYTICS_PAGE_ACCESS",
                    username,
                    remoteAddr
            );

            // 기본 날짜 설정 (최근 30일)
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(30);

            // 모든 분석 데이터 로드
            loadAnalyticsData(model, startDate, endDate);

            return "bikeAnalytics";

        } catch (Exception e) {
            log.error("분석 페이지 로딩 중 오류 발생", e);
            securityLogger.logSecurityEvent(
                    "ANALYTICS_PAGE_ACCESS_FAILURE",
                    username,
                    remoteAddr
            );
            model.addAttribute("error", "분석 페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }

    // REST API Endpoints
    @GetMapping("/analytics/daily-stats")
    @ResponseBody
    public ResponseEntity<?> getDailyStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.findDailyResponseStats(startDate, endDate)));
    }

    @GetMapping("/analytics/budget-distribution")
    @ResponseBody
    public ResponseEntity<?> getBudgetDistribution() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getBudgetDistribution()));
    }

    @GetMapping("/analytics/engine-capacity")
    @ResponseBody
    public ResponseEntity<?> getEngineCapacity() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getEngineCapacityPreference()));
    }

    @GetMapping("/analytics/riding-style")
    @ResponseBody
    public ResponseEntity<?> getRidingStyle() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getRidingStylePreference()));
    }

    @GetMapping("/analytics/speed-range")
    @ResponseBody
    public ResponseEntity<?> getSpeedRange() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getSpeedRangePreference()));
    }

    @GetMapping("/analytics/riding-distance")
    @ResponseBody
    public ResponseEntity<?> getRidingDistance() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getRidingDistancePreference()));
    }

    @GetMapping("/analytics/rpm-preference")
    @ResponseBody
    public ResponseEntity<?> getRpmPreference() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getRpmPreference()));
    }

    @GetMapping("/analytics/style-preference")
    @ResponseBody
    public ResponseEntity<?> getStylePreference() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getStylePreference()));
    }

    @GetMapping("/analytics/top-models")
    @ResponseBody
    public ResponseEntity<?> getTopModels() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getTopRecommendedModels(10)));
    }

    @GetMapping("/analytics/brand-frequency")
    @ResponseBody
    public ResponseEntity<?> getBrandFrequency() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getBrandRecommendationFrequency()));
    }

    @GetMapping("/analytics/brand-trends")
    @ResponseBody
    public ResponseEntity<?> getBrandTrends(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getBrandTrendsByPeriod(startDate, endDate)));
    }

    private void loadAnalyticsData(Model model, LocalDate startDate, LocalDate endDate) {
        // 일별 응답 통계
        List<BikeAnalyticsDTO> dailyStats = bikeAnalyticsService.findDailyResponseStats(startDate, endDate);
        model.addAttribute("dailyStats", dailyStats);

        // 예산 분포
        List<BikeAnalyticsDTO> budgetStats = bikeAnalyticsService.getBudgetDistribution();
        model.addAttribute("budgetStats", budgetStats);

        // 엔진 배기량 선호도
        List<BikeAnalyticsDTO> engineStats = bikeAnalyticsService.getEngineCapacityPreference();
        model.addAttribute("engineStats", engineStats);

        // 주행 스타일 선호도
        List<BikeAnalyticsDTO> ridingStats = bikeAnalyticsService.getRidingStylePreference();
        model.addAttribute("ridingStats", ridingStats);

        // 속도 범위 선호도
        List<BikeAnalyticsDTO> speedStats = bikeAnalyticsService.getSpeedRangePreference();
        model.addAttribute("speedStats", speedStats);

        // 주행 거리 선호도
        List<BikeAnalyticsDTO> distanceStats = bikeAnalyticsService.getRidingDistancePreference();
        model.addAttribute("distanceStats", distanceStats);

        // RPM 선호도
        List<BikeAnalyticsDTO> rpmStats = bikeAnalyticsService.getRpmPreference();
        model.addAttribute("rpmStats", rpmStats);

        // 스타일 선호도
        List<BikeAnalyticsDTO> styleStats = bikeAnalyticsService.getStylePreference();
        model.addAttribute("styleStats", styleStats);

        // 인기 모델 (상위 10개)
        List<BikeAnalyticsDTO> topModels = bikeAnalyticsService.getTopRecommendedModels(10);
        model.addAttribute("topModels", topModels);

        // 브랜드별 추천 빈도
        List<BikeAnalyticsDTO> brandStats = bikeAnalyticsService.getBrandRecommendationFrequency();
        model.addAttribute("brandStats", brandStats);

        // 브랜드 트렌드
        List<BikeAnalyticsDTO> brandTrends = bikeAnalyticsService.getBrandTrendsByPeriod(startDate, endDate);
        model.addAttribute("brandTrends", brandTrends);

        // 예산별 선호 모델
        List<BikeAnalyticsDTO> budgetModels = bikeAnalyticsService.getPreferredModelsByBudget();
        model.addAttribute("budgetModels", budgetModels);

        // 용도별 선호 모델
        List<BikeAnalyticsDTO> purposeModels = bikeAnalyticsService.getPreferredModelsByPurpose();
        model.addAttribute("purposeModels", purposeModels);

        // 스타일 조합 분석
        List<BikeAnalyticsDTO> styleCombinations = bikeAnalyticsService.getStyleCombinationAnalysis();
        model.addAttribute("styleCombinations", styleCombinations);

        // 월별 추천 트렌드
        List<BikeAnalyticsDTO> monthlyTrends = bikeAnalyticsService.getMonthlyRecommendationTrends();
        model.addAttribute("monthlyTrends", monthlyTrends);

        // 계절별 선호도
        List<BikeAnalyticsDTO> seasonalPrefs = bikeAnalyticsService.getSeasonalPreferences();
        model.addAttribute("seasonalPrefs", seasonalPrefs);

        // 날짜 범위 데이터
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
    }
}