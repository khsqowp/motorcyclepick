package com.example.motorcyclepick.controller;

// 필요한 Spring 및 기타 라이브러리 import

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.exception.AuthorizationException;
import com.example.motorcyclepick.exception.UserAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// 로그인 관련 요청을 처리하는 컨트롤러
@Controller
public class LoginController {

    // 보안 관련 로깅을 위한 SecurityLogger 주입
    @Autowired
    private SecurityLogger securityLogger;

    // 로그인 페이지를 보여주는 메서드
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            HttpServletRequest request,
                            Model model) {

        // 현재 세션 가져오기 (새로 생성하지 않음)
        HttpSession session = request.getSession(false);
        String errorMessage = null;

        // 세션에서 인증 예외 정보 확인
        if (session != null) {
            Exception exception = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null) {
                errorMessage = exception.getMessage();
                // 예외 정보 세션에서 제거
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }

        // 로그인 실패시 에러 메시지 설정
        if (error != null) {
            model.addAttribute("error", errorMessage != null ?
                    errorMessage : "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
            // 로그인 실패 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "LOGIN_FAILURE",
                    request.getParameter("id"),
                    request.getRemoteAddr()
            );
        }

        // 로그아웃 성공시 메시지 설정
        if (logout != null) {
            model.addAttribute("message", "로그아웃되었습니다.");
            // 로그아웃 성공 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "LOGOUT_SUCCESS",
                    "anonymous",
                    request.getRemoteAddr()
            );
        }

        // login 뷰 반환
        return "login";
    }

    // 로그인 처리를 수행하는 메서드
    @PostMapping("/loginProc")
    public String processLogin(@RequestParam String id,
                               @RequestParam String password,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            // 아이디와 비밀번호 유효성 검사
            if (id == null || id.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                throw new UserAuthenticationException("아이디와 비밀번호를 입력해주세요.");
            }

            // 로그인 수행
            try {
                request.login(id, password);
            } catch (ServletException e) {
                throw new UserAuthenticationException("로그인에 실패했습니다.", e);
            }

            // 인증 정보 확인
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                // 로그인 성공 이벤트 로깅
                securityLogger.logSecurityEvent(
                        "LOGIN_SUCCESS",
                        auth.getName(),
                        request.getRemoteAddr()
                );

                // 세션 생성 및 만료 시간 설정
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(3600); // 1시간
            }

            // 메인 페이지로 리다이렉트
            return "redirect:/";

        } catch (UserAuthenticationException e) {
            // 로그인 실패 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "LOGIN_FAILURE",
                    id,
                    request.getRemoteAddr()
            );

            // 에러 메시지 설정 및 로그인 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("loginError", e.getMessage());
            return "redirect:/login?error=true";
        }
    }

    // 로그아웃 처리를 수행하는 메서드
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            // 현재 인증 정보 확인
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                // 로그아웃 시작 이벤트 로깅
                securityLogger.logSecurityEvent(
                        "LOGOUT_INITIATED",
                        auth.getName(),
                        request.getRemoteAddr()
                );
            }

            // 세션 무효화
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // 보안 컨텍스트 클리어
            SecurityContextHolder.clearContext();
            // 로그아웃 수행
            try {
                request.logout();
            } catch (ServletException e) {
                throw new AuthorizationException("로그아웃 처리 중 오류가 발생했습니다.", e);
            }

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("message", "성공적으로 로그아웃되었습니다.");

        } catch (AuthorizationException e) {
            // 로그아웃 실패 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "LOGOUT_FAILURE",
                    "anonymous",
                    request.getRemoteAddr()
            );
            // 에러 메시지 설정
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // 로그인 페이지로 리다이렉트
        return "redirect:/login?logout=true";
    }
}