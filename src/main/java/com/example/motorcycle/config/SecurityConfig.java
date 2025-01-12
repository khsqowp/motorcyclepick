package com.example.motorcycle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    public SecurityConfig(UserDetailsService userDetailsService) {
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
                                "/loginProc")
                        .csrfTokenRepository(csrfTokenRepository()))
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(
                                "/", "/register", "/check-id/*", "/uploadMotorcycle", "/surveyMotorcycle",
                                "/resultPage", "/error", "/login", "/dictionary",
                                "/api/**", "analytics/**", "/static/**", "/css/**", "/js/**", "/images/**",
                                "/send-verification", "/verify-code", "/email-verification",
                                "https://cdnjs.cloudflare.com/**")  // CDN 접근 허용
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
                        .frameOptions().deny()
                        .xssProtection(xss -> xss.block(true))
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdnjs.cloudflare.com; " +
                                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                        "img-src 'self' data: https:; " +
                                        "font-src 'self' https://fonts.gstatic.com;"))
                        .addHeaderWriter((request, response) -> {
                            response.setHeader("X-XSS-Protection", "1; mode=block");
                            response.setHeader("Content-Security-Policy",
                                    "default-src 'self'; " +
                                            "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdnjs.cloudflare.com; " +
                                            "style-src 'self' 'unsafe-inline'; " +
                                            "img-src 'self' data: https:; " +
                                            "font-src 'self' https:; ");
                        })
                        .contentTypeOptions().and()
                        .addHeaderWriter((request, response) ->
                                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin"))
                        .permissionsPolicy().policy("camera=(), microphone=(), geolocation=(), payment=()"))
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }


}