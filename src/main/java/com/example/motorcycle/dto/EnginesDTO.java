package com.example.motorcycle.dto;

import com.example.motorcycle.domain.EnginesDomain;
import lombok.Data;

@Data
public class EnginesDTO {
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

    public EnginesDomain toDomain() {
        EnginesDomain engines = new EnginesDomain();
        engines.setEnginesID(this.getEnginesID());
        engines.setMotorcycleID(this.getMotorcycleID());
        engines.setCapacity(this.getCapacity());
        engines.setBoreStroke(this.getBoreStroke());
        engines.setCompressionRatio(this.getCompressionRatio());
        engines.setCoolingSystem(this.getCoolingSystem());
        engines.setLubrication(this.getLubrication());
        engines.setFuelSystem(this.getFuelSystem());
        engines.setEmission(this.getEmission());
        engines.setInduction(this.getInduction());
        engines.setMileage(this.getMileage());
        engines.setTopSpeed(this.getTopSpeed());
        engines.setClutch(this.getClutch());
        engines.setTransmissionGearCount(this.getTransmissionGearCount());
        engines.setTransmissionType(this.getTransmissionType());
        engines.setEngineStroke(this.getEngineStroke());
        engines.setEngineCylinder(this.getEngineCylinder());
        engines.setEngineCamshaft(this.getEngineCamshaft());
        engines.setEngineType(this.getEngineType());
        engines.setEngineCrankAngle(this.getEngineCrankAngle());
        engines.setMaxPowerHp(this.getMaxPowerHp());
        engines.setMaxPowerRpm(this.getMaxPowerRpm());
        engines.setMaxTorqueNm(this.getMaxTorqueNm());
        engines.setMaxTorqueRpm(this.getMaxTorqueRpm());
        return engines;
    }

    public static EnginesDTO fromDomain(EnginesDomain engines) {
        EnginesDTO dto = new EnginesDTO();
        dto.setEnginesID(engines.getEnginesID());
        dto.setMotorcycleID(engines.getMotorcycleID());
        dto.setCapacity(engines.getCapacity());
        dto.setBoreStroke(engines.getBoreStroke());
        dto.setCompressionRatio(engines.getCompressionRatio());
        dto.setCoolingSystem(engines.getCoolingSystem());
        dto.setLubrication(engines.getLubrication());
        dto.setFuelSystem(engines.getFuelSystem());
        dto.setEmission(engines.getEmission());
        dto.setInduction(engines.getInduction());
        dto.setMileage(engines.getMileage());
        dto.setTopSpeed(engines.getTopSpeed());
        dto.setClutch(engines.getClutch());
        dto.setTransmissionGearCount(engines.getTransmissionGearCount());
        dto.setTransmissionType(engines.getTransmissionType());
        dto.setEngineStroke(engines.getEngineStroke());
        dto.setEngineCylinder(engines.getEngineCylinder());
        dto.setEngineCamshaft(engines.getEngineCamshaft());
        dto.setEngineType(engines.getEngineType());
        dto.setEngineCrankAngle(engines.getEngineCrankAngle());
        dto.setMaxPowerHp(engines.getMaxPowerHp());
        dto.setMaxPowerRpm(engines.getMaxPowerRpm());
        dto.setMaxTorqueNm(engines.getMaxTorqueNm());
        dto.setMaxTorqueRpm(engines.getMaxTorqueRpm());
        return dto;
    }
}