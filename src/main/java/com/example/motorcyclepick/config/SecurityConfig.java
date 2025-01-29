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

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Lazy UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringAntMatchers(
                                "/motorcycle/delete",
                                "/api/dictionary/**",
                                "/send-verification",
                                "/verify-code",
                                "/api/models/**",
                                "/loginProc")
                        .csrfTokenRepository(csrfTokenRepository()))
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(
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
                        .antMatchers("/motorcycle/delete").hasRole("ADMIN")
                        .antMatchers("/motorcycle/**").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/login")
                        .and()
                        .sessionFixation().migrateSession())
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

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }
}