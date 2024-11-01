package com.example.motorcycle.repository;

import com.example.motorcycle.domain.ElectronicsDomain;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
public interface ElectronicsMapper {
    @Select("SELECT * FROM Electronics WHERE motorcycleID = #{motorcycleID}")
    ElectronicsDomain findByMotorcycleId(@Param("motorcycleID") Long motorcycleID);

    @Insert(
            "INSERT INTO Electronics (motorcycleID, engineManagement, emissionControl, engineControl, alternator, battery, headlight, ignition, starting, tractionControl) VALUES (#{motorcycleID()}, #{electronics.engineManagement}, #{electronics.emissionControl}, #{electronics.engineControl}, #{electronics.alternator}, #{electronics.battery}, #{electronics.headlight}, #{electronics.ignition}, #{electronics.starting}, #{electronics.tractionControl})")
    void insertElectronicsData(@Param("electronics") ElectronicsDomain electronics);

    @Update("UPDATE Electronics SET engineManagement=#{electronics.engineManagement}, emissionControl=#{electronics.emissionControl}, engineControl=#{electronics.engineControl}, alternator=#{electronics.alternator}, battery=#{electronics.battery}, headlight=#{electronics.headlight}, ignition=#{electronics.ignition}, starting=#{electronics.starting}, tractionControl=#{electronics.tractionControl} WHERE motorcycleID=#{motorcycleID}")
    void updateElectronics(@Param("motorcycleID") Long motorcycleID, @Param("electronics") ElectronicsDomain electronics);

    @Delete("DELETE FROM Electronics WHERE motorcycleID = #{motorcycleID}")
    void deleteElectronics(@Param("motorcycleID") Long motorcycleID);
}
