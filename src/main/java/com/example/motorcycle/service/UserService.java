package com.example.motorcycle.service;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.domain.User;
import com.example.motorcycle.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogger securityLogger;

    public UserService(UserMapper userMapper, @org.springframework.context.annotation.Lazy PasswordEncoder passwordEncoder, SecurityLogger securityLogger) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityLogger = new SecurityLogger();
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        try {
            User user = userMapper.findById(id);
            if (user == null) {
                securityLogger.logSecurityEvent("USER_NOT_FOUND",
                        id,
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
                throw new UsernameNotFoundException("User not found with id: " + id);
            }

            securityLogger.logSecurityEvent("USER_LOGIN_SUCCESS",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );

            return new org.springframework.security.core.userdetails.User(
                    user.getId(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent("USER_LOGIN_FAILURE",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            throw e;
        }
    }

    public void registerUser(User user) {
        try {
            if (userMapper.findById(user.getId()) != null) {
                securityLogger.logSecurityEvent("USER_REGISTER_DUPLICATE",
                        user.getId(),
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
                throw new RuntimeException("이미 존재하는 아이디입니다.");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("ROLE_USER");
            userMapper.insertUser(user);

            securityLogger.logSecurityEvent("USER_REGISTER_SUCCESS",
                    user.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent("USER_REGISTER_FAILURE",
                    user.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            throw e;
        }
    }
}