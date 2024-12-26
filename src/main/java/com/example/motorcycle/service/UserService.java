package com.example.motorcycle.service;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.domain.User;
import com.example.motorcycle.repository.UserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

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

    public List<User> getAllUsers() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        try {
            // 접근 로깅
            securityLogger.logSecurityEvent(
                    "GET_ALL_USERS_REQUEST",
                    username,
                    remoteAddr
            );

            List<User> users = userMapper.findAll();

            // 성공 로깅
            securityLogger.logSecurityEvent(
                    "GET_ALL_USERS_SUCCESS",
                    username,
                    remoteAddr
            );

            return users;
        } catch (Exception e) {
            // 실패 로깅
            securityLogger.logSecurityEvent(
                    "GET_ALL_USERS_FAILURE",
                    username,
                    remoteAddr
            );
            throw new RuntimeException("사용자 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    public User findById(String id) {
        try {
            User user = userMapper.findById(id);
            if (user == null) {
                securityLogger.logSecurityEvent(
                        "USER_FIND_NOT_FOUND",
                        "SYSTEM",
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
            }
            return user;
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "USER_FIND_FAILURE",
                    "SYSTEM",
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            throw new RuntimeException("사용자 조회 중 오류가 발생했습니다.", e);
        }
    }

    public void updateUser(User user) {
        try {
            // 기존 사용자 정보 가져오기
            User existingUser = userMapper.findById(user.getId());
            if (existingUser == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            // 비밀번호 유지
            user.setPassword(existingUser.getPassword());

            // 사용자 정보 업데이트
            userMapper.updateUser(user);

            securityLogger.logSecurityEvent(
                    "USER_UPDATE_SUCCESS",
                    user.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "USER_UPDATE_FAILURE",
                    user.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            throw new RuntimeException("사용자 정보 수정 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteUser(String id) {
        try {
            userMapper.deleteUser(id);  // String 타입 그대로 전달
            securityLogger.logSecurityEvent(
                    "USER_DELETE_SUCCESS",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "USER_DELETE_FAILURE",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            throw new RuntimeException("사용자 삭제 중 오류가 발생했습니다.", e);
        }
    }
}