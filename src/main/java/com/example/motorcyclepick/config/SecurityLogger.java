package com.example.motorcyclepick.config;

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

@Component
@Slf4j
public class SecurityLogger {
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(30);

    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> blockedIPs = new ConcurrentHashMap<>();

    @Data
    @AllArgsConstructor
    public static class SecurityEvent {
        private String event;
        private String username;
        private String ipAddress;
        private LocalDateTime timestamp;

        @Override
        public String toString() {
            return String.format("SecurityEvent[event=%s, user=%s, ip=%s, time=%s]",
                    event, username, ipAddress, timestamp);
        }
    }

    public void logSecurityEvent(String event, String username, String ipAddress) {
        SecurityEvent securityEvent = new SecurityEvent(event, username, ipAddress, LocalDateTime.now());
        log.info("Security Event: {}", securityEvent);

        if ("LOGIN_FAILURE".equals(event)) {
            handleFailedLogin(ipAddress);
        }
    }

    private void handleFailedLogin(String ipAddress) {
        if (isIPBlocked(ipAddress)) {
            log.warn("Blocked IP {} attempted to login", ipAddress);
            return;
        }

        int attempts = loginAttempts.getOrDefault(ipAddress, 0) + 1;
        loginAttempts.put(ipAddress, attempts);

        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            blockIP(ipAddress);
        }
    }

    private boolean isIPBlocked(String ipAddress) {
        LocalDateTime blockedTime = blockedIPs.get(ipAddress);
        if (blockedTime != null) {
            if (LocalDateTime.now().isBefore(blockedTime.plus(BLOCK_DURATION))) {
                return true;
            } else {
                blockedIPs.remove(ipAddress);
                loginAttempts.remove(ipAddress);
            }
        }
        return false;
    }

    private void blockIP(String ipAddress) {
        blockedIPs.put(ipAddress, LocalDateTime.now());
        log.warn("IP {} has been blocked due to multiple failed login attempts", ipAddress);
    }

    public void logDataAccessEvent(String username, String action, String resource) {
        SecurityEvent event = new SecurityEvent(
                "DATA_ACCESS",
                username,
                getClientIP(),
                LocalDateTime.now()
        );
        log.info("Data Access Event: {} - Action: {}, Resource: {}", event, action, resource);
    }

    public void logSystemEvent(String eventType, String details) {
        SecurityEvent event = new SecurityEvent(
                eventType,
                "SYSTEM",
                "internal",
                LocalDateTime.now()
        );
        log.info("System Event: {} - Details: {}", event, details);
    }

    private String getClientIP() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getRemoteAddr();
        } catch (Exception e) {
            return "unknown";
        }
    }
}