package com.example.motorcyclepick.service;

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.domain.UserDomain;
import com.example.motorcyclepick.dto.UserDTO;
import com.example.motorcyclepick.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogger securityLogger;

    @Lazy
    @Autowired
    private SecurityService securityService;

    public UserService(UserMapper userMapper, @Lazy PasswordEncoder passwordEncoder, SecurityLogger securityLogger) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityLogger = securityLogger;
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
            // 입력값 검증
            validateAndSanitizeUserInput(userDTO);

            if (userMapper.findById(userDTO.getId()) != null) {
                securityLogger.logSecurityEvent("USER_REGISTER_DUPLICATE",
                        userDTO.getId(),
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr()
                );
                throw new RuntimeException("이미 존재하는 아이디입니다.");
            }

            // 비밀번호 암호화
            if (!securityService.isPasswordValid(userDTO.getPassword())) {
                throw new RuntimeException("비밀번호는 8자 이상이며, 숫자, 대문자, 소문자, 특수문자를 포함해야 합니다.");
            }
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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

    private void validateAndSanitizeUserInput(UserDTO userDTO) {
        if (userDTO.getId() == null || userDTO.getId().trim().isEmpty()) {
            throw new RuntimeException("아이디는 필수 입력값입니다.");
        }
        if (!userDTO.getId().matches("^[a-zA-Z0-9]{4,20}$")) {
            throw new RuntimeException("아이디는 4-20자의 영문자와 숫자만 허용됩니다.");
        }

        if (userDTO.getUsername() != null) {
            if (!userDTO.getUsername().matches("^[가-힣a-zA-Z]{2,20}$")) {
                throw new RuntimeException("사용자 이름은 2-20자의 한글 또는 영문만 허용됩니다.");
            }
            userDTO.setUsername(securityService.sanitizeInput(userDTO.getUsername()));
        }

        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new RuntimeException("비밀번호는 필수 입력값입니다.");
        }

        if (userDTO.getEmail() != null) {
            if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new RuntimeException("올바른 이메일 형식이 아닙니다.");
            }
            userDTO.setEmail(securityService.sanitizeInput(userDTO.getEmail()));
        }

        if (userDTO.getPhoneNumber() != null) {
            if (!userDTO.getPhoneNumber().matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) {
                throw new RuntimeException("올바른 전화번호 형식이 아닙니다.");
            }
            userDTO.setPhoneNumber(securityService.sanitizeInput(userDTO.getPhoneNumber()));
        }

        if (userDTO.getInstagram() != null) {
            if (!userDTO.getInstagram().matches("^@?[A-Za-z0-9_.]{1,30}$")) {
                throw new RuntimeException("올바른 인스타그램 아이디 형식이 아닙니다.");
            }
            userDTO.setInstagram(securityService.sanitizeInput(userDTO.getInstagram()));
        }

        if (userDTO.getRegion() != null) {
            if (!userDTO.getRegion().matches("^[가-힣a-zA-Z\\s]{2,50}$")) {
                throw new RuntimeException("올바른 지역 형식이 아닙니다.");
            }
            userDTO.setRegion(securityService.sanitizeInput(userDTO.getRegion()));
        }

        if (userDTO.getBirthDate() != null) {
            LocalDate now = LocalDate.now();
            if (userDTO.getBirthDate().isAfter(now)) {
                throw new RuntimeException("생년월일은 현재 날짜보다 이후일 수 없습니다.");
            }
        }

        // role 검증
        if (userDTO.getRole() != null) {
            if (!userDTO.getRole().matches("^ROLE_[A-Z]{1,10}$")) {
                throw new RuntimeException("올바르지 않은 역할입니다.");
            }
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
            validateAndSanitizeUserInput(userDTO);

            UserDomain existingUser = userMapper.findById(userDTO.getId());
            if (existingUser == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            // 비밀번호 처리
            if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
                userDTO.setPassword(existingUser.getPassword());
            }

            // 변경되지 않은 필드들은 기존 값 유지
            if (userDTO.getEmail() == null) userDTO.setEmail(existingUser.getEmail());
            if (userDTO.getPhoneNumber() == null) userDTO.setPhoneNumber(existingUser.getPhoneNumber());
            if (userDTO.getBirthDate() == null) userDTO.setBirthDate(existingUser.getBirthDate());

            UserDomain userDomain = userDTO.toDomain();
            userMapper.updateUser(userDomain);

        } catch (Exception e) {
            throw new RuntimeException("사용자 정보 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    public void deleteUser(String id) {
        try {
            // 권한 검증 추가
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!currentUser.equals(id) &&
                    !SecurityContextHolder.getContext().getAuthentication()
                            .getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                securityLogger.logSecurityEvent("USER_DELETE_UNAUTHORIZED",
                        id,
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                                .getRequest().getRemoteAddr()
                );
                throw new RuntimeException("해당 사용자를 삭제할 권한이 없습니다.");
            }

            if (userMapper.findById(id) == null) {
                securityLogger.logSecurityEvent("USER_DELETE_NOT_FOUND",
                        id,
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                                .getRequest().getRemoteAddr()
                );
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            userMapper.deleteUser(id);
            securityLogger.logSecurityEvent("USER_DELETE_SUCCESS",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent("USER_DELETE_FAILURE",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            throw e;
        }
    }

    public List<UserDTO> findByRegion(String region) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        try {
            securityLogger.logSecurityEvent(
                    "FIND_USERS_BY_REGION_REQUEST",
                    username,
                    remoteAddr
            );

            List<UserDomain> userDomains = userMapper.findByRegion(region);
            List<UserDTO> userDTOs = userDomains.stream()
                    .map(UserDTO::fromDomain)
                    .collect(Collectors.toList());

            securityLogger.logSecurityEvent(
                    "FIND_USERS_BY_REGION_SUCCESS",
                    username,
                    remoteAddr
            );

            return userDTOs;
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "FIND_USERS_BY_REGION_FAILURE",
                    username,
                    remoteAddr
            );
            throw new RuntimeException("지역별 사용자 조회 중 오류가 발생했습니다.", e);
        }
    }

    public List<UserDTO> findByBirthDateBetween(Date startDate, Date endDate) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        try {
            securityLogger.logSecurityEvent(
                    "FIND_USERS_BY_BIRTHDATE_REQUEST",
                    username,
                    remoteAddr
            );

            List<UserDomain> userDomains = userMapper.findByBirthDateBetween(startDate, endDate);
            List<UserDTO> userDTOs = userDomains.stream()
                    .map(UserDTO::fromDomain)
                    .collect(Collectors.toList());

            securityLogger.logSecurityEvent(
                    "FIND_USERS_BY_BIRTHDATE_SUCCESS",
                    username,
                    remoteAddr
            );

            return userDTOs;
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "FIND_USERS_BY_BIRTHDATE_FAILURE",
                    username,
                    remoteAddr
            );
            throw new RuntimeException("생년월일 범위로 사용자 조회 중 오류가 발생했습니다.", e);
        }
    }

    public void updateEncryptedFields(String id, String encryptedEmail, String encryptedPhone) {
        try {
            userMapper.updateEncryptedFields(id, encryptedEmail, encryptedPhone);
            securityLogger.logSecurityEvent(
                    "UPDATE_ENCRYPTED_FIELDS_SUCCESS",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
        } catch (Exception e) {
            securityLogger.logSecurityEvent(
                    "UPDATE_ENCRYPTED_FIELDS_FAILURE",
                    id,
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest().getRemoteAddr()
            );
            throw new RuntimeException("암호화된 필드 업데이트 중 오류가 발생했습니다.", e);
        }
    }
}