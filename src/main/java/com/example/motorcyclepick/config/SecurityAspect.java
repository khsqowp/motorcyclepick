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

@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {

    private final SecurityLogger securityLogger;

    @Around("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public Object logSecureAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        try {
            Object result = joinPoint.proceed();
            securityLogger.logSecurityEvent(
                    "ACCESS_SUCCESS",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            return result;
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "ACCESS_FAILURE",
                    auth.getName(),
                    request.getRemoteAddr()
            );
            throw e;
        }
    }

    @Around("execution(* com.example.motorcyclepick.controller.*.*(..))")
    public Object logControllerAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        String methodName = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            securityLogger.logSecurityEvent(
                    "CONTROLLER_ACCESS",
                    username,
                    request.getRemoteAddr()
            );
            return result;
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "CONTROLLER_ACCESS_FAILURE",
                    username,
                    request.getRemoteAddr()
            );
            throw e;
        }
    }
}
