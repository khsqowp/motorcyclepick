package com.example.motorcycle.repository;

import com.example.motorcycle.domain.UserDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDomain findById(String id);

    List<UserDomain> findAll();

    void insertUser(UserDomain userDomain);

    void updateUser(UserDomain userDomain);

    void deleteUser(String id);

    // findByUserId 메서드 제거 (findByUsername으로 대체)
}