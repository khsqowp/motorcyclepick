package com.example.motorcyclepick.controller;

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.domain.UserDomain;
import com.example.motorcyclepick.dto.UserDTO;
import com.example.motorcyclepick.service.EmailService;
import com.example.motorcyclepick.service.SecurityService;
import com.example.motorcyclepick.service.UserService;
import lombok.RequiredArgsConstructor;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Slf4j
@RequiredArgsConstructor  // 이것으로 final 필드들의 생성자 주입이 자동으로 처리됨
public class RegisterController {
    private final Map<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogger securityLogger;
    private final CsrfTokenRepository csrfTokenRepository;
    private final SecurityService securityService;
    private final EmailService emailService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO,
                                      BindingResult result,
                                      HttpServletRequest request) {
        log.debug("수신된 회원가입 데이터: {}", userDTO);

        // 입력값 검증
        if (result.hasErrors()) {
            log.error("유효성 검사 실패: {}", result.getAllErrors());
            securityLogger.logSecurityEvent(
                    "REGISTER_VALIDATION_FAILURE",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            return ResponseEntity.badRequest()
                    .body(result.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            // XSS 방지를 위한 입력값 검증
            userDTO.setId(securityService.sanitizeInput(userDTO.getId()));
            userDTO.setEmail(securityService.sanitizeInput(userDTO.getEmail()));
            userDTO.setUsername(securityService.sanitizeInput(userDTO.getUsername()));

            // 기본값 설정
            userDTO.setBirthDate(validateAndConvertBirthDate(userDTO.getBirthDate()));
            userDTO.setRegion(validateAndSetDefaultRegion(userDTO.getRegion()));
            userDTO.setPhoneNumber(formatPhoneNumber(userDTO.getPhoneNumber()));
            userDTO.setInstagram(securityService.sanitizeInput(userDTO.getInstagram()));

            // 비밀번호 암호화 및 기본 역할 설정
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setRole("ROLE_USER");

            // 생성일시, 수정일시 설정
            LocalDateTime now = LocalDateTime.now();
            userDTO.setCreatedAt(now);
            userDTO.setUpdatedAt(now);

            userService.registerUser(userDTO);
            log.debug("회원가입 성공: {}", userDTO.getId());

            // 보안 로깅
            securityLogger.logSecurityEvent(
                    "REGISTER_SUCCESS",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "REGISTER_ERROR",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private LocalDate validateAndConvertBirthDate(LocalDate birthDate) {
        return birthDate == null ? LocalDate.of(2000, 1, 1) : birthDate;
    }

    private String validateAndSetDefaultRegion(String region) {
        return (region == null || region.trim().isEmpty()) ? "Unknown" : region;
    }

    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }
        String numbers = phoneNumber.replaceAll("[^0-9]", "");
        if (numbers.length() == 11) {
            return String.format("%s-%s-%s",
                    numbers.substring(0, 3),
                    numbers.substring(3, 7),
                    numbers.substring(7));
        }
        return phoneNumber;
    }


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
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload,
                                        HttpServletRequest request) {
        String email = securityService.sanitizeInput(payload.get("email"));
        String code = securityService.sanitizeInput(payload.get("code"));

        if (email == null || code == null) {
            securityLogger.logSecurityEvent(
                    "VERIFY_CODE_INVALID_INPUT",
                    "anonymous",
                    request.getRemoteAddr()
            );
            return ResponseEntity.badRequest().body("이메일과 인증코드는 필수입니다.");
        }

        VerificationInfo info = verificationCodes.get(email);
        if (info != null && info.getCode().equals(code) && LocalDateTime.now().isBefore(info.getExpiry())) {
            verificationCodes.remove(email);
            securityLogger.logSecurityEvent(
                    "VERIFY_CODE_SUCCESS",
                    email,
                    request.getRemoteAddr()
            );
            return ResponseEntity.ok().build();
        }

        securityLogger.logSecurityEvent(
                "VERIFY_CODE_FAILURE",
                email,
                request.getRemoteAddr()
        );
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

    @GetMapping("/check-id/{id}")
    @ResponseBody
    public ResponseEntity<?> checkIdDuplicate(@PathVariable String id,
                                              HttpServletRequest request) {
        try {
            // 무작위 지연 시간 추가 (200~500ms)
            Thread.sleep((long) (Math.random() * 300) + 200);

            // SecurityLogger를 통한 로깅
            securityLogger.logSecurityEvent(
                    "ID_CHECK_REQUEST",
                    id,
                    request.getRemoteAddr()
            );

            if (userService.findById(id) != null) {
                securityLogger.logSecurityEvent(
                        "ID_CHECK_DUPLICATE",
                        id,
                        request.getRemoteAddr()
                );
                return ResponseEntity.ok().body("duplicate");
            }
            return ResponseEntity.ok().body("available");
        } catch (InterruptedException e) {
            securityLogger.logSecurityEvent(
                    "ID_CHECK_ERROR",
                    id,
                    request.getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}