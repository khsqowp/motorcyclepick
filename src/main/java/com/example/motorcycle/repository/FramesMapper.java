package com.example.motorcycle.repository;

import com.example.motorcycle.domain.FramesDomain;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
public interface FramesMapper {
    @Select("SELECT * FROM Frames WHERE motorcycleID = #{motorcycleID}")
    FramesDomain findByMotorcycleId(@Param("motorcycleID") Long motorcycleID);

    @Insert("INSERT INTO Frames (motorcycleID, frame, frontSuspension, rearSuspension, frontWheelTravel, rearWheelTravel, frontBrakes, rearBrakes, absSystem, frontWheel, rearWheel, frontTyre, rearTyre, wheels, abs, absPro, rake, trail, frontRim, rearRim, castor, steeringAngle, steeringHeadAngle) " +
            "VALUES (#{motorcycleID}, #{frames.frame}, #{frames.frontSuspension}, #{frames.rearSuspension}, #{frames.frontWheelTravel}, #{frames.rearWheelTravel}, #{frames.frontBrakes}, #{frames.rearBrakes}, #{frames.absSystem}, #{frames.frontWheel}, #{frames.rearWheel}, #{frames.frontTyre}, #{frames.rearTyre}, #{frames.wheels}, #{frames.abs}, #{frames.absPro}, #{frames.rake}, #{frames.trail}, #{frames.frontRim}, #{frames.rearRim}, #{frames.castor}, #{frames.steeringAngle}, #{frames.steeringHeadAngle})")
    void insertFramesData(@Param("frames") FramesDomain frames);

    @Update("UPDATE Frames SET frame=#{frames.frame}, frontSuspension=#{frames.frontSuspension}, rearSuspension=#{frames.rearSuspension}, frontWheelTravel=#{frames.frontWheelTravel}, rearWheelTravel=#{frames.rearWheelTravel}, frontBrakes=#{frames.frontBrakes}, rearBrakes=#{frames.rearBrakes}, absSystem=#{frames.absSystem}, frontWheel=#{frames.frontWheel}, rearWheel=#{frames.rearWheel}, frontTyre=#{frames.frontTyre}, rearTyre=#{frames.rearTyre}, wheels=#{frames.wheels}, abs=#{frames.abs}, absPro=#{frames.absPro}, rake=#{frames.rake}, trail=#{frames.trail}, frontRim=#{frames.frontRim}, rearRim=#{frames.rearRim}, castor=#{frames.castor}, steeringAngle=#{frames.steeringAngle}, steeringHeadAngle=#{frames.steeringHeadAngle} WHERE motorcycleID=#{motorcycleID}")
    void updateFrames(@Param("motorcycleID") Long motorcycleID, @Param("frames") FramesDomain frames);

    @Delete("DELETE FROM Frames WHERE motorcycleID = #{motorcycleID}")
    void deleteFrames(@Param("motorcycleID") Long motorcycleID);
}
