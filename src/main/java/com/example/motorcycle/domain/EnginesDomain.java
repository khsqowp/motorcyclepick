package com.example.motorcycle.domain;

import com.example.motorcycle.dto.EnginesDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnginesDomain {
    private Long enginesID;
    private Long motorcycleID;
    private Float capacity;
    private String boreStroke;
    private String compressionRatio;
    private String coolingSystem;
    private String lubrication;
    private String fuelSystem;
    private String emission;
    private String induction;
    private Float mileage;
    private Float topSpeed;
    private String clutch;
    private String transmissionGearCount;
    private String transmissionType;
    private Float engineStroke;
    private Float engineCylinder;
    private String engineCamshaft;
    private String engineType;
    private String engineCrankAngle;
    private Float maxPowerHp;
    private Float maxPowerRpm;
    private Float maxTorqueNm;
    private Float maxTorqueRpm;
    private String classGrade;

    public static EnginesDomain fromDTO(EnginesDTO dto) {
        EnginesDomain enginesDomain = new EnginesDomain();
        enginesDomain.setEnginesID(dto.getEnginesID());
        enginesDomain.setMotorcycleID(dto.getMotorcycleID());
        enginesDomain.setCapacity(dto.getCapacity());
        enginesDomain.setBoreStroke(dto.getBoreStroke());
        enginesDomain.setCompressionRatio(dto.getCompressionRatio());
        enginesDomain.setCoolingSystem(dto.getCoolingSystem());
        enginesDomain.setLubrication(dto.getLubrication());
        enginesDomain.setFuelSystem(dto.getFuelSystem());
        enginesDomain.setEmission(dto.getEmission());
        enginesDomain.setInduction(dto.getInduction());
        enginesDomain.setMileage(dto.getMileage());
        enginesDomain.setTopSpeed(dto.getTopSpeed());
        enginesDomain.setClutch(dto.getClutch());
        enginesDomain.setTransmissionGearCount(dto.getTransmissionGearCount());
        enginesDomain.setTransmissionType(dto.getTransmissionType());
        enginesDomain.setEngineStroke(dto.getEngineStroke());
        enginesDomain.setEngineCylinder(dto.getEngineCylinder());
        enginesDomain.setEngineCamshaft(dto.getEngineCamshaft());
        enginesDomain.setEngineType(dto.getEngineType());
        enginesDomain.setEngineCrankAngle(dto.getEngineCrankAngle());
        enginesDomain.setMaxPowerHp(dto.getMaxPowerHp());
        enginesDomain.setMaxPowerRpm(dto.getMaxPowerRpm());
        enginesDomain.setMaxTorqueNm(dto.getMaxTorqueNm());
        enginesDomain.setMaxTorqueRpm(dto.getMaxTorqueRpm());
        enginesDomain.setClassGrade(dto.getClassGrade());
        return enginesDomain;
    }
}