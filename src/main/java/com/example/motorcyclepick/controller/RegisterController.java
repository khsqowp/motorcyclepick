package com.example.motorcyclepick.controller;

// 필요한 의존성 임포트
import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.domain.UserDomain;
import com.example.motorcyclepick.dto.UserDTO;
import com.example.motorcyclepick.exception.DataIntegrityException;
import com.example.motorcyclepick.exception.DataValidationException;
import com.example.motorcyclepick.exception.EmailVerificationException;
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

// 회원가입 관련 요청을 처리하는 컨트롤러
@Controller
@Slf4j // 로깅을 위한 어노테이션
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
public class RegisterController {
    // 이메일 인증 코드를 저장하는 동시성 해시맵
    private final Map<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();

    // 필요한 서비스 및 컴포넌트 주입
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogger securityLogger;
    private final CsrfTokenRepository csrfTokenRepository;
    private final SecurityService securityService;
    private final EmailService emailService;

    // 회원가입 폼 페이지 표시
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO,
                                      BindingResult result,
                                      HttpServletRequest request) {
        // 디버그 로깅
        log.debug("수신된 회원가입 데이터: {}", userDTO);

        // 입력값 유효성 검증
        if (result.hasErrors()) {
            log.error("유효성 검사 실패: {}", result.getAllErrors());
            securityLogger.logSecurityEvent(
                    "REGISTER_VALIDATION_FAILURE",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            throw new DataValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }

        try {
            // XSS 방지를 위한 입력값 살균
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

            // 회원 등록 수행
            try {
                userService.registerUser(userDTO);
            } catch (Exception e) {
                throw new DataIntegrityException("회원 등록 중 오류가 발생했습니다: " + e.getMessage(), e);
            }

            log.debug("회원가입 성공: {}", userDTO.getId());

            // 보안 이벤트 로깅
            securityLogger.logSecurityEvent(
                    "REGISTER_SUCCESS",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );

            return ResponseEntity.ok().build();

        } catch (DataValidationException | DataIntegrityException e) {
            // 에러 로깅
            log.error("회원가입 처리 중 오류 발생: {}", e.getMessage());
            securityLogger.logSecurityEvent(
                    "REGISTER_ERROR",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 생년월일 유효성 검증 및 기본값 설정
    private LocalDate validateAndConvertBirthDate(LocalDate birthDate) {
        return birthDate == null ? LocalDate.of(2000, 1, 1) : birthDate;
    }

    // 지역 정보 유효성 검증 및 기본값 설정
    private String validateAndSetDefaultRegion(String region) {
        return (region == null || region.trim().isEmpty()) ? "Unknown" : region;
    }

    // 전화번호 형식 정리
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }
        // 숫자만 추출
        String numbers = phoneNumber.replaceAll("[^0-9]", "");
        // 11자리면 형식에 맞게 변환
        if (numbers.length() == 11) {
            return String.format("%s-%s-%s",
                    numbers.substring(0, 3),
                    numbers.substring(3, 7),
                    numbers.substring(7));
        }
        return phoneNumber;
    }

    // 이메일 인증 코드 전송
    @PostMapping("/send-verification")
    @ResponseBody
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> payload,
                                                   HttpServletRequest request) {
        log.debug("이메일 인증 요청 수신: {}", payload.get("email"));

        try {
            String email = payload.get("email");
            if (email == null || email.isEmpty()) {
                throw new EmailVerificationException("이메일이 제공되지 않았습니다.");
            }

            // 인증 이메일 전송
            try {
                String code = emailService.sendVerificationEmail(email);
                // 인증 코드 저장
                log.debug("인증 코드 생성 완료: {}, 코드: {}", email, code);
                verificationCodes.put(email, new VerificationInfo(code, LocalDateTime.now().plusMinutes(5)));
                return ResponseEntity.ok().body("이메일이 성공적으로 전송되었습니다.");
            } catch (MailException e) {
                throw new EmailVerificationException("이메일 전송 실패", e);
            }
        } catch (EmailVerificationException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // 이메일 인증 코드 검증
    @PostMapping("/verify-code")
    @ResponseBody
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload,
                                        HttpServletRequest request) {
        // 입력값 살균
        String email = securityService.sanitizeInput(payload.get("email"));
        String code = securityService.sanitizeInput(payload.get("code"));

        // 필수값 검증
        if (email == null || code == null) {
            securityLogger.logSecurityEvent(
                    "VERIFY_CODE_INVALID_INPUT",
                    "anonymous",
                    request.getRemoteAddr()
            );
            return ResponseEntity.badRequest().body("이메일과 인증코드는 필수입니다.");
        }

        // 인증 코드 검증
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

        // 인증 실패 로깅
        securityLogger.logSecurityEvent(
                "VERIFY_CODE_FAILURE",
                email,
                request.getRemoteAddr()
        );
        return ResponseEntity.badRequest().body("잘못된 인증번호 또는 만료된 인증번호입니다.");
    }

    // 인증 정보를 저장하는 내부 클래스
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

    // 아이디 중복 체크
    @GetMapping("/check-id/{id}")
    @ResponseBody
    public ResponseEntity<?> checkIdDuplicate(@PathVariable String id,
                                              HttpServletRequest request) {
        try {
            // 타이밍 공격 방지를 위한 무작위 지연
            Thread.sleep((long) (Math.random() * 300) + 200);

            // ID 체크 요청 로깅
            securityLogger.logSecurityEvent(
                    "ID_CHECK_REQUEST",
                    id,
                    request.getRemoteAddr()
            );

            // 중복 체크 수행
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
            // 에러 로깅
            securityLogger.logSecurityEvent(
                    "ID_CHECK_ERROR",
                    id,
                    request.getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}