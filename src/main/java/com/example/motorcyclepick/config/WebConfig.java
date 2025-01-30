// 웹 애플리케이션의 MVC 설정을 담당하는 패키지
package com.example.motorcyclepick.config;

// 필요한 스프링 프레임워크 컴포넌트들을 임포트

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// 스프링의 설정 클래스임을 나타내는 어노테이션
@Configuration
// 스프링 MVC의 설정을 커스터마이즈하기 위한 인터페이스 구현
public class WebConfig implements WebMvcConfigurer {
    // application.properties 또는 application.yml에서 images.path 값을 주입
    @Value("${images.path}")
    private String imagesPath;

    // 컨트롤러의 메서드 인자 리졸버를 추가하는 메서드 오버라이드
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 스프링 시큐리티의 현재 인증된 사용자 정보를 주입하기 위한 리졸버 추가
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    // 정적 리소스 핸들러를 설정하는 메서드 오버라이드
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // URL 패턴과 리소스 위치를 매핑
        registry.addResourceHandler(
                        "/static/**",    // 모든 정적 리소스에 대한 URL 패턴
                        "/css/**",       // CSS 파일에 대한 URL 패턴
                        "/js/**",        // JavaScript 파일에 대한 URL 패턴
                        "/images/**")    // 이미지 파일에 대한 URL 패턴
                .addResourceLocations(
                        "classpath:/static/",           // 클래스패스의 static 디렉토리
                        "classpath:/static/css/",       // CSS 파일 위치
                        "classpath:/static/js/",        // JavaScript 파일 위치
                        "file:" + imagesPath + "/");    // 외부 이미지 파일 위치
    }
}