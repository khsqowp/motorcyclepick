package com.example.motorcycle.service;

import com.example.motorcycle.config.SecurityLogger;
import com.example.motorcycle.domain.UserDomain;
import com.example.motorcycle.dto.UserDTO;
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
import java.util.stream.Collectors;

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
            UserDomain userDomain = userMapper.findById(id);
            if (userDomain == null) {
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
                    userDomain.getId(),
                    userDomain.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(userDomain.getRole()))
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent("USER_LOGIN_FAILURE",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            throw e;
        }
    }

    public void registerUser(UserDTO userDTO) {
        try {
            if (userMapper.findById(userDTO.getId()) != null) {
                securityLogger.logSecurityEvent("USER_REGISTER_DUPLICATE",
                        userDTO.getId(),
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
                throw new RuntimeException("이미 존재하는 아이디입니다.");
            }

            UserDomain userDomain = userDTO.toDomain();
            userMapper.insertUser(userDomain);

            securityLogger.logSecurityEvent("USER_REGISTER_SUCCESS",
                    userDTO.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent("USER_REGISTER_FAILURE",
                    userDTO.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            throw e;
        }
    }

    public List<UserDTO> getAllUsers() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        try {
            securityLogger.logSecurityEvent(
                    "GET_ALL_USERS_REQUEST",
                    username,
                    remoteAddr
            );

            List<UserDomain> userDomains = userMapper.findAll();
            List<UserDTO> userDTOs = userDomains.stream()
                    .map(UserDTO::fromDomain)
                    .collect(Collectors.toList());

            securityLogger.logSecurityEvent(
                    "GET_ALL_USERS_SUCCESS",
                    username,
                    remoteAddr
            );

            return userDTOs;
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "GET_ALL_USERS_FAILURE",
                    username,
                    remoteAddr
            );
            throw new RuntimeException("사용자 목록 조회 중 오류가 발생했습니다.", e);
        }
    }


    public UserDTO findById(String id) {
        try {
            UserDomain userDomain = userMapper.findById(id);
            if (userDomain == null) {
                securityLogger.logSecurityEvent(
                        "USER_FIND_NOT_FOUND",
                        "SYSTEM",
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
                return null;
            }
            return UserDTO.fromDomain(userDomain);
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "USER_FIND_FAILURE",
                    "SYSTEM",
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
            );
            throw new RuntimeException("사용자 조회 중 오류가 발생했습니다.", e);
        }
    }


    public void updateUser(UserDTO userDTO) {
        try {
            UserDomain existingUserDomain = userMapper.findById(userDTO.getId());
            if (existingUserDomain == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            UserDomain userDomain = userDTO.toDomain();
            userDomain.setPassword(existingUserDomain.getPassword());

            userMapper.updateUser(userDomain);

            securityLogger.logSecurityEvent(
                    "USER_UPDATE_SUCCESS",
                    userDTO.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "USER_UPDATE_FAILURE",
                    userDTO.getId(),
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            throw new RuntimeException("사용자 정보 수정 중 오류가 발생했습니다.", e);
        }
    }


    public void deleteUser(String id) {
        try {
            userMapper.deleteUser(id);
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