package com.example.motorcycle.repository;

import com.example.motorcycle.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
    void insertFramesData(FramesDomain framesDomain);
    void insertTransmissionsData(TransmissionsDomain transmissionsDomain);

    //수정
    void updateMotorcycle(MotorcycleDomain motorcycleDomain);
    void updateDimensions(@Param("motorcycleID") Long MotorcycleID,
                          @Param("dimensions") DimensionsDomain dimensionsDomain);
    void updateElectronics(@Param("motorcycleID") Long MotorcycleID,
                           @Param("electronics") ElectronicsDomain electronicsDomain);
    void updateEngines(@Param("motorcycleID") Long motorcycleID,
                       @Param("engines") EnginesDomain enginesDomain);
    void updateFrames(@Param("motorcycleID") Long motorcycleID,
                      @Param("frames") FramesDomain framesDomain);
    void updateTransmissions(@Param("motorcycleID") Long motorcycleID,
                             @Param("transmissions") TransmissionsDomain transmissionsDomain);

    //삭제
    void deleteMotorcycle(Long motorcycleID);
    void deleteDimensions(Long motorcycleID);
    void deleteElectronics(Long motorcycleID);
    void deleteEngines(Long motorcycleID);
    void deleteFrames(Long motorcycleID);
    void deleteTransmissions(Long motorcycleID);
}

//@Mapper
//public interface MotorcycleMapper {
//    // Motorcycle 단일 조회 (모든 관련 테이블 포함)
//    @Select("SELECT m.*, " +
//            // Dimensions 필드
//            "d.dimensions, d.seatHeight, d.wheelbase, d.groundClearance, d.dryWeight, d.wetWeight, d.fuelCapacity, d.innerLegCurve, d.permittedTotalWeight, " +
//            // Electronics 필드
//            "e.engineManagement, e.emissionControl, e.engineControl, e.alternator, e.battery, e.headlight, e.ignition AS engineIgnition, e.starting, e.tractionControl, " +
//            // Engines 필드
//            "en.engine, en.capacity, en.bore_stroke, en.compression_ratio, en.cooling_system, en.lubrication, en.max_power, en.max_torque, en.fuel_system, en.exhaust, en.engine_oil, en.mixture_control, en.emission, en.induction, " +
//            // Frames 필드
//            "f.frame, f.frontSuspension, f.rearSuspension, f.frontWheelTravel, f.rearWheelTravel, f.frontBrakes, f.rearBrakes, f.absSystem, f.frontWheel, f.rearWheel, f.frontTyre, f.rearTyre, f.wheels, f.abs, f.absPro, f.rake, f.trail, f.frontRim, f.rearRim, f.castor, f.steeringAngle, f.steeringHeadAngle, " +
//            // Transmissions 필드
//            "t.transmission_drive, t.transmission, t.final_drive, t.primary_drive_ratio, t.primary_ratio, t.gear_ratio, t.transmission_ratio, t.secondary_ratio, t.gear_ratios, t.clutch " +
//            "FROM Motorcycle m " +
//            "LEFT JOIN Dimensions d ON m.motorcycleID = d.motorcycleID " +
//            "LEFT JOIN Electronics e ON m.motorcycleID = e.motorcycleID " +
//            "LEFT JOIN Engines en ON m.motorcycleID = en.motorcycleID " +
//            "LEFT JOIN Frames f ON m.motorcycleID = f.motorcycleID " +
//            "LEFT JOIN Transmissions t ON m.motorcycleID = t.motorcycleID " +
//            "WHERE m.motorcycleID = #{motorcycleID}")
//
//    @Results(id = "motorcycleResultMap", value = {
//            @Result(property = "motorcycleID", column = "motorcycleID"),
//            @Result(property = "brand", column = "brand"),
//            @Result(property = "model", column = "model"),
//            @Result(property = "years", column = "years"),
//            @Result(property = "production", column = "production"),
//            @Result(property = "replica", column = "replica"),
//            @Result(property = "cruiser", column = "cruiser"),
//            @Result(property = "tourer", column = "tourer"),
//            @Result(property = "adventure", column = "adventure"),
//            @Result(property = "multiPurpose", column = "multiPurpose"),
//            @Result(property = "naked", column = "naked"),
//            @Result(property = "cafeRacer", column = "cafeRacer"),
//            @Result(property = "scrambler", column = "scrambler"),
//            @Result(property = "offRoad", column = "offRoad"),
//            @Result(property = "motard", column = "motard"),
//            @Result(property = "trial", column = "trial"),
//            @Result(property = "scooter", column = "scooter"),
//            @Result(property = "classic", column = "classic"),
//            // Dimensions 필드 매핑
//            @Result(property = "dimensions.dimensions", column = "dimensions"),
//            @Result(property = "dimensions.seatHeight", column = "seatHeight"),
//            @Result(property = "dimensions.wheelbase", column = "wheelbase"),
//            @Result(property = "dimensions.groundClearance", column = "groundClearance"),
//            @Result(property = "dimensions.dryWeight", column = "dryWeight"),
//            @Result(property = "dimensions.wetWeight", column = "wetWeight"),
//            @Result(property = "dimensions.fuelCapacity", column = "fuelCapacity"),
//            @Result(property = "dimensions.innerLegCurve", column = "innerLegCurve"),
//            @Result(property = "dimensions.permittedTotalWeight", column = "permittedTotalWeight"),
//            // Electronics 필드 매핑
//            @Result(property = "electronics.engineManagement", column = "engineManagement"),
//            @Result(property = "electronics.emissionControl", column = "emissionControl"),
//            @Result(property = "electronics.engineControl", column = "engineControl"),
//            @Result(property = "electronics.alternator", column = "alternator"),
//            @Result(property = "electronics.battery", column = "battery"),
//            @Result(property = "electronics.headlight", column = "headlight"),
//            @Result(property = "electronics.ignition", column = "engineIgnition"),
//            @Result(property = "electronics.starting", column = "starting"),
//            @Result(property = "electronics.tractionControl", column = "tractionControl"),
//            // Engines 필드 매핑
//            @Result(property = "engines.engine", column = "engine"),
//            @Result(property = "engines.capacity", column = "capacity"),
//            @Result(property = "engines.boreStroke", column = "bore_stroke"),
//            @Result(property = "engines.compressionRatio", column = "compression_ratio"),
//            @Result(property = "engines.coolingSystem", column = "cooling_system"),
//            @Result(property = "engines.lubrication", column = "lubrication"),
//            @Result(property = "engines.maxPower", column = "max_power"),
//            @Result(property = "engines.maxTorque", column = "max_torque"),
//            @Result(property = "engines.fuelSystem", column = "fuel_system"),
//            @Result(property = "engines.exhaust", column = "exhaust"),
//            @Result(property = "engines.engineOil", column = "engine_oil"),
//            @Result(property = "engines.mixtureControl", column = "mixture_control"),
//            @Result(property = "engines.emission", column = "emission"),
//            @Result(property = "engines.induction", column = "induction"),
//            // Frames 필드 매핑
//            @Result(property = "frames.frame", column = "frame"),
//            @Result(property = "frames.frontSuspension", column = "frontSuspension"),
//            @Result(property = "frames.rearSuspension", column = "rearSuspension"),
//            @Result(property = "frames.frontWheelTravel", column = "frontWheelTravel"),
//            @Result(property = "frames.rearWheelTravel", column = "rearWheelTravel"),
//            @Result(property = "frames.frontBrakes", column = "frontBrakes"),
//            @Result(property = "frames.rearBrakes", column = "rearBrakes"),
//            @Result(property = "frames.absSystem", column = "absSystem"),
//            @Result(property = "frames.frontWheel", column = "frontWheel"),
//            @Result(property = "frames.rearWheel", column = "rearWheel"),
//            @Result(property = "frames.frontTyre", column = "frontTyre"),
//            @Result(property = "frames.rearTyre", column = "rearTyre"),
//            @Result(property = "frames.wheels", column = "wheels"),
//            @Result(property = "frames.abs", column = "abs"),
//            @Result(property = "frames.absPro", column = "absPro"),
//            @Result(property = "frames.rake", column = "rake"),
//            @Result(property = "frames.trail", column = "trail"),
//            @Result(property = "frames.frontRim", column = "frontRim"),
//            @Result(property = "frames.rearRim", column = "rearRim"),
//            @Result(property = "frames.castor", column = "castor"),
//            @Result(property = "frames.steeringAngle", column = "steeringAngle"),
//            @Result(property = "frames.steeringHeadAngle", column = "steeringHeadAngle"),
//            // Transmissions 필드 매핑
//            @Result(property = "transmissions.transmissionDrive", column = "transmission_drive"),
//            @Result(property = "transmissions.transmission", column = "transmission"),
//            @Result(property = "transmissions.finalDrive", column = "final_drive"),
//            @Result(property = "transmissions.primaryDriveRatio", column = "primary_drive_ratio"),
//            @Result(property = "transmissions.primaryRatio", column = "primary_ratio"),
//            @Result(property = "transmissions.gearRatio", column = "gear_ratio"),
//            @Result(property = "transmissions.transmissionRatio", column = "transmission_ratio"),
//            @Result(property = "transmissions.secondaryRatio", column = "secondary_ratio"),
//            @Result(property = "transmissions.gearRatios", column = "gear_ratios"),
//            @Result(property = "transmissions.clutch", column = "clutch")
//    })
//    MotorcycleDomain findByMotorcycleId(@Param("motorcycleID") Long motorcycleID);
//
//    // Motorcycle 전체 조회 (모든 관련 테이블 포함)
//    @Select("SELECT m.*, " +
//            // Dimensions 필드
//            "d.dimensions, d.seatHeight, d.wheelbase, d.groundClearance, d.dryWeight, d.wetWeight, d.fuelCapacity, d.innerLegCurve, d.permittedTotalWeight, " +
//            // Electronics 필드
//            "e.engineManagement, e.emissionControl, e.engineControl, e.alternator, e.battery, e.headlight, e.ignition AS engineIgnition, e.starting, e.tractionControl, " +
//            // Engines 필드
//            "en.engine, en.capacity, en.bore_stroke, en.compression_ratio, en.cooling_system, en.lubrication, en.max_power, en.max_torque, en.fuel_system, en.exhaust, en.engine_oil, en.mixture_control, en.emission, en.induction, " +
//            // Frames 필드
//            "f.frame, f.frontSuspension, f.rearSuspension, f.frontWheelTravel, f.rearWheelTravel, f.frontBrakes, f.rearBrakes, f.absSystem, f.frontWheel, f.rearWheel, f.frontTyre, f.rearTyre, f.wheels, f.abs, f.absPro, f.rake, f.trail, f.frontRim, f.rearRim, f.castor, f.steeringAngle, f.steeringHeadAngle, " +
//            // Transmissions 필드
//            "t.transmission_drive, t.transmission, t.final_drive, t.primary_drive_ratio, t.primary_ratio, t.gear_ratio, t.transmission_ratio, t.secondary_ratio, t.gear_ratios, t.clutch " +
//            "FROM Motorcycle m " +
//            "LEFT JOIN Dimensions d ON m.motorcycleID = d.motorcycleID " +
//            "LEFT JOIN Electronics e ON m.motorcycleID = e.motorcycleID " +
//            "LEFT JOIN Engines en ON m.motorcycleID = en.motorcycleID " +
//            "LEFT JOIN Frames f ON m.motorcycleID = f.motorcycleID " +
//            "LEFT JOIN Transmissions t ON m.motorcycleID = t.motorcycleID")
//    @ResultMap("motorcycleResultMap")
//    List<MotorcycleDomain> findAll();
//
//    @Insert("INSERT INTO Motorcycle (brand, model, years, production, replica, cruiser, tourer, adventure, multiPurpose, naked, cafeRacer, scrambler, offRoad, motard, trial, scooter, classic) VALUES (#{motorcycle.brand}, #{motorcycle.model}, #{motorcycle.years}, #{motorcycle.production}, #{motorcycle.replica}, #{motorcycle.cruiser}, #{motorcycle.tourer}, #{motorcycle.adventure}, #{motorcycle.multiPurpose}, #{motorcycle.naked}, #{motorcycle.cafeRacer}, #{motorcycle.scrambler}, #{motorcycle.offRoad}, #{motorcycle.motard}, #{motorcycle.trial}, #{motorcycle.scooter}, #{motorcycle.classic})")
//    @Options(useGeneratedKeys = true, keyProperty = "motorcycleID")
//    void insertMotorcycleData(@Param("motorcycle") MotorcycleDomain motorcycle);
//
//    @Insert("INSERT INTO Dimensions (motorcycleID, dimensions, seatHeight, wheelbase, groundClearance, dryWeight, wetWeight, fuelCapacity, innerLegCurve, permittedTotalWeight) " +
//            "VALUES (#{motorcycleID}, #{dimensions.dimensions}, #{dimensions.seatHeight}, #{dimensions.wheelbase}, #{dimensions.groundClearance}, #{dimensions.dryWeight}, #{dimensions.wetWeight}, #{dimensions.fuelCapacity}, #{dimensions.innerLegCurve}, #{dimensions.permittedTotalWeight})")
//    void insertDimensionsData(@Param("dimensions") DimensionsDomain dimensions);
//
//    @Insert("INSERT INTO Electronics (motorcycleID, engineManagement, emissionControl, engineControl, alternator, battery, headlight, ignition, starting, tractionControl) VALUES (#{motorcycleID()}, #{electronics.engineManagement}, #{electronics.emissionControl}, #{electronics.engineControl}, #{electronics.alternator}, #{electronics.battery}, #{electronics.headlight}, #{electronics.ignition}, #{electronics.starting}, #{electronics.tractionControl})")
//    void insertElectronicsData(@Param("electronics") ElectronicsDomain electronics);
//
//    @Insert("INSERT INTO Engines (motorcycleID, engine, capacity, bore_stroke, compression_ratio, cooling_system, lubrication, max_power, max_torque, fuel_system, exhaust, engine_oil, mixture_control, emission, induction) " +
//            "VALUES (#{motorcycleID}, #{engines.engine}, #{engines.capacity}, #{engines.bore_stroke}, #{engines.compression_ratio}, #{engines.cooling_system}, #{engines.lubrication}, #{engines.max_power}, #{engines.max_torque}, #{engines.fuel_system}, #{engines.exhaust}, #{engines.engine_oil}, #{engines.mixture_control}, #{engines.emission}, #{engines.induction})")
//    void insertEnginesData(@Param("engines") EnginesDomain engines);
//
//    @Insert("INSERT INTO Frames (motorcycleID, frame, frontSuspension, rearSuspension, frontWheelTravel, rearWheelTravel, frontBrakes, rearBrakes, absSystem, frontWheel, rearWheel, frontTyre, rearTyre, wheels, abs, absPro, rake, trail, frontRim, rearRim, castor, steeringAngle, steeringHeadAngle) " +
//            "VALUES (#{motorcycleID}, #{frames.frame}, #{frames.frontSuspension}, #{frames.rearSuspension}, #{frames.frontWheelTravel}, #{frames.rearWheelTravel}, #{frames.frontBrakes}, #{frames.rearBrakes}, #{frames.absSystem}, #{frames.frontWheel}, #{frames.rearWheel}, #{frames.frontTyre}, #{frames.rearTyre}, #{frames.wheels}, #{frames.abs}, #{frames.absPro}, #{frames.rake}, #{frames.trail}, #{frames.frontRim}, #{frames.rearRim}, #{frames.castor}, #{frames.steeringAngle}, #{frames.steeringHeadAngle})")
//    void insertFramesData(@Param("frames") FramesDomain frames);
//
//    @Insert("INSERT INTO Transmissions (motorcycleID, transmission_drive, transmission, final_drive, primary_drive_ratio, primary_ratio, gear_ratio, transmission_ratio, secondary_ratio, gear_ratios, clutch) " +
//            "VALUES (#{motorcycleID}, #{transmissions.transmission_drive}, #{transmissions.transmission}, #{transmissions.final_drive}, #{transmissions.primary_drive_ratio}, #{transmissions.primary_ratio}, #{transmissions.gear_ratio}, #{transmissions.transmission_ratio}, #{transmissions.secondary_ratio}, #{transmissions.gear_ratios}, #{transmissions.clutch})")
//    void insertTransmissionsData(@Param("transmissions") TransmissionsDomain transmissions);
//
//
//    @Update("UPDATE Motorcycle SET brand=#{motorcycle.brand}, model=#{motorcycle.model}, years=#{motorcycle.years}, production=#{motorcycle.production}, replica=#{motorcycle.replica}, cruiser=#{motorcycle.cruiser}, tourer=#{motorcycle.tourer}, adventure=#{motorcycle.adventure}, multiPurpose=#{motorcycle.multiPurpose}, naked=#{motorcycle.naked}, cafeRacer=#{motorcycle.cafeRacer}, scrambler=#{motorcycle.scrambler}, offRoad=#{motorcycle.offRoad}, motard=#{motorcycle.motard}, trial=#{motorcycle.trial}, scooter=#{motorcycle.scooter}, classic=#{motorcycle.classic} WHERE motorcycleID=#{motorcycle.motorcycleID}")
//    void updateMotorcycle(@Param("motorcycle") MotorcycleDomain motorcycle);
//
//    @Update("UPDATE Dimensions SET dimensions=#{dimensions.dimensions}, seatHeight=#{dimensions.seatHeight}, wheelbase=#{dimensions.wheelbase}, groundClearance=#{dimensions.groundClearance}, dryWeight=#{dimensions.dryWeight}, wetWeight=#{dimensions.wetWeight}, fuelCapacity=#{dimensions.fuelCapacity}, innerLegCurve=#{dimensions.innerLegCurve}, permittedTotalWeight=#{dimensions.permittedTotalWeight} WHERE motorcycleID = #{motorcycleID}")
//    void updateDimensions(@Param("motorcycleID") Long motorcycleID, @Param("dimensions") DimensionsDomain dimensions);
//
//    @Update("UPDATE Electronics SET engineManagement=#{electronics.engineManagement}, emissionControl=#{electronics.emissionControl}, engineControl=#{electronics.engineControl}, alternator=#{electronics.alternator}, battery=#{electronics.battery}, headlight=#{electronics.headlight}, ignition=#{electronics.ignition}, starting=#{electronics.starting}, tractionControl=#{electronics.tractionControl} WHERE motorcycleID=#{motorcycleID}")
//    void updateElectronics(@Param("motorcycleID") Long motorcycleID, @Param("electronics") ElectronicsDomain electronics);
//
//    @Update("UPDATE Engines SET engine=#{engines.engine}, capacity=#{engines.capacity}, bore_stroke=#{engines.bore_stroke}, compression_ratio=#{engines.compression_ratio}, cooling_system=#{engines.cooling_system}, lubrication=#{engines.lubrication}, max_power=#{engines.max_power}, max_torque=#{engines.max_torque}, fuel_system=#{engines.fuel_system}, exhaust=#{engines.exhaust}, engine_oil=#{engines.engine_oil}, mixture_control=#{engines.mixture_control}, emission=#{engines.emission}, induction=#{engines.induction} WHERE motorcycleID=#{motorcycleID}")
//    void updateEngines(@Param("motorcycleID") Long motorcycleID, @Param("engines") EnginesDomain engines);
//
//    @Update("UPDATE Frames SET frame=#{frames.frame}, frontSuspension=#{frames.frontSuspension}, rearSuspension=#{frames.rearSuspension}, frontWheelTravel=#{frames.frontWheelTravel}, rearWheelTravel=#{frames.rearWheelTravel}, frontBrakes=#{frames.frontBrakes}, rearBrakes=#{frames.rearBrakes}, absSystem=#{frames.absSystem}, frontWheel=#{frames.frontWheel}, rearWheel=#{frames.rearWheel}, frontTyre=#{frames.frontTyre}, rearTyre=#{frames.rearTyre}, wheels=#{frames.wheels}, abs=#{frames.abs}, absPro=#{frames.absPro}, rake=#{frames.rake}, trail=#{frames.trail}, frontRim=#{frames.frontRim}, rearRim=#{frames.rearRim}, castor=#{frames.castor}, steeringAngle=#{frames.steeringAngle}, steeringHeadAngle=#{frames.steeringHeadAngle} WHERE motorcycleID=#{motorcycleID}")
//    void updateFrames(@Param("motorcycleID") Long motorcycleID, @Param("frames") FramesDomain frames);
//
//    @Update("UPDATE Transmissions SET transmission_drive=#{transmissions.transmission_drive}, transmission=#{transmissions.transmission}, final_drive=#{transmissions.final_drive}, primary_drive_ratio=#{transmissions.primary_drive_ratio}, primary_ratio=#{transmissions.primary_ratio}, gear_ratio=#{transmissions.gear_ratio}, transmission_ratio=#{transmissions.transmission_ratio}, secondary_ratio=#{transmissions.secondary_ratio}, gear_ratios=#{transmissions.gear_ratios}, clutch=#{transmissions.clutch} WHERE motorcycleID=#{motorcycleID}")
//    void updateTransmissions(@Param("motorcycleID") Long motorcycleID, @Param("transmissions") TransmissionsDomain transmissions);
//
//
//    @Delete("DELETE FROM Motorcycle WHERE motorcycleID = #{motorcycleID}")
//    void deleteMotorcycle(@Param("motorcycleID") Long motorcycleID);
//
//    @Delete("DELETE FROM Dimensions WHERE motorcycleID = #{motorcycleID}")
//    void deleteDimensions(@Param("motorcycleID") Long motorcycleID);
//
//    @Delete("DELETE FROM Electronics WHERE motorcycleID = #{motorcycleID}")
//    void deleteElectronics(@Param("motorcycleID") Long motorcycleID);
//
//    @Delete("DELETE FROM Engines WHERE motorcycleID = #{motorcycleID}")
//    void deleteEngines(@Param("motorcycleID") Long motorcycleID);
//
//    @Delete("DELETE FROM Frames WHERE motorcycleID = #{motorcycleID}")
//    void deleteFrames(@Param("motorcycleID") Long motorcycleID);
//
//    @Delete("DELETE FROM Transmissions WHERE motorcycleID = #{motorcycleID}")
//    void deleteTransmissions(@Param("motorcycleID") Long motorcycleID);
//
//}
