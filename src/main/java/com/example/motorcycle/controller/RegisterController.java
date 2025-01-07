package com.example.motorcycle.controller;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.domain.UserDomain;
import com.example.motorcycle.dto.UserDTO;
import com.example.motorcycle.service.EmailService;
import com.example.motorcycle.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Slf4j
public class RegisterController {

    @Autowired
    private EmailService emailService;
    private Map<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogger securityLogger;
    private final CsrfTokenRepository csrftokenRepository;

    public RegisterController(UserService userService,
                              PasswordEncoder passwordEncoder,
                              SecurityLogger securityLogger,
                              CsrfTokenRepository csrfTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityLogger = securityLogger;
        this.csrftokenRepository = csrfTokenRepository;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserDTO userDTO, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            securityLogger.logSecurityEvent(
                    "REGISTER_VALIDATION_FAILURE",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            model.addAttribute("user", userDTO);
            return "register";
        }


        // 비밀번호 암호화
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // 기본 역할 설정
        userDTO.setRole("ROLE_USER");

//        userService.registerUser(userDTO);

        try {
            userService.registerUser(userDTO);
            return "redirect:/login";
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "REGISTER_ERROR",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            model.addAttribute("user", userDTO);
            model.addAttribute("error", e.getMessage());
            return "register";
        }

    }

    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @PostMapping("/send-verification")
    @ResponseBody
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> payload,
                                                   HttpServletRequest request) {
        log.debug("이메일 인증 요청 수신: {}", payload.get("email"));

        try {
            String email = payload.get("email");
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("이메일이 제공되지 않았습니다.");
            }

            String code = emailService.sendVerificationEmail(email);

            // 코드 생성 및 저장 로그 추가
            log.debug("인증 코드 생성 완료: {}, 코드: {}", email, code);
            verificationCodes.put(email, new VerificationInfo(code, LocalDateTime.now().plusMinutes(5)));

            return ResponseEntity.ok().body("이메일이 성공적으로 전송되었습니다.");
        } catch (MailException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 전송 실패: " + e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    @ResponseBody
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        VerificationInfo info = verificationCodes.get(email);
        if (info != null && info.getCode().equals(code) && LocalDateTime.now().isBefore(info.getExpiry())) {
            verificationCodes.remove(email);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("잘못된 인증번호 또는 만료된 인증번호입니다.");
    }
    static class VerificationInfo {
        private final String code;
        private final LocalDateTime expiry;

        public VerificationInfo(String code, LocalDateTime expiry) {
            this.code = code;
            this.expiry = expiry;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }

        @Override
        public String toString() {
            return "VerificationInfo{" +
                    "code='" + code + '\'' +
                    ", expiry=" + expiry +
                    '}';
        }
    }

}