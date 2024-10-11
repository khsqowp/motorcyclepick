package com.example.motorcycle.repository;

import com.example.motorcycle.domain.Motorcycle;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface MotorcycleMapper {
    @Select("SELECT * FROM Motorcycle WHERE motorcycleID = #{motorcycleID}")
    Motorcycle findByMotorcycleId(@Param("motorcycleID") Long motorcycleID);

    @Insert(
            "INSERT INTO Motorcycle (brand, model, years, production, replica, cruiser, tourer, adventure, multiPurpose, naked, cafeRacer, scrambler, offRoad, motard, trial, scooter, classic) VALUES (#{motorcycle.brand}, #{motorcycle.model}, #{motorcycle.years}, #{motorcycle.production}, #{motorcycle.replica}, #{motorcycle.cruiser}, #{motorcycle.tourer}, #{motorcycle.adventure}, #{motorcycle.multiPurpose}, #{motorcycle.naked}, #{motorcycle.cafeRacer}, #{motorcycle.scrambler}, #{motorcycle.offRoad}, #{motorcycle.motard}, #{motorcycle.trial}, #{motorcycle.scooter}, #{motorcycle.classic})")
    @Options(useGeneratedKeys = true, keyProperty = "motorcycleID")
    void insertMotorcycleData(@Param("motorcycle") Motorcycle motorcycle);

    @Update("UPDATE Motorcycle SET brand=#{motorcycle.brand}, model=#{motorcycle.model}, years=#{motorcycle.years}, production=#{motorcycle.production}, replica=#{motorcycle.replica}, cruiser=#{motorcycle.cruiser}, tourer=#{motorcycle.tourer}, adventure=#{motorcycle.adventure}, multiPurpose=#{motorcycle.multiPurpose}, naked=#{motorcycle.naked}, cafeRacer=#{motorcycle.cafeRacer}, scrambler=#{motorcycle.scrambler}, offRoad=#{motorcycle.offRoad}, motard=#{motorcycle.motard}, trial=#{motorcycle.trial}, scooter=#{motorcycle.scooter}, classic=#{motorcycle.classic} WHERE motorcycleID=#{motorcycle.motorcycleID}")
    void updateMotorcycle(@Param("motorcycle") Motorcycle motorcycle);

    @Delete("DELETE FROM Motorcycle WHERE motorcycleID = #{motorcycleID}")
    void deleteMotorcycle(@Param("motorcycleID") Long motorcycleID);

    @Mapper
        @Select("SELECT * FROM Motorcycle")
    List<Motorcycle> findAll(); // 모든 Motorcycle 데이터를 조회하는 메서드 추
}
