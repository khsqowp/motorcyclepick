package com.example.motorcyclepick.repository;

import com.example.motorcyclepick.domain.MotorcycleDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MotorcycleMapper {
    // 조회
    MotorcycleDomain findByMotorcycleId(Long motorcycleID);

    List<MotorcycleDomain> findAll();

    // 삽입
    void insertMotorcycleData(MotorcycleDomain motorcycleDomain);

    // 수정
    void updateMotorcycle(MotorcycleDomain motorcycleDomain);

    // 삭제
    void deleteMotorcycle(Long motorcycleID);

    // 선호도 기반 검색 (기존 코드 유지)
    List<MotorcycleDomain> findByAllRangePreferences(Map<String, Object> params);

    List<String> findDistinctBrands();

    List<String> findModelsByBrand(@Param("brand") String brand);
}
