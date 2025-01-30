package com.example.motorcyclepick.config;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

// AOP를 사용한 보안 로깅을 위한 Aspect 클래스
@Aspect
// 스프링 컴포넌트로 등록
@Component
// Lombok을 사용한 생성자 주입
@RequiredArgsConstructor
public class SecurityAspect {

    // 보안 로깅을 위한 의존성 주입
    private final SecurityLogger securityLogger;

    // @PreAuthorize 어노테이션이 붙은 메소드의 보안 접근 로깅
    @Around("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public Object logSecureAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 현재 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 현재 요청 정보 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        try {
            // 메소드 실행
            Object result = joinPoint.proceed();
            // 성공 로그 기록
            securityLogger.logSecurityEvent(
                    "ACCESS_SUCCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            return result;
        } catch (Exception e) {
            // 실패 로그 기록
            securityLogger.logSecurityEvent(
                    "ACCESS_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    // 모든 컨트롤러 메소드 접근 로깅
    @Around("execution(* com.example.motorcyclepick.controller.*.*(..))")
    public Object logControllerAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 현재 요청 정보 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // 인증 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 사용자명 (인증되지 않은 경우 "anonymous")
        String username = auth != null ? auth.getName() : "anonymous";
        // 호출된 메소드명
        String methodName = joinPoint.getSignature().getName();

        try {
            // 메소드 실행
            Object result = joinPoint.proceed();
            // 컨트롤러 접근 성공 로그
            securityLogger.logSecurityEvent(
                    "CONTROLLER_ACCESS",
                    username,
                    request.getRemoteAddr()
            );
            return result;
        } catch (Exception e) {
            // 컨트롤러 접근 실패 로그
            securityLogger.logSecurityEvent(
                    "CONTROLLER_ACCESS_FAILURE",
                    username,
                    request.getRemoteAddr()
            );
            throw e;
        }
    }
}