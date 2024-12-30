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
                        .antMatchers("/", "/register", "/uploadMotorcycle", "/surveyMotorcycle",
                                "/resultPage", "/error", "/login", "/dictionary",
                                "/api/**", "/static/**", "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        .antMatchers("/motorcycle/delete").hasRole("ADMIN")
                        .antMatchers("/motorcycle/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
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
                        .invalidSessionUrl("/")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/")
                        .and()
                        .sessionFixation().newSession()
                        .sessionAuthenticationErrorUrl("/")
                        .sessionAuthenticationFailureHandler((request, response, exception) ->
                                response.sendRedirect("/"))
                        .enableSessionUrlRewriting(false)
                        .sessionFixation().migrateSession()
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
                                            "connect-src 'self'" +
                                            "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                            "font-src 'self' https://fonts.gstatic.com;"
                            );
                        })
                        .contentTypeOptions().and()
                        .addHeaderWriter((request, response) ->
                                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin")
                        )
                        .permissionsPolicy().policy("camera=(), microphone=(), geolocation=(), payment=()")
                )
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/motorcycle/delete", "/api/dictionary/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .userDetailsService(userDetailsService);

        return http.build();
    }
}