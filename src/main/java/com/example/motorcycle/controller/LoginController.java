package com.example.motorcycle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
        }
        if (logout != null) {
            model.addAttribute("message", "로그아웃되었습니다.");
        }
        return "login";
    }

    @PostMapping("/loginProc")
    public String processLogin(@RequestParam String id,
                               @RequestParam String password,
                               HttpServletRequest request) {
        try {
            request.login(id, password);
            return "redirect:/";
        } catch (ServletException e) {
            return "redirect:/login?error=true";
        }
    }
}