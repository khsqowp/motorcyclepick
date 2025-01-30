package com.example.motorcyclepick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// 이메일 전송을 위한 설정 클래스
@Configuration
public class MailConfig {

    // JavaMailSender 빈 설정
    @Bean
    public JavaMailSender javaMailSender() {
        // JavaMailSender 구현체 생성
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // 메일 서버 설정
        mailSender.setHost("smtp.kakao.com");        // 카카오 SMTP 서버 호스트
        mailSender.setPort(465);                     // SSL 보안 포트
        mailSender.setUsername("ryan03255@kakao.com"); // 발신자 이메일
        mailSender.setPassword("cabhckpeswjotmbo");    // 앱 비밀번호
        mailSender.setDefaultEncoding("UTF-8");        // 기본 인코딩 설정

        // 추가 속성 설정
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");     // 전송 프로토콜 설정
        props.put("mail.smtp.auth", "true");             // SMTP 인증 사용
        props.put("mail.smtp.ssl.enable", "true");       // SSL 보안 연결 사용
        props.put("mail.smtp.ssl.trust", "smtp.kakao.com"); // SSL 신뢰할 호스트
        props.put("mail.debug", "true");                 // 디버그 모드 활성화

        return mailSender;
    }
}