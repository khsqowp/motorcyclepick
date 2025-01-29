package com.example.motorcyclepick.domain;

import com.example.motorcyclepick.dto.MotorcycleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor

public class MotorcycleDomain implements Serializable {
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

    //Dimensions
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

    private String startSystem;          // varchar(255)
    private Boolean abs;                 // tinyint(1)
    private Boolean tcs;                 // tinyint(1)
    private Boolean cruiseControl;       // tinyint(1)
    private Boolean assistSlipperClutch; // tinyint(1)
    private Boolean electricScreen;    // tinyint(1)
    private Boolean clutchAssistSystem;  // tinyint(1)
    private Boolean imu;                 // tinyint(1)
    private Boolean corneringAbs;        // tinyint(1)
    private Boolean wheelieControl;      // tinyint(1)
    private Boolean ridingModeChange;    // tinyint(1)
    private Boolean bankingAngleDisplay; // tinyint(1)
    private Boolean absLevelControl;     // tinyint(1)
    private Boolean quickshiftUp;        // tinyint(1)
    private Boolean quickshiftUpDown;    // tinyint(1)

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

//    @Getter
//    private DimensionsDomain dimensionsDomain;
//    @Getter
//    private ElectronicsDomain electronicsDomain;
//    @Getter
//    private EnginesDomain enginesDomain;

    public static MotorcycleDomain fromDTO(MotorcycleDTO dto) {
        MotorcycleDomain motorcycleDomain = new MotorcycleDomain();
        motorcycleDomain.setMotorcycleID(dto.getMotorcycleID());
        motorcycleDomain.setBrand(dto.getBrand());
        motorcycleDomain.setModel(dto.getModel());
        motorcycleDomain.setYears(dto.getYears());
        motorcycleDomain.setReplica(dto.getReplica());
        motorcycleDomain.setCruiser(dto.getCruiser());
        motorcycleDomain.setTourer(dto.getTourer());
        motorcycleDomain.setAdventure(dto.getAdventure());
        motorcycleDomain.setMultiPurpose(dto.getMultiPurpose());
        motorcycleDomain.setNaked(dto.getNaked());
        motorcycleDomain.setScrambler(dto.getScrambler());
        motorcycleDomain.setOffRoad(dto.getOffRoad());
        motorcycleDomain.setMotard(dto.getMotard());
        motorcycleDomain.setScooter(dto.getScooter());
        motorcycleDomain.setClassic(dto.getClassic());
        motorcycleDomain.setCafeRacer(dto.getCafeRacer());
        motorcycleDomain.setPrice(dto.getPrice());

// Dimensions
        motorcycleDomain.setWheelbase(dto.getWheelbase());
        motorcycleDomain.setSeatHeight(dto.getSeatHeight());
        motorcycleDomain.setWetWeight(dto.getWetWeight());
        motorcycleDomain.setFuelCapacity(dto.getFuelCapacity());
        motorcycleDomain.setGroundClearance(dto.getGroundClearance());
        motorcycleDomain.setDryWeight(dto.getDryWeight());
        motorcycleDomain.setLength(dto.getLength());
        motorcycleDomain.setWidth(dto.getWidth());
        motorcycleDomain.setHeight(dto.getHeight());
        motorcycleDomain.setFrontTyreWidth(dto.getFrontTyreWidth());
        motorcycleDomain.setFrontTyreAspectRatio(dto.getFrontTyreAspectRatio());
        motorcycleDomain.setFrontTyreStructure(dto.getFrontTyreStructure());
        motorcycleDomain.setFrontTyreDiameter(dto.getFrontTyreDiameter());
        motorcycleDomain.setRearTyreWidth(dto.getRearTyreWidth());
        motorcycleDomain.setRearTyreAspectRatio(dto.getRearTyreAspectRatio());
        motorcycleDomain.setRearTyreStructure(dto.getRearTyreStructure());
        motorcycleDomain.setRearTyreDiameter(dto.getRearTyreDiameter());
        motorcycleDomain.setFrontBrakeDiscCount(dto.getFrontBrakeDiscCount());
        motorcycleDomain.setFrontBrakeDiscSize(dto.getFrontBrakeDiscSize());
        motorcycleDomain.setFrontBrakeDiscType(dto.getFrontBrakeDiscType());
        motorcycleDomain.setFrontBrakePistonCount(dto.getFrontBrakePistonCount());
        motorcycleDomain.setRearBrakeDiscSize(dto.getRearBrakeDiscSize());
        motorcycleDomain.setRearBrakeDiscType(dto.getRearBrakeDiscType());
        motorcycleDomain.setRearBrakePistonCount(dto.getRearBrakePistonCount());
        motorcycleDomain.setFrameType(dto.getFrameType());
        motorcycleDomain.setFrameMaterial(dto.getFrameMaterial());

// Features
        motorcycleDomain.setStartSystem(dto.getStartSystem());
        motorcycleDomain.setAbs(dto.getAbs());
        motorcycleDomain.setTcs(dto.getTcs());
        motorcycleDomain.setCruiseControl(dto.getCruiseControl());
        motorcycleDomain.setAssistSlipperClutch(dto.getAssistSlipperClutch());
        motorcycleDomain.setElectricScreen(dto.getElectricScreen());
        motorcycleDomain.setClutchAssistSystem(dto.getClutchAssistSystem());
        motorcycleDomain.setImu(dto.getImu());
        motorcycleDomain.setCorneringAbs(dto.getCorneringAbs());
        motorcycleDomain.setWheelieControl(dto.getWheelieControl());
        motorcycleDomain.setRidingModeChange(dto.getRidingModeChange());
        motorcycleDomain.setBankingAngleDisplay(dto.getBankingAngleDisplay());
        motorcycleDomain.setAbsLevelControl(dto.getAbsLevelControl());
        motorcycleDomain.setQuickshiftUp(dto.getQuickshiftUp());
        motorcycleDomain.setQuickshiftUpDown(dto.getQuickshiftUpDown());

// Engine specifications
        motorcycleDomain.setCapacity(dto.getCapacity());
        motorcycleDomain.setBoreStroke(dto.getBoreStroke());
        motorcycleDomain.setCompressionRatio(dto.getCompressionRatio());
        motorcycleDomain.setCoolingSystem(dto.getCoolingSystem());
        motorcycleDomain.setLubrication(dto.getLubrication());
        motorcycleDomain.setFuelSystem(dto.getFuelSystem());
        motorcycleDomain.setEmission(dto.getEmission());
        motorcycleDomain.setInduction(dto.getInduction());
        motorcycleDomain.setMileage(dto.getMileage());
        motorcycleDomain.setTopSpeed(dto.getTopSpeed());
        motorcycleDomain.setClutch(dto.getClutch());
        motorcycleDomain.setTransmissionGearCount(dto.getTransmissionGearCount());
        motorcycleDomain.setTransmissionType(dto.getTransmissionType());
        motorcycleDomain.setEngineStroke(dto.getEngineStroke());
        motorcycleDomain.setEngineCylinder(dto.getEngineCylinder());
        motorcycleDomain.setEngineCamshaft(dto.getEngineCamshaft());
        motorcycleDomain.setEngineType(dto.getEngineType());
        motorcycleDomain.setEngineCrankAngle(dto.getEngineCrankAngle());
        motorcycleDomain.setMaxPowerHp(dto.getMaxPowerHp());
        motorcycleDomain.setMaxPowerRpm(dto.getMaxPowerRpm());
        motorcycleDomain.setMaxTorqueNm(dto.getMaxTorqueNm());
        motorcycleDomain.setMaxTorqueRpm(dto.getMaxTorqueRpm());
        motorcycleDomain.setClassGrade(dto.getClassGrade());


//        motorcycleDomain.setDimensionsDomain(DimensionsDomain.fromDTO(dto.getDimensionsDTO()));
//        motorcycleDomain.setElectronicsDomain(ElectronicsDomain.fromDTO(dto.getElectronicsDTO()));
//        motorcycleDomain.setEnginesDomain(EnginesDomain.fromDTO(dto.getEnginesDTO()));
//
//        // 각 DTO가 null이더라도 빈 도메인 객체 생성
//        motorcycleDomain.setDimensionsDomain(dto.getDimensionsDTO() != null ?
//                DimensionsDomain.fromDTO(dto.getDimensionsDTO()) :
//                new DimensionsDomain());
//
//        motorcycleDomain.setElectronicsDomain(dto.getElectronicsDTO() != null ?
//                ElectronicsDomain.fromDTO(dto.getElectronicsDTO()) :
//                new ElectronicsDomain());
//
//        motorcycleDomain.setEnginesDomain(dto.getEnginesDTO() != null ?
//                EnginesDomain.fromDTO(dto.getEnginesDTO()) :
//                new EnginesDomain());

        return motorcycleDomain;
    }

}
