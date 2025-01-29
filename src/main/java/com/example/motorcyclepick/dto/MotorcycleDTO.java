package com.example.motorcyclepick.dto;

import com.example.motorcyclepick.domain.MotorcycleDomain;
import lombok.Data;

import java.io.Serializable;

@Data
public class MotorcycleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long motorcycleID;

    private String brand;
    private String model;
    private Float years;
    private Float replica;
    private Float cruiser;
    private Float tourer;
    private Float adventure;
    private Float multiPurpose;
    private Float naked;
    private Float scrambler;
    private Float offRoad;
    private Float motard;
    private Float scooter;
    private Float classic;
    private Float cafeRacer;
    private Float price;

    private Float wheelbase;
    private Float seatHeight;
    private Float wetWeight;
    private Float fuelCapacity;
    private Float groundClearance;
    private Float dryWeight;
    private Float length;
    private Float width;
    private Float height;
    private Float frontTyreWidth;
    private Float frontTyreAspectRatio;
    private String frontTyreStructure;
    private Float frontTyreDiameter;
    private Float rearTyreWidth;
    private Float rearTyreAspectRatio;
    private String rearTyreStructure;
    private Float rearTyreDiameter;
    private String frontBrakeDiscCount;
    private Float frontBrakeDiscSize;
    private String frontBrakeDiscType;
    private Float frontBrakePistonCount;
    private Float rearBrakeDiscSize;
    private String rearBrakeDiscType;
    private Float rearBrakePistonCount;
    private String frameType;
    private String frameMaterial;

    private String startSystem;
    private Boolean abs;
    private Boolean tcs;
    private Boolean cruiseControl;
    private Boolean assistSlipperClutch;
    private Boolean electricScreen;
    private Boolean clutchAssistSystem;
    private Boolean imu;
    private Boolean corneringAbs;
    private Boolean wheelieControl;
    private Boolean ridingModeChange;
    private Boolean bankingAngleDisplay;
    private Boolean absLevelControl;
    private Boolean quickshiftUp;
    private Boolean quickshiftUpDown;
    private DimensionsDTO dimensionsDTO;
    private ElectronicsDTO electronicsDTO;
    private EnginesDTO enginesDTO;

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


    // fromDomain 메서드 추가
    public static MotorcycleDTO fromDomain(MotorcycleDomain motorcycle) {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        MotorcycleDTO dto = new MotorcycleDTO();
        dto.setMotorcycleID(motorcycle.getMotorcycleID());
        dto.setBrand(motorcycle.getBrand());
        dto.setModel(motorcycle.getModel());
        dto.setYears(motorcycle.getYears());
        dto.setReplica(motorcycle.getReplica());
        dto.setCruiser(motorcycle.getCruiser());
        dto.setTourer(motorcycle.getTourer());
        dto.setAdventure(motorcycle.getAdventure());
        dto.setMultiPurpose(motorcycle.getMultiPurpose());
        dto.setNaked(motorcycle.getNaked());
        dto.setScrambler(motorcycle.getScrambler());
        dto.setOffRoad(motorcycle.getOffRoad());
        dto.setMotard(motorcycle.getMotard());
        dto.setScooter(motorcycle.getScooter());
        dto.setClassic(motorcycle.getClassic());
        dto.setCafeRacer(motorcycle.getCafeRacer());
        dto.setPrice(motorcycle.getPrice());

// Dimensions
        dto.setWheelbase(motorcycle.getWheelbase());
        dto.setSeatHeight(motorcycle.getSeatHeight());
        dto.setWetWeight(motorcycle.getWetWeight());
        dto.setFuelCapacity(motorcycle.getFuelCapacity());
        dto.setGroundClearance(motorcycle.getGroundClearance());
        dto.setDryWeight(motorcycle.getDryWeight());
        dto.setLength(motorcycle.getLength());
        dto.setWidth(motorcycle.getWidth());
        dto.setHeight(motorcycle.getHeight());
        dto.setFrontTyreWidth(motorcycle.getFrontTyreWidth());
        dto.setFrontTyreAspectRatio(motorcycle.getFrontTyreAspectRatio());
        dto.setFrontTyreStructure(motorcycle.getFrontTyreStructure());
        dto.setFrontTyreDiameter(motorcycle.getFrontTyreDiameter());
        dto.setRearTyreWidth(motorcycle.getRearTyreWidth());
        dto.setRearTyreAspectRatio(motorcycle.getRearTyreAspectRatio());
        dto.setRearTyreStructure(motorcycle.getRearTyreStructure());
        dto.setRearTyreDiameter(motorcycle.getRearTyreDiameter());
        dto.setFrontBrakeDiscCount(motorcycle.getFrontBrakeDiscCount());
        dto.setFrontBrakeDiscSize(motorcycle.getFrontBrakeDiscSize());
        dto.setFrontBrakeDiscType(motorcycle.getFrontBrakeDiscType());
        dto.setFrontBrakePistonCount(motorcycle.getFrontBrakePistonCount());
        dto.setRearBrakeDiscSize(motorcycle.getRearBrakeDiscSize());
        dto.setRearBrakeDiscType(motorcycle.getRearBrakeDiscType());
        dto.setRearBrakePistonCount(motorcycle.getRearBrakePistonCount());
        dto.setFrameType(motorcycle.getFrameType());
        dto.setFrameMaterial(motorcycle.getFrameMaterial());

// Features
        dto.setStartSystem(motorcycle.getStartSystem());
        dto.setAbs(motorcycle.getAbs());
        dto.setTcs(motorcycle.getTcs());
        dto.setCruiseControl(motorcycle.getCruiseControl());
        dto.setAssistSlipperClutch(motorcycle.getAssistSlipperClutch());
        dto.setElectricScreen(motorcycle.getElectricScreen());
        dto.setClutchAssistSystem(motorcycle.getClutchAssistSystem());
        dto.setImu(motorcycle.getImu());
        dto.setCorneringAbs(motorcycle.getCorneringAbs());
        dto.setWheelieControl(motorcycle.getWheelieControl());
        dto.setRidingModeChange(motorcycle.getRidingModeChange());
        dto.setBankingAngleDisplay(motorcycle.getBankingAngleDisplay());
        dto.setAbsLevelControl(motorcycle.getAbsLevelControl());
        dto.setQuickshiftUp(motorcycle.getQuickshiftUp());
        dto.setQuickshiftUpDown(motorcycle.getQuickshiftUpDown());

// Engine specifications
        dto.setCapacity(motorcycle.getCapacity());
        dto.setBoreStroke(motorcycle.getBoreStroke());
        dto.setCompressionRatio(motorcycle.getCompressionRatio());
        dto.setCoolingSystem(motorcycle.getCoolingSystem());
        dto.setLubrication(motorcycle.getLubrication());
        dto.setFuelSystem(motorcycle.getFuelSystem());
        dto.setEmission(motorcycle.getEmission());
        dto.setInduction(motorcycle.getInduction());
        dto.setMileage(motorcycle.getMileage());
        dto.setTopSpeed(motorcycle.getTopSpeed());
        dto.setClutch(motorcycle.getClutch());
        dto.setTransmissionGearCount(motorcycle.getTransmissionGearCount());
        dto.setTransmissionType(motorcycle.getTransmissionType());
        dto.setEngineStroke(motorcycle.getEngineStroke());
        dto.setEngineCylinder(motorcycle.getEngineCylinder());
        dto.setEngineCamshaft(motorcycle.getEngineCamshaft());
        dto.setEngineType(motorcycle.getEngineType());
        dto.setEngineCrankAngle(motorcycle.getEngineCrankAngle());
        dto.setMaxPowerHp(motorcycle.getMaxPowerHp());
        dto.setMaxPowerRpm(motorcycle.getMaxPowerRpm());
        dto.setMaxTorqueNm(motorcycle.getMaxTorqueNm());
        dto.setMaxTorqueRpm(motorcycle.getMaxTorqueRpm());
        dto.setClassGrade(motorcycle.getClassGrade());

//        // 각 도메인이 null이더라도 빈 DTO 객체 생성
//        dto.setDimensionsDTO(motorcycle.getDimensionsDomain() != null ?
//                DimensionsDTO.fromDomain(motorcycle.getDimensionsDomain()) :
//                new DimensionsDTO());
//
//        dto.setElectronicsDTO(motorcycle.getElectronicsDomain() != null ?
//                ElectronicsDTO.fromDomain(motorcycle.getElectronicsDomain()) :
//                new ElectronicsDTO());
//
//        dto.setEnginesDTO(motorcycle.getEnginesDomain() != null ?
//                EnginesDTO.fromDomain(motorcycle.getEnginesDomain()) :
//                new EnginesDTO());

        return dto;
    }

    public MotorcycleDomain toDomain() {
        MotorcycleDomain motorcycle = new MotorcycleDomain();
        motorcycle.setMotorcycleID(this.motorcycleID);
        motorcycle.setBrand(this.brand);
        motorcycle.setModel(this.model);
        motorcycle.setYears(this.years);
        motorcycle.setReplica(this.replica);
        motorcycle.setCruiser(this.cruiser);
        motorcycle.setTourer(this.tourer);
        motorcycle.setAdventure(this.adventure);
        motorcycle.setMultiPurpose(this.multiPurpose);
        motorcycle.setNaked(this.naked);
        motorcycle.setScrambler(this.scrambler);
        motorcycle.setOffRoad(this.offRoad);
        motorcycle.setMotard(this.motard);
        motorcycle.setScooter(this.scooter);
        motorcycle.setClassic(this.classic);
        motorcycle.setCafeRacer(this.cafeRacer);
        motorcycle.setPrice(this.price);

// Dimensions
        motorcycle.setWheelbase(this.wheelbase);
        motorcycle.setSeatHeight(this.seatHeight);
        motorcycle.setWetWeight(this.wetWeight);
        motorcycle.setFuelCapacity(this.fuelCapacity);
        motorcycle.setGroundClearance(this.groundClearance);
        motorcycle.setDryWeight(this.dryWeight);
        motorcycle.setLength(this.length);
        motorcycle.setWidth(this.width);
        motorcycle.setHeight(this.height);
        motorcycle.setFrontTyreWidth(this.frontTyreWidth);
        motorcycle.setFrontTyreAspectRatio(this.frontTyreAspectRatio);
        motorcycle.setFrontTyreStructure(this.frontTyreStructure);
        motorcycle.setFrontTyreDiameter(this.frontTyreDiameter);
        motorcycle.setRearTyreWidth(this.rearTyreWidth);
        motorcycle.setRearTyreAspectRatio(this.rearTyreAspectRatio);
        motorcycle.setRearTyreStructure(this.rearTyreStructure);
        motorcycle.setRearTyreDiameter(this.rearTyreDiameter);
        motorcycle.setFrontBrakeDiscCount(this.frontBrakeDiscCount);
        motorcycle.setFrontBrakeDiscSize(this.frontBrakeDiscSize);
        motorcycle.setFrontBrakeDiscType(this.frontBrakeDiscType);
        motorcycle.setFrontBrakePistonCount(this.frontBrakePistonCount);
        motorcycle.setRearBrakeDiscSize(this.rearBrakeDiscSize);
        motorcycle.setRearBrakeDiscType(this.rearBrakeDiscType);
        motorcycle.setRearBrakePistonCount(this.rearBrakePistonCount);
        motorcycle.setFrameType(this.frameType);
        motorcycle.setFrameMaterial(this.frameMaterial);

// Features
        motorcycle.setStartSystem(this.startSystem);
        motorcycle.setAbs(this.abs);
        motorcycle.setTcs(this.tcs);
        motorcycle.setCruiseControl(this.cruiseControl);
        motorcycle.setAssistSlipperClutch(this.assistSlipperClutch);
        motorcycle.setElectricScreen(this.electricScreen);
        motorcycle.setClutchAssistSystem(this.clutchAssistSystem);
        motorcycle.setImu(this.imu);
        motorcycle.setCorneringAbs(this.corneringAbs);
        motorcycle.setWheelieControl(this.wheelieControl);
        motorcycle.setRidingModeChange(this.ridingModeChange);
        motorcycle.setBankingAngleDisplay(this.bankingAngleDisplay);
        motorcycle.setAbsLevelControl(this.absLevelControl);
        motorcycle.setQuickshiftUp(this.quickshiftUp);
        motorcycle.setQuickshiftUpDown(this.quickshiftUpDown);

// Engine specifications
        motorcycle.setCapacity(this.capacity);
        motorcycle.setBoreStroke(this.boreStroke);
        motorcycle.setCompressionRatio(this.compressionRatio);
        motorcycle.setCoolingSystem(this.coolingSystem);
        motorcycle.setLubrication(this.lubrication);
        motorcycle.setFuelSystem(this.fuelSystem);
        motorcycle.setEmission(this.emission);
        motorcycle.setInduction(this.induction);
        motorcycle.setMileage(this.mileage);
        motorcycle.setTopSpeed(this.topSpeed);
        motorcycle.setClutch(this.clutch);
        motorcycle.setTransmissionGearCount(this.transmissionGearCount);
        motorcycle.setTransmissionType(this.transmissionType);
        motorcycle.setEngineStroke(this.engineStroke);
        motorcycle.setEngineCylinder(this.engineCylinder);
        motorcycle.setEngineCamshaft(this.engineCamshaft);
        motorcycle.setEngineType(this.engineType);
        motorcycle.setEngineCrankAngle(this.engineCrankAngle);
        motorcycle.setMaxPowerHp(this.maxPowerHp);
        motorcycle.setMaxPowerRpm(this.maxPowerRpm);
        motorcycle.setMaxTorqueNm(this.maxTorqueNm);
        motorcycle.setMaxTorqueRpm(this.maxTorqueRpm);
        motorcycle.setClassGrade(this.classGrade);

//        // 서브 엔티티를 각 도메인 객체로 변환
//        if (this.dimensionsDTO != null) {
//            motorcycle.setDimensionsDomain(this.dimensionsDTO.toDomain());
//        }
//        if (this.electronicsDTO != null) {
//            motorcycle.setElectronicsDomain(this.electronicsDTO.toDomain());
//        }
//        if (this.enginesDTO != null) {
//            motorcycle.setEnginesDomain(this.enginesDTO.toDomain());
//        }

        return motorcycle;
    }
}
