package com.example.motorcyclepick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

// Spring Security 설정 클래스
@Configuration
// Spring Security 활성화
@EnableWebSecurity
public class SecurityConfig {

    // UserDetailsService 의존성 주입 (지연 로딩)
    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Lazy UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 비밀번호 암호화를 위한 인코더 빈 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 설정
                .csrf(csrf -> csrf
                        .ignoringAntMatchers( // CSRF 검증 제외 URL
                                "/motorcycle/delete",
                                "/api/dictionary/**",
                                "/send-verification",
                                "/verify-code",
                                "/api/models/**",
                                "/loginProc")
                        .csrfTokenRepository(csrfTokenRepository()))

                // URL 별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .antMatchers( // 인증 없이 접근 가능한 리소스
                                "/resources/**",
                                "/static/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/", "/register", "/check-id/*", "/uploadMotorcycle", "/surveyMotorcycle",
                                "/resultPage", "/error", "/login", "/dictionary",
                                "/api/**", "analytics/**",
                                "/send-verification", "/verify-code", "/email-verification",
                                "https://cdnjs.cloudflare.com/**",
                                "https://fonts.googleapis.com/**",
                                "https://fonts.gstatic.com/**",
                                "/gallery/**")
                        .permitAll()
                        .antMatchers("/motorcycle/delete").hasRole("ADMIN") // 관리자 전용
                        .antMatchers("/motorcycle/**").authenticated() // 인증 필요
                        .anyRequest().authenticated())

                // 로그인 설정
                .formLogin(form -> form
                        .loginPage("/login") // 로그인 페이지 경로
                        .usernameParameter("id") // 아이디 파라미터명
                        .passwordParameter("password") // 비밀번호 파라미터명
                        .loginProcessingUrl("/loginProc") // 로그인 처리 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동
                        .permitAll())

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 이동
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN") // 쿠키 삭제
                        .permitAll())

                // 세션 관리 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/")
                        .maximumSessions(1) // 최대 세션 수
                        .maxSessionsPreventsLogin(false) // 중복 로그인 허용하고 이전 세션 무효화
                        .expiredUrl("/login")
                        .and()
                        .sessionFixation().migrateSession()) // 세션 고정 보호

                // 보안 헤더 설정
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                        .xssProtection(xss -> xss.block(true))
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdnjs.cloudflare.com; " +
                                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com; " +
                                        "img-src 'self' data: * blob:; " +
                                        "font-src 'self' https://fonts.gstatic.com https://fonts.googleapis.com; " +
                                        "frame-src 'self';" +
                                        "upgrade-insecure-requests;"
                        ))
                        .contentTypeOptions()
                        .and()
                        .addHeaderWriter((request, response) -> {
                            response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                            response.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
                        }))
                .userDetailsService(userDetailsService);

        return http.build();
    }

    // CSRF 토큰 저장소 설정
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }
}