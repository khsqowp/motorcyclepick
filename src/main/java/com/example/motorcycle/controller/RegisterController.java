package com.example.motorcycle.controller;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.domain.UserDomain;
import com.example.motorcycle.dto.UserDTO;
import com.example.motorcycle.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegisterController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogger securityLogger;

    public RegisterController(UserService userService, PasswordEncoder passwordEncoder, SecurityLogger securityLogger) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityLogger = securityLogger;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserDTO userDTO, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            securityLogger.logSecurityEvent(
                    "REGISTER_VALIDATION_FAILURE",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            return "register";
        }


        // 비밀번호 암호화
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // 기본 역할 설정
        userDTO.setRole("ROLE_USER");

        userService.registerUser(userDTO);

        try {
            userService.registerUser(userDTO);
            return "redirect:/login";
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "REGISTER_ERROR",
                    userDTO.getId(),
                    request.getRemoteAddr()
            );
            return "register";
        }

    }
}