package com.example.motorcyclepick.repository;

import com.example.motorcyclepick.domain.UserDomain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    UserDomain findById(String id);

    List<UserDomain> findAll();

    void insertUser(UserDomain userDomain);

    void updateUser(UserDomain userDomain);

    void deleteUser(String id);

    // findByUserId 메서드 제거 (findByUsername으로 대체)

    List<UserDomain> findByRegion(String region);

    List<UserDomain> findByBirthDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    void updateEncryptedFields(@Param("id") String id, @Param("encryptedEmail") String encryptedEmail, @Param("encryptedPhone") String encryptedPhone);
}