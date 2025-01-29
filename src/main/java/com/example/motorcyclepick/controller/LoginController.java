package com.example.motorcyclepick.controller;

import com.example.motorcyclepick.config.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private SecurityLogger securityLogger;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            HttpServletRequest request,
                            Model model) {

        HttpSession session = request.getSession(false);
        String errorMessage = null;

        if (session != null) {
            Exception exception = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null) {
                errorMessage = exception.getMessage();
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }

        if (error != null) {
            model.addAttribute("error", errorMessage != null ?
                    errorMessage : "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
            securityLogger.logSecurityEvent(
                    "LOGIN_FAILURE",
                    request.getParameter("id"),
                    request.getRemoteAddr()
            );
        }

        if (logout != null) {
            model.addAttribute("message", "로그아웃되었습니다.");
            securityLogger.logSecurityEvent(
                    "LOGOUT_SUCCESS",
                    "anonymous",
                    request.getRemoteAddr()
            );
        }

        return "login";
    }

    @PostMapping("/loginProc")
    public String processLogin(@RequestParam String id,
                               @RequestParam String password,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            if (id == null || id.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                throw new ServletException("아이디와 비밀번호를 입력해주세요.");
            }

            request.login(id, password);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                securityLogger.logSecurityEvent(
                        "LOGIN_SUCCESS",
                        auth.getName(),
                        request.getRemoteAddr()
                );

                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(3600); // 1시간
            }

            return "redirect:/";

        } catch (ServletException e) {
            securityLogger.logSecurityEvent(
                    "LOGIN_FAILURE",
                    id,
                    request.getRemoteAddr()
            );

            redirectAttributes.addFlashAttribute("loginError",
                    "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                securityLogger.logSecurityEvent(
                        "LOGOUT_INITIATED",
                        auth.getName(),
                        request.getRemoteAddr()
                );
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            SecurityContextHolder.clearContext();
            request.logout();

            redirectAttributes.addFlashAttribute("message", "성공적으로 로그아웃되었습니다.");

        } catch (ServletException e) {
            securityLogger.logSecurityEvent(
                    "LOGOUT_FAILURE",
                    "anonymous",
                    request.getRemoteAddr()
            );
            redirectAttributes.addFlashAttribute("error", "로그아웃 처리 중 오류가 발생했습니다.");
        }

        return "redirect:/login?logout=true";
    }
}