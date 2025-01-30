// SecurityService.java
package com.example.motorcyclepick.service;

import com.example.motorcyclepick.exception.DataNotFoundException;
import com.example.motorcyclepick.exception.UserAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.regex.Pattern;

@Service
public class SecurityService {
    private final PasswordEncoder passwordEncoder;
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );

    public SecurityService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isPasswordValid(String password) {
        if (password == null) {
            throw new UserAuthenticationException("비밀번호가 null입니다");
        }
        if (password.trim().isEmpty()) {
            throw new UserAuthenticationException("비밀번호가 비어있습니다");
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public String sanitizeInput(String input) {
        if (input == null) {
            throw new DataNotFoundException("입력값이 null입니다");
        }
        if (input.trim().isEmpty()) {
            throw new DataNotFoundException("입력값이 비어있습니다");
        }
        // XSS 방지를 위한 문자 치환
        return input.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("&", "&amp;");
    }
}