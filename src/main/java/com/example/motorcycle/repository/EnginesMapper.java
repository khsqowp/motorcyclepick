//package com.example.motorcycle.repository;
//
//import com.example.motorcycle.domain.EnginesDomain;
//import org.apache.ibatis.annotations.*;
//import org.springframework.stereotype.Repository;
//
//@Mapper
//public interface EnginesMapper{
//    @Select("SELECT * FROM Engines WHERE motorcycleID = #{motorcycleID}")
//    EnginesDomain findByMotorcycleId(@Param("motorcycleID") Long motorcycleID);
//
//    @Insert(
//            "INSERT INTO Engines (motorcycleID, engine, capacity, bore_stroke, compression_ratio, cooling_system, lubrication, max_power, max_torque, fuel_system, exhaust, engine_oil, mixture_control, emission, induction) " +
//                    "VALUES (#{motorcycleID}, #{engines.engine}, #{engines.capacity}, #{engines.bore_stroke}, #{engines.compression_ratio}, #{engines.cooling_system}, #{engines.lubrication}, #{engines.max_power}, #{engines.max_torque}, #{engines.fuel_system}, #{engines.exhaust}, #{engines.engine_oil}, #{engines.mixture_control}, #{engines.emission}, #{engines.induction})")
//    void insertEnginesData(@Param("engines") EnginesDomain engines);
//
//    @Update("UPDATE Engines SET engine=#{engines.engine}, capacity=#{engines.capacity}, bore_stroke=#{engines.bore_stroke}, compression_ratio=#{engines.compression_ratio}, cooling_system=#{engines.cooling_system}, lubrication=#{engines.lubrication}, max_power=#{engines.max_power}, max_torque=#{engines.max_torque}, fuel_system=#{engines.fuel_system}, exhaust=#{engines.exhaust}, engine_oil=#{engines.engine_oil}, mixture_control=#{engines.mixture_control}, emission=#{engines.emission}, induction=#{engines.induction} WHERE motorcycleID=#{motorcycleID}")
//    void updateEngines(@Param("motorcycleID") Long motorcycleID, @Param("engines") EnginesDomain engines);
//
//    @Delete("DELETE FROM Engines WHERE motorcycleID = #{motorcycleID}")
//    void deleteEngines(@Param("motorcycleID") Long motorcycleID);
//}
