// 보안 관련 로깅을 담당하는 설정 패키지
package com.example.motorcyclepick.config;

// 필요한 라이브러리 임포트

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 스프링 컴포넌트로 등록하여 자동 빈 생성
@Component
// Lombok을 사용한 로깅 기능 활성화
@Slf4j
public class SecurityLogger {
    // 최대 로그인 시도 횟수 상수 정의
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    // IP 차단 지속 시간 설정 (30분)
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(30);

    // IP별 로그인 시도 횟수를 저장하는 동시성 지원 맵
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    // 차단된 IP와 차단 시작 시간을 저장하는 동시성 지원 맵
    private final Map<String, LocalDateTime> blockedIPs = new ConcurrentHashMap<>();

    // 보안 이벤트를 저장하는 내부 클래스
    @Data  // Lombok을 사용하여 getter/setter 자동 생성
    @AllArgsConstructor  // 모든 필드를 파라미터로 받는 생성자 자동 생성
    public static class SecurityEvent {
        private String event;      // 이벤트 유형
        private String username;   // 사용자 이름
        private String ipAddress;  // IP 주소
        private LocalDateTime timestamp;  // 이벤트 발생 시간

        // 이벤트 정보를 문자열로 변환하는 메서드 오버라이드
        @Override
        public String toString() {
            return String.format("SecurityEvent[event=%s, user=%s, ip=%s, time=%s]",
                    event, username, ipAddress, timestamp);
        }
    }

    // 보안 이벤트를 로깅하는 메서드
    public void logSecurityEvent(String event, String username, String ipAddress) {
        SecurityEvent securityEvent = new SecurityEvent(event, username, ipAddress, LocalDateTime.now());
        log.info("Security Event: {}", securityEvent);

        if ("USER_LOGIN_SUCCESS".equals(event) || "LOGOUT_SUCCESS".equals(event)) {
            // 로그인 성공 또는 로그아웃 시 IP 차단 상태 초기화
            resetLoginAttempts(ipAddress);
        } else if ("LOGIN_FAILURE".equals(event)) {
            handleFailedLogin(ipAddress);
        }
    }

    // 로그인 실패 처리 메서드
    private void handleFailedLogin(String ipAddress) {
        // IP가 이미 차단되었는지 확인
        if (isIPBlocked(ipAddress)) {
            log.warn("Blocked IP {} attempted to login", ipAddress);
            return;
        }

        // 해당 IP의 로그인 시도 횟수 증가
        int attempts = loginAttempts.getOrDefault(ipAddress, 0) + 1;
        loginAttempts.put(ipAddress, attempts);

        // 최대 시도 횟수 초과 시 IP 차단
        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            blockIP(ipAddress);
        }
    }

    // IP 차단 상태 확인 메서드
    private boolean isIPBlocked(String ipAddress) {
        LocalDateTime blockedTime = blockedIPs.get(ipAddress);
        if (blockedTime != null) {
            // 차단 기간이 지나지 않았으면 true 반환
            if (LocalDateTime.now().isBefore(blockedTime.plus(BLOCK_DURATION))) {
                return true;
            } else {
                // 차단 기간이 지났으면 차단 정보 삭제
                blockedIPs.remove(ipAddress);
                loginAttempts.remove(ipAddress);
            }
        }
        return false;
    }

    // IP 차단 카운터 초기화 메서드 추가
    public void resetLoginAttempts(String ipAddress) {
        // IP 차단 상태와 로그인 시도 횟수를 모두 초기화
        loginAttempts.remove(ipAddress);
        blockedIPs.remove(ipAddress);
        log.info("Successfully reset login attempts and block status for IP: {}", ipAddress);
    }

    // IP 차단 처리 메서드
    private void blockIP(String ipAddress) {
        // IP 차단 시작 시간 저장
        blockedIPs.put(ipAddress, LocalDateTime.now());
        // 차단 로그 기록
        log.warn("IP {} has been blocked due to multiple failed login attempts", ipAddress);
    }

    // 데이터 접근 이벤트 로깅 메서드
    public void logDataAccessEvent(String username, String action, String resource) {
        SecurityEvent event = new SecurityEvent(
                "DATA_ACCESS",
                username,
                getClientIP(),
                LocalDateTime.now()
        );
        log.info("Data Access Event: {} - Action: {}, Resource: {}", event, action, resource);
    }

    // 시스템 이벤트 로깅 메서드
    public void logSystemEvent(String eventType, String details) {
        SecurityEvent event = new SecurityEvent(
                eventType,
                "SYSTEM",
                "internal",
                LocalDateTime.now()
        );
        log.info("System Event: {} - Details: {}", event, details);
    }

    // 클라이언트 IP 주소를 가져오는 유틸리티 메서드
    private String getClientIP() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getRemoteAddr();
        } catch (Exception e) {
            return "unknown";
        }
    }
}