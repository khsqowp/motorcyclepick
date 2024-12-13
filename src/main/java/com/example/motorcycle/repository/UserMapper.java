package com.example.motorcycle.repository;

import com.example.motorcycle.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User findById(String id);

    List<User> findAll();

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    // findByUserId 메서드 제거 (findByUsername으로 대체)
}