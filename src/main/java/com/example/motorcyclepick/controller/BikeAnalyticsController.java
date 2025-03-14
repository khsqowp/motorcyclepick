// 오토바이 분석 기능을 처리하는 컨트롤러 패키지
package com.example.motorcyclepick.controller;

// 필요한 의존성들을 임포트

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.dto.BikeAnalyticsDTO;
import com.example.motorcyclepick.exception.AuthorizationException;
import com.example.motorcyclepick.exception.DataAccessException;
import com.example.motorcyclepick.service.BikeAnalyticsService;
import com.example.motorcyclepick.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Lombok을 사용한 로깅 기능 활성화
@Slf4j
// 스프링 MVC 컨트롤러 선언
@Controller
// Lombok을 사용하여 필수 생성자 자동 생성
@RequiredArgsConstructor
public class BikeAnalyticsController {
    // 오토바이 분석 서비스 의존성 주입
    private final BikeAnalyticsService bikeAnalyticsService;
    // 보안 로깅 서비스 의존성 주입
    private final SecurityLogger securityLogger;

    // 보안 서비스 의존성 주입
    @Autowired
    private SecurityService securityService;

    // 분석 페이지 표시 (관리자와 모더레이터만 접근 가능)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics")
    public String showAnalytics(Model model, HttpServletRequest request) {
        // 현재 인증 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String remoteAddr = request.getRemoteAddr();
        String username = auth.getName();

        try {
            securityLogger.logSecurityEvent(
                    "ANALYTICS_PAGE_ACCESS",
                    username,
                    remoteAddr
            );

            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(30);

            loadAnalyticsData(model, startDate, endDate);

            return "bikeAnalytics";

        } catch (AuthorizationException e) {
            log.error("분석 페이지 접근 권한 오류", e);
            model.addAttribute("error", "분석 페이지에 대한 접근 권한이 없습니다.");
            return "error";
        } catch (DataAccessException e) {
            log.error("분석 데이터 접근 중 오류 발생", e);
            securityLogger.logSecurityEvent(
                    "ANALYTICS_PAGE_ACCESS_FAILURE",
                    username,
                    remoteAddr
            );
            model.addAttribute("error", "분석 데이터를 불러오는 중 오류가 발생했습니다.");
            return "error";
        }
    }

    // 일별 통계 데이터 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/daily-stats")
    @ResponseBody
    public ResponseEntity<?> getDailyStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 날짜 범위 유효성 검사
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().body(Map.of("error", "시작 날짜는 종료 날짜보다 이후일 수 없습니다."));
        }

        // 데이터 접근 로깅
        securityLogger.logDataAccessEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "ANALYTICS_DATA_ACCESS",
                "daily-stats"
        );

        // 일별 통계 데이터 반환
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.findDailyResponseStats(startDate, endDate)));
    }

    // 예산 분포 데이터 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/budget-distribution")
    @ResponseBody
    public ResponseEntity<?> getBudgetDistribution() {
        try {
            securityLogger.logDataAccessEvent(
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    "ANALYTICS_DATA_ACCESS",
                    "budget-distribution"
            );
            return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getBudgetDistribution()));
        } catch (AuthorizationException e) {
            log.error("예산 분포 데이터 접근 권한 오류", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "데이터 접근 권한이 없습니다."));
        } catch (DataAccessException e) {
            log.error("예산 분포 데이터 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "데이터베이스 접근 중 오류가 발생했습니다."));
        }
    }

    // 엔진 배기량 선호도 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/engine-capacity")
    @ResponseBody
    public ResponseEntity<?> getEngineCapacity() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getEngineCapacityPreference()));
    }

    // 라이딩 스타일 선호도 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/riding-style")
    @ResponseBody
    public ResponseEntity<?> getRidingStyle() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getRidingStylePreference()));
    }

    // 속도 범위 선호도 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/speed-range")
    @ResponseBody
    public ResponseEntity<?> getSpeedRange() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getSpeedRangePreference()));
    }

    // 주행 거리 선호도 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/riding-distance")
    @ResponseBody
    public ResponseEntity<?> getRidingDistance() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getRidingDistancePreference()));
    }

    // RPM 선호도 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/rpm-preference")
    @ResponseBody
    public ResponseEntity<?> getRpmPreference() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getRpmPreference()));
    }

    // 스타일 선호도 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/style-preference")
    @ResponseBody
    public ResponseEntity<?> getStylePreference() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getStylePreference()));
    }

    // 인기 모델 상위 10개 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/top-models")
    @ResponseBody
    public ResponseEntity<?> getTopModels() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getTopRecommendedModels(10)));
    }

    // 브랜드별 추천 빈도 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/brand-frequency")
    @ResponseBody
    public ResponseEntity<?> getBrandFrequency() {
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getBrandRecommendationFrequency()));
    }

    // 브랜드별 트렌드 조회 API
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/analytics/brand-trends")
    @ResponseBody
    public ResponseEntity<?> getBrandTrends(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 날짜 파라미터 유효성 검사
        if (startDate == null || endDate == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "날짜 범위는 필수입니다."));
        }
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().body(Map.of("error", "시작 날짜는 종료 날짜보다 이후일 수 없습니다."));
        }

        // 데이터 접근 로깅
        securityLogger.logDataAccessEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "BRAND_TRENDS_ACCESS",
                "date_range:" + startDate + "to" + endDate
        );

        // 브랜드 트렌드 데이터 반환
        return ResponseEntity.ok(Map.of("data", bikeAnalyticsService.getBrandTrendsByPeriod(startDate, endDate)));
    }

    // 모든 분석 데이터를 로드하는 private 메서드
    private void loadAnalyticsData(Model model, LocalDate startDate, LocalDate endDate) {
        try {
            // 데이터 로드 시작 로깅
            securityLogger.logDataAccessEvent(
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    "ANALYTICS_LOAD",
                    "all_data"
            );

            // 일별 통계 데이터 로드
            try {
                List<BikeAnalyticsDTO> dailyStats = bikeAnalyticsService.findDailyResponseStats(startDate, endDate);
                model.addAttribute("dailyStats", dailyStats);
            } catch (Exception e) {
                log.error("일별 통계 로딩 중 오류", e);
                model.addAttribute("dailyStatsError", "일별 통계를 불러오는 중 오류가 발생했습니다.");
            }

            // 예산 분포 데이터 로드
            try {
                List<BikeAnalyticsDTO> budgetStats = bikeAnalyticsService.getBudgetDistribution();
                model.addAttribute("budgetStats", budgetStats);
            } catch (Exception e) {
                log.error("예산 분포 로딩 중 오류", e);
                model.addAttribute("budgetStatsError", "예산 분포를 불러오는 중 오류가 발생했습니다.");
            }

            // 엔진 용량 선호도 데이터 로드
            try {
                List<BikeAnalyticsDTO> engineStats = bikeAnalyticsService.getEngineCapacityPreference();
                model.addAttribute("engineStats", engineStats);
            } catch (Exception e) {
                log.error("엔진 용량 선호도 로딩 중 오류", e);
                model.addAttribute("engineStatsError", "엔진 용량 선호도를 불러오는 중 오류가 발생했습니다.");
            }

            // 주행 스타일 선호도 데이터 로드
            try {
                List<BikeAnalyticsDTO> ridingStats = bikeAnalyticsService.getRidingStylePreference();
                model.addAttribute("ridingStats", ridingStats);
            } catch (Exception e) {
                log.error("주행 스타일 선호도 로딩 중 오류", e);
                model.addAttribute("ridingStatsError", "주행 스타일 선호도를 불러오는 중 오류가 발생했습니다.");
            }

            // 속도 범위 선호도 데이터 로드
            try {
                List<BikeAnalyticsDTO> speedStats = bikeAnalyticsService.getSpeedRangePreference();
                model.addAttribute("speedStats", speedStats);
            } catch (Exception e) {
                log.error("속도 범위 선호도 로딩 중 오류", e);
                model.addAttribute("speedStatsError", "속도 범위 선호도를 불러오는 중 오류가 발생했습니다.");
            }

            // 주행 거리 선호도 데이터 로드
            try {
                List<BikeAnalyticsDTO> distanceStats = bikeAnalyticsService.getRidingDistancePreference();
                model.addAttribute("distanceStats", distanceStats);
            } catch (Exception e) {
                log.error("주행 거리 선호도 로딩 중 오류", e);
                model.addAttribute("distanceStatsError", "주행 거리 선호도를 불러오는 중 오류가 발생했습니다.");
            }

// RPM 선호도 데이터 로드
            try {
                List<BikeAnalyticsDTO> rpmStats = bikeAnalyticsService.getRpmPreference();
                model.addAttribute("rpmStats", rpmStats);
            } catch (Exception e) {
                log.error("RPM 선호도 로딩 중 오류", e);
                model.addAttribute("rpmStatsError", "RPM 선호도를 불러오는 중 오류가 발생했습니다.");
            }

            // 스타일 선호도 데이터 로드
            try {
                List<BikeAnalyticsDTO> styleStats = bikeAnalyticsService.getStylePreference();
                model.addAttribute("styleStats", styleStats);
            } catch (Exception e) {
                log.error("스타일 선호도 로딩 중 오류", e);
                model.addAttribute("styleStatsError", "스타일 선호도를 불러오는 중 오류가 발생했습니다.");
            }

            // 상위 추천 모델 데이터 로드
            try {
                List<BikeAnalyticsDTO> topModels = bikeAnalyticsService.getTopRecommendedModels(10);
                model.addAttribute("topModels", topModels);
            } catch (Exception e) {
                log.error("인기 모델 로딩 중 오류", e);
                model.addAttribute("topModelsError", "인기 모델을 불러오는 중 오류가 발생했습니다.");
            }

            // 브랜드 추천 빈도 데이터 로드
            try {
                List<BikeAnalyticsDTO> brandStats = bikeAnalyticsService.getBrandRecommendationFrequency();
                model.addAttribute("brandStats", brandStats);
            } catch (Exception e) {
                log.error("브랜드 추천 빈도 로딩 중 오류", e);
                model.addAttribute("brandStatsError", "브랜드 추천 빈도를 불러오는 중 오류가 발생했습니다.");
            }

            // 브랜드 트렌드 데이터 로드
            try {
                List<BikeAnalyticsDTO> brandTrends = bikeAnalyticsService.getBrandTrendsByPeriod(startDate, endDate);
                model.addAttribute("brandTrends", brandTrends);
            } catch (Exception e) {
                log.error("브랜드 트렌드 로딩 중 오류", e);
                model.addAttribute("brandTrendsError", "브랜드 트렌드를 불러오는 중 오류가 발생했습니다.");
            }

            // 예산별 선호 모델 데이터 로드
            try {
                List<BikeAnalyticsDTO> budgetModels = bikeAnalyticsService.getPreferredModelsByBudget();
                model.addAttribute("budgetModels", budgetModels);
            } catch (Exception e) {
                log.error("예산별 선호 모델 로딩 중 오류", e);
                model.addAttribute("budgetModelsError", "예산별 선호 모델을 불러오는 중 오류가 발생했습니다.");
            }

            // 용도별 선호 모델 데이터 로드
            try {
                List<BikeAnalyticsDTO> purposeModels = bikeAnalyticsService.getPreferredModelsByPurpose();
                model.addAttribute("purposeModels", purposeModels);
            } catch (Exception e) {
                log.error("용도별 선호 모델 로딩 중 오류", e);
                model.addAttribute("purposeModelsError", "용도별 선호 모델을 불러오는 중 오류가 발생했습니다.");
            }

            // 스타일 조합 분석 데이터 로드
            try {
                List<BikeAnalyticsDTO> styleCombinations = bikeAnalyticsService.getStyleCombinationAnalysis();
                model.addAttribute("styleCombinations", styleCombinations);
            } catch (Exception e) {
                log.error("스타일 조합 분석 로딩 중 오류", e);
                model.addAttribute("styleCombinationsError", "스타일 조합 분석을 불러오는 중 오류가 발생했습니다.");
            }

            // 월별 추천 트렌드 데이터 로드
            try {
                List<BikeAnalyticsDTO> monthlyTrends = bikeAnalyticsService.getMonthlyRecommendationTrends();
                model.addAttribute("monthlyTrends", monthlyTrends);
            } catch (Exception e) {
                log.error("월별 추천 트렌드 로딩 중 오류", e);
                model.addAttribute("monthlyTrendsError", "월별 추천 트렌드를 불러오는 중 오류가 발생했습니다.");
            }

            // 계절별 선호도 데이터 로드
            try {
                List<BikeAnalyticsDTO> seasonalPrefs = bikeAnalyticsService.getSeasonalPreferences();
                model.addAttribute("seasonalPrefs", seasonalPrefs);
            } catch (Exception e) {
                log.error("계절별 선호도 로딩 중 오류", e);
                model.addAttribute("seasonalPrefsError", "계절별 선호도를 불러오는 중 오류가 발생했습니다.");
            }

            // 분석 기간 데이터를 모델에 추가
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);

        } catch (Exception e) {
            // 전체 데이터 로드 실패 시 오류 로깅
            log.error("전체 데이터 로딩 중 오류 발생", e);
            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "ANALYTICS_DATA_LOAD_FAILURE",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            // 예외를 상위로 전파
            throw new RuntimeException("데이터 로딩 중 오류가 발생했습니다.", e);
        }
    }
}