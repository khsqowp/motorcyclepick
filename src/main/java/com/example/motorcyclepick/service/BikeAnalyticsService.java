package com.example.motorcyclepick.service;

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.dto.BikeAnalyticsDTO;
import com.example.motorcyclepick.repository.BikeAnalyticsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BikeAnalyticsService {
    private final BikeAnalyticsMapper bikeAnalyticsMapper;
    private final SecurityLogger securityLogger;

    @Autowired
    private SecurityService securityService;

    // 추가할 보안 코드
    private void validateAccess(String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("인증되지 않은 접근입니다.");
        }

        String sanitizedAction = securityService.sanitizeInput(action);
        securityLogger.logDataAccessEvent(auth.getName(), "ANALYTICS_ACCESS_VALIDATION", sanitizedAction);
    }

    public List<BikeAnalyticsDTO> findDailyResponseStats(LocalDate startDate, LocalDate endDate) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_DAILY_RESPONSE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            // 여기를 수정합니다
            List<Map<String, Object>> results = bikeAnalyticsMapper.findDailyResponseStats(startDate, endDate);

            // convertMapToDTO 메서드를 직접 사용하여 변환
            return results.stream()
                    .map(this::convertMapToDTO)  // 기존의 convertMapToDTO 메서드 활용
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_DAILY_RESPONSE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getBudgetDistribution() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_BUDGET_DISTRIBUTION_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getBudgetDistribution();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            // 여기에 추가
            log.error("보안 위반 시도 감지: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "SECURITY_VIOLATION",
                    auth != null ? auth.getName() : "anonymous",
                    request.getRemoteAddr()
            );
            throw new SecurityException("보안 위반이 감지되었습니다.", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<BikeAnalyticsDTO> getEngineCapacityPreference() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_ENGINE_CAPACITY_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getEngineCapacityPreference();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_ENGINE_CAPACITY_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getRidingStylePreference() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_RIDING_STYLE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getRidingStylePreference();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_RIDING_STYLE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getSpeedRangePreference() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_SPEED_RANGE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getSpeedRangePreference();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_SPEED_RANGE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getRidingDistancePreference() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_RIDING_DISTANCE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getRidingDistancePreference();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_RIDING_DISTANCE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getRpmPreference() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_RPM_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getRpmPreference();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_RPM_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getStylePreference() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_STYLE_PREFERENCE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getStylePreference();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_STYLE_PREFERENCE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getTopRecommendedModels(int limit) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_TOP_MODELS_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getTopRecommendedModels(limit);
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_TOP_MODELS_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<BikeAnalyticsDTO> getBrandRecommendationFrequency() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_BRAND_FREQUENCY_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getBrandRecommendationFrequency();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_BRAND_FREQUENCY_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    private BikeAnalyticsDTO convertMapToDTO(Map<String, Object> map) {
        // 추가할 보안 코드
        if (map == null || map.isEmpty()) {
            log.warn("빈 데이터 맵이 전달되었습니다.");
            throw new IllegalArgumentException("유효하지 않은 데이터입니다.");
        }

        // XSS 방지
        map.forEach((key, value) -> {
            if (value instanceof String) {
                map.put(key, securityService.sanitizeInput((String) value));
            }
        });

        BikeAnalyticsDTO dto = new BikeAnalyticsDTO();

        if(map.containsKey("survey_date")) {
            Object dateObj = map.get("survey_date");
            if (dateObj instanceof java.sql.Date) {
                java.sql.Date sqlDate = (java.sql.Date) dateObj;
                dto.setSurveyDate(sqlDate.toLocalDate());
            }
        }
        if(map.containsKey("daily_count")) {
            Object countObj = map.get("daily_count");
            if (countObj instanceof Long) {
                dto.setDailyCount(((Long) countObj).intValue());
            } else if (countObj instanceof Integer) {
                dto.setDailyCount((Integer) countObj);
            } else if (countObj instanceof BigDecimal) {
                dto.setDailyCount(((BigDecimal) countObj).intValue());
            }
        }
        if(map.containsKey("budget_range")) dto.setBudgetRange((String) map.get("budget_range"));
        if(map.containsKey("count")) {
            Object countObj = map.get("count");
            if (countObj instanceof BigDecimal) {
                dto.setCount(((BigDecimal) countObj).intValue());
            } else if (countObj instanceof Long) {
                dto.setCount(((Long) countObj).intValue());
            } else if (countObj instanceof Integer) {
                dto.setCount((Integer) countObj);
            }
        }
        if(map.containsKey("q2_value")) dto.setQ2Value((String) map.get("q2_value"));
        if(map.containsKey("q3_value")) dto.setQ3Value((String) map.get("q3_value"));
        if(map.containsKey("q4_value")) dto.setQ4Value((String) map.get("q4_value"));
        if(map.containsKey("q5_value")) dto.setQ5Value((String) map.get("q5_value"));
        if(map.containsKey("q6_value")) dto.setQ6Value((String) map.get("q6_value"));
        if(map.containsKey("style")) {
            String style = (String) map.get("style");
            dto.setQ7Value1(style);
            dto.setQ7Value2(style);
        }
        if(map.containsKey("brand")) dto.setBrand((String) map.get("brand"));
        if(map.containsKey("model")) dto.setModel((String) map.get("model"));
        if(map.containsKey("total_recommendations")) {
            Object countObj = map.get("total_recommendations");
            if (countObj instanceof BigDecimal) {
                dto.setTotalRecommendations(((BigDecimal) countObj).intValue());
            } else if (countObj instanceof Long) {
                dto.setTotalRecommendations(((Long) countObj).intValue());
            } else if (countObj instanceof Integer) {
                dto.setTotalRecommendations((Integer) countObj);
            }
        }
        if(map.containsKey("total_count")) {
            Object countObj = map.get("total_count");
            if (countObj instanceof BigDecimal) {
                dto.setRecommendationCount(((BigDecimal) countObj).intValue());
            } else if (countObj instanceof Long) {
                dto.setRecommendationCount(((Long) countObj).intValue());
            } else if (countObj instanceof Integer) {
                dto.setRecommendationCount((Integer) countObj);
            }
        }
        if(map.containsKey("combination_count")) {
            Object countObj = map.get("combination_count");
            if (countObj instanceof BigDecimal) {
                dto.setCombinationCount(((BigDecimal) countObj).intValue());
            } else if (countObj instanceof Long) {
                dto.setCombinationCount(((Long) countObj).intValue());
            } else if (countObj instanceof Integer) {
                dto.setCombinationCount((Integer) countObj);
            }
        }
        if(map.containsKey("monthly_count")) {
            Object countObj = map.get("monthly_count");
            if (countObj instanceof BigDecimal) {
                dto.setMonthlyCount(((BigDecimal) countObj).intValue());
            } else if (countObj instanceof Long) {
                dto.setMonthlyCount(((Long) countObj).intValue());
            } else if (countObj instanceof Integer) {
                dto.setMonthlyCount((Integer) countObj);
            }
        }
        if(map.containsKey("preference_count")) {
            Object countObj = map.get("preference_count");
            if (countObj instanceof BigDecimal) {
                dto.setPreferenceCount(((BigDecimal) countObj).intValue());
            } else if (countObj instanceof Long) {
                dto.setPreferenceCount(((Long) countObj).intValue());
            } else if (countObj instanceof Integer) {
                dto.setPreferenceCount((Integer) countObj);
            }
        }
        if(map.containsKey("riding_purpose")) dto.setRidingPurpose((String) map.get("riding_purpose"));
        if(map.containsKey("riding_style")) dto.setRidingStyle((String) map.get("riding_style"));
        if(map.containsKey("season")) dto.setSeason((String) map.get("season"));

        return dto;
    }

    public List<BikeAnalyticsDTO> getBrandTrendsByPeriod(LocalDate startDate, LocalDate endDate) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_BRAND_TRENDS_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getBrandTrendsByPeriod(startDate, endDate);
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_BRAND_TRENDS_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getPreferredModelsByBudget() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_MODELS_BY_BUDGET_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getPreferredModelsByBudget();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_MODELS_BY_BUDGET_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getPreferredModelsByPurpose() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_MODELS_BY_PURPOSE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getPreferredModelsByPurpose();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_MODELS_BY_PURPOSE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getStyleCombinationAnalysis() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_STYLE_COMBINATION_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getStyleCombinationAnalysis();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_STYLE_COMBINATION_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getMonthlyRecommendationTrends() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            List<Map<String, Object>> results = bikeAnalyticsMapper.getMonthlyRecommendationTrends();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_MONTHLY_TRENDS_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

// 데이터 변환 및 그룹핑
            return results.stream()
                    .map(map -> {
                        Object countObj = map.get("monthly_count");
                        Integer monthlyCount = null;
                        if (countObj instanceof BigDecimal) {
                            monthlyCount = ((BigDecimal) countObj).intValue();
                        } else if (countObj instanceof Long) {
                            monthlyCount = ((Long) countObj).intValue();
                        } else if (countObj instanceof Integer) {
                            monthlyCount = (Integer) countObj;
                        }

                        LocalDate date = ((java.sql.Date) map.get("survey_date")).toLocalDate();
                        String month = String.format("%d-%02d", date.getYear(), date.getMonthValue());

                        BikeAnalyticsDTO dto = new BikeAnalyticsDTO();
                        dto.setSurveyDate(date);
                        dto.setBrand((String) map.get("brand"));
                        dto.setModel((String) map.get("model"));
                        dto.setMonthlyCount(monthlyCount);
                        return dto;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_MONTHLY_TRENDS_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    public List<BikeAnalyticsDTO> getSeasonalPreferences() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_SEASONAL_PREFERENCE_ACCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );

            List<Map<String, Object>> results = bikeAnalyticsMapper.getSeasonalPreferences();
            return results.stream()
                    .map(this::convertMapToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            securityLogger.logSecurityEvent(
                    "ANALYTICS_SEASONAL_PREFERENCE_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }
}