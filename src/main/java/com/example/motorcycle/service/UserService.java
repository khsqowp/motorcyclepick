package com.example.motorcycle.service;

import com.example.motorcycle.domain.User;
import com.example.motorcycle.repository.UserMapper;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // @Lazy 어노테이션이 잘못된 패키지에서 임포트됨
    public UserService(UserMapper userMapper, @org.springframework.context.annotation.Lazy PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // userId 대신 username으로 파라미터 이름 변경 (Spring Security 표준)
        User user = userMapper.findById(id);  // 메서드 이름도 일관성 있게 변경
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getId(),  // getId() 대신 getUsername() 사용
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    public void registerUser(User user) {
        // 아이디 중복 체크
        if (userMapper.findById(user.getId()) != null) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        userMapper.insertUser(user);
    }
}