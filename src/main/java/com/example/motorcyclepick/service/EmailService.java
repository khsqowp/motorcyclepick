package com.example.motorcyclepick.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendVerificationEmail(String to) {
        try {
            String code = generateVerificationCode();
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("ryan03255@kakao.com");
            message.setTo(to);
            message.setSubject("[오토바이 커뮤니티] 회원가입 인증번호");
            message.setText("인증번호: " + code + "\n(유효시간 5분)");

            log.debug("이메일 전송 시도 - 수신자: {}, 인증코드: {}", to, code);
            mailSender.send(message);
            log.debug("이메일 전송 성공 - 수신자: {}", to);

            return code;
        } catch (Exception e) {
            log.error("이메일 전송 실패 - 수신자: {}, 에러: {}", to, e.getMessage(), e);
            throw new MailSendException("이메일 전송 실패", e);
        }
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}