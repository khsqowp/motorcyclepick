package com.example.motorcyclepick.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring Configuration 클래스임을 명시하는 어노테이션
@Configuration
// Spring MVC 설정을 커스터마이즈하기 위한 WebMvcConfigurer 인터페이스 구현
public class BoardConfig implements WebMvcConfigurer {

    // CORS 설정을 위한 메서드 오버라이드
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 설정 적용
                .allowedOrigins("*")  // 모든 출처(도메인)에서의 요청 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE");  // 허용할 HTTP 메서드 지정
    }

    // View Controller 설정을 위한 메서드 오버라이드
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // /login 경로에 대해 login 뷰를 매핑
        registry.addViewController("/login").setViewName("login");
        // 추가적인 뷰 컨트롤러 설정을 위한 주석
    }
}

