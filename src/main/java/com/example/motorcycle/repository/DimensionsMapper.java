package com.example.motorcycle.repository;

import com.example.motorcycle.domain.Dimensions;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DimensionsMapper {

    @Select("SELECT * FROM Dimensions WHERE motorcycleID = #{motorcycleID}")
    Dimensions findByMotorcycleId(@Param("motorcycleID") Long motorcycleID);

    @Insert("INSERT INTO Dimensions (motorcycleID, dimensions, seatHeight, wheelbase, groundClearance, dryWeight, wetWeight, fuelCapacity, innerLegCurve, permittedTotalWeight) " +
            "VALUES (#{motorcycleID}, #{dimensions.dimensions}, #{dimensions.seatHeight}, #{dimensions.wheelbase}, #{dimensions.groundClearance}, #{dimensions.dryWeight}, #{dimensions.wetWeight}, #{dimensions.fuelCapacity}, #{dimensions.innerLegCurve}, #{dimensions.permittedTotalWeight})")
    void insertDimensionsData(@Param("dimensions") Dimensions dimensions);

    @Update("UPDATE Dimensions SET dimensions=#{dimensions.dimensions}, seatHeight=#{dimensions.seatHeight}, wheelbase=#{dimensions.wheelbase}, groundClearance=#{dimensions.groundClearance}, dryWeight=#{dimensions.dryWeight}, wetWeight=#{dimensions.wetWeight}, fuelCapacity=#{dimensions.fuelCapacity}, innerLegCurve=#{dimensions.innerLegCurve}, permittedTotalWeight=#{dimensions.permittedTotalWeight} WHERE motorcycleID = #{motorcycleID}")
    void updateDimensions(@Param("motorcycleID") Long motorcycleID, @Param("dimensions") Dimensions dimensions);

    @Delete("DELETE FROM Dimensions WHERE motorcycleID = #{motorcycleID}")
    void deleteDimensions(@Param("motorcycleID") Long motorcycleID);
}

