package com.example.motorcycle.repository;

import com.example.motorcycle.domain.TransmissionsDomain;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TransmissionsMapper {
    @Select("SELECT * FROM Transmissions WHERE motorcycleID = #{motorcycleID}")
    TransmissionsDomain findByMotorcycleId(@Param("motorcycleID") Long motorcycleID);

    @Insert("INSERT INTO Transmissions (motorcycleID, transmission_drive, transmission, final_drive, primary_drive_ratio, primary_ratio, gear_ratio, transmission_ratio, secondary_ratio, gear_ratios, clutch) " +
            "VALUES (#{motorcycleID}, #{transmissions.transmission_drive}, #{transmissions.transmission}, #{transmissions.final_drive}, #{transmissions.primary_drive_ratio}, #{transmissions.primary_ratio}, #{transmissions.gear_ratio}, #{transmissions.transmission_ratio}, #{transmissions.secondary_ratio}, #{transmissions.gear_ratios}, #{transmissions.clutch})")
    void insertTransmissionsData(@Param("transmissions") TransmissionsDomain transmissions);

    @Update("UPDATE Transmissions SET transmission_drive=#{transmissions.transmission_drive}, transmission=#{transmissions.transmission}, final_drive=#{transmissions.final_drive}, primary_drive_ratio=#{transmissions.primary_drive_ratio}, primary_ratio=#{transmissions.primary_ratio}, gear_ratio=#{transmissions.gear_ratio}, transmission_ratio=#{transmissions.transmission_ratio}, secondary_ratio=#{transmissions.secondary_ratio}, gear_ratios=#{transmissions.gear_ratios}, clutch=#{transmissions.clutch} WHERE motorcycleID=#{motorcycleID}")
    void updateTransmissions(@Param("motorcycleID") Long motorcycleID, @Param("transmissions") TransmissionsDomain transmissions);

    @Delete("DELETE FROM Transmissions WHERE motorcycleID = #{motorcycleID}")
    void deleteTransmissions(@Param("motorcycleID") Long motorcycleID);
}
