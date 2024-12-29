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
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/register", "/uploadMotorcycle", "/surveyMotorcycle",
                                "/resultPage", "/error", "/login",
                                "/api/**", "/static/**", "/images/**")
                        .permitAll()
                        .antMatchers("/motorcycle/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/motorcycle/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/login")
                )
                .headers(headers -> headers
                        .frameOptions().deny()
                        .xssProtection(xss -> xss.block(true))
                        .addHeaderWriter((request, response) -> {
                            response.setHeader("X-XSS-Protection", "1; mode=block");
                            response.setHeader("Content-Security-Policy",
                                    "default-src 'self'; " +
                                            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                                            "style-src 'self' 'unsafe-inline'; " +
                                            "img-src 'self' data: https:; " +
                                            "font-src 'self' https:; " +
                                            "connect-src 'self'"
                            );
                        })
                        .contentTypeOptions().and()
                        .addHeaderWriter((request, response) ->
                                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin")
                        )
                        .permissionsPolicy().policy("camera=(), microphone=(), geolocation=(), payment=()")
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .userDetailsService(userDetailsService);

        return http.build();
    }
}