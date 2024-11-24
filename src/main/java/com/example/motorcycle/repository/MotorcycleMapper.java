package com.example.motorcycle.repository;

import com.example.motorcycle.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MotorcycleMapper {
    //조회
    MotorcycleDomain findByMotorcycleId(Long motorcycleID);
    List<MotorcycleDomain> findAll();

    //삽입
    void insertMotorcycleData(MotorcycleDomain motorcycleDomain);
    void insertDimensionsData(DimensionsDomain dimensionsDomain);
    void insertElectronicsData(ElectronicsDomain electronicsDomain);
    void insertEnginesData(EnginesDomain enginesDomain);

    //수정
    void updateMotorcycle(MotorcycleDomain motorcycleDomain);
    void updateDimensions(@Param("motorcycleID") Long MotorcycleID,
                          @Param("dimensions") DimensionsDomain dimensionsDomain);
    void updateElectronics(@Param("motorcycleID") Long MotorcycleID,
                           @Param("electronics") ElectronicsDomain electronicsDomain);
    void updateEngines(@Param("motorcycleID") Long motorcycleID,
                       @Param("engines") EnginesDomain enginesDomain);

    //삭제
    void deleteMotorcycle(Long motorcycleID);
    void deleteDimensions(Long motorcycleID);
    void deleteElectronics(Long motorcycleID);
    void deleteEngines(Long motorcycleID);


    // 정확한 값 범위로 검색
    List<MotorcycleDomain> findByAllRangePreferences(Map<String, Object> params);

}