package com.example.motorcycle.form;

import com.example.motorcycle.dto.*;
import lombok.Data;

@Data
public class MotorcycleForm {
    private Long motorcycleID;

    // Motorcycle 기본 정보
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
    private Float trial;
    private Float scooter;
    private Float classic;
    private Float cafeRacer;

    // Dimensions 필드
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

    // Electronics 필드
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

    // Engines 필드
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

    // toDTO 메서드: Form 데이터를 DTO로 변환
    public MotorcycleDTO toDTO() {
        MotorcycleDTO dto = new MotorcycleDTO();

        dto.setMotorcycleID(this.motorcycleID);
        dto.setBrand(this.brand);
        dto.setModel(this.model);
        dto.setYears(this.years);
        dto.setReplica(this.replica);
        dto.setCruiser(this.cruiser);
        dto.setTourer(this.tourer);
        dto.setAdventure(this.adventure);
        dto.setMultiPurpose(this.multiPurpose);
        dto.setNaked(this.naked);
        dto.setScrambler(this.scrambler);
        dto.setOffRoad(this.offRoad);
        dto.setMotard(this.motard);
        dto.setTrial(this.trial);
        dto.setScooter(this.scooter);
        dto.setClassic(this.classic);
        dto.setCafeRacer(this.cafeRacer);

        DimensionsDTO dimensionsDTO = new DimensionsDTO();
        dimensionsDTO.setMotorcycleID(this.getMotorcycleID());
        dto.setDimensionsDTO(dimensionsDTO);
        dto.getDimensionsDTO().setMotorcycleID(this.motorcycleID);
        dto.getDimensionsDTO().setWheelbase(this.wheelbase);
        dto.getDimensionsDTO().setSeatHeight(this.seatHeight);
        dto.getDimensionsDTO().setWetWeight(this.wetWeight);
        dto.getDimensionsDTO().setFuelCapacity(this.fuelCapacity);
        dto.getDimensionsDTO().setGroundClearance(this.groundClearance);
        dto.getDimensionsDTO().setDryWeight(this.dryWeight);
        dto.getDimensionsDTO().setLength(this.length);
        dto.getDimensionsDTO().setWidth(this.width);
        dto.getDimensionsDTO().setHeight(this.height);
        dto.getDimensionsDTO().setFrontTyreWidth(this.frontTyreWidth);
        dto.getDimensionsDTO().setFrontTyreAspectRatio(this.frontTyreAspectRatio);
        dto.getDimensionsDTO().setFrontTyreStructure(this.frontTyreStructure);
        dto.getDimensionsDTO().setFrontTyreDiameter(this.frontTyreDiameter);
        dto.getDimensionsDTO().setRearTyreWidth(this.rearTyreWidth);
        dto.getDimensionsDTO().setRearTyreAspectRatio(this.rearTyreAspectRatio);
        dto.getDimensionsDTO().setRearTyreStructure(this.rearTyreStructure);
        dto.getDimensionsDTO().setRearTyreDiameter(this.rearTyreDiameter);
        dto.getDimensionsDTO().setFrontBrakeDiscCount(this.frontBrakeDiscCount);
        dto.getDimensionsDTO().setFrontBrakeDiscSize(this.frontBrakeDiscSize);
        dto.getDimensionsDTO().setFrontBrakeDiscType(this.frontBrakeDiscType);
        dto.getDimensionsDTO().setFrontBrakePistonCount(this.frontBrakePistonCount);
        dto.getDimensionsDTO().setRearBrakeDiscSize(this.rearBrakeDiscSize);
        dto.getDimensionsDTO().setRearBrakeDiscType(this.rearBrakeDiscType);
        dto.getDimensionsDTO().setRearBrakePistonCount(this.rearBrakePistonCount);
        dto.getDimensionsDTO().setFrameType(this.frameType);
        dto.getDimensionsDTO().setFrameMaterial(this.frameMaterial);

        ElectronicsDTO electronicsDTO = new ElectronicsDTO();
        electronicsDTO.setMotorcycleID(this.getMotorcycleID());
        dto.setElectronicsDTO(electronicsDTO);

        dto.getElectronicsDTO().setStartSystem(this.startSystem);
        dto.getElectronicsDTO().setAbs(this.abs);
        dto.getElectronicsDTO().setTcs(this.tcs);
        dto.getElectronicsDTO().setCruiseControl(this.cruiseControl);
        dto.getElectronicsDTO().setAssistSlipperClutch(this.assistSlipperClutch);
        dto.getElectronicsDTO().setElectricScreen(this.electricScreen);
        dto.getElectronicsDTO().setClutchAssistSystem(this.clutchAssistSystem);
        dto.getElectronicsDTO().setImu(this.imu);
        dto.getElectronicsDTO().setCorneringAbs(this.corneringAbs);
        dto.getElectronicsDTO().setWheelieControl(this.wheelieControl);
        dto.getElectronicsDTO().setRidingModeChange(this.ridingModeChange);
        dto.getElectronicsDTO().setBankingAngleDisplay(this.bankingAngleDisplay);
        dto.getElectronicsDTO().setAbsLevelControl(this.absLevelControl);
        dto.getElectronicsDTO().setQuickshiftUp(this.quickshiftUp);
        dto.getElectronicsDTO().setQuickshiftUpDown(this.quickshiftUpDown);

        EnginesDTO enginesDTO = new EnginesDTO();
        enginesDTO.setMotorcycleID(this.getMotorcycleID());
        dto.setEnginesDTO(enginesDTO);
        dto.getEnginesDTO().setMotorcycleID(this.getMotorcycleID());
        dto.getEnginesDTO().setCapacity(this.capacity);
        dto.getEnginesDTO().setBoreStroke(this.boreStroke);
        dto.getEnginesDTO().setCompressionRatio(this.compressionRatio);
        dto.getEnginesDTO().setCoolingSystem(this.coolingSystem);
        dto.getEnginesDTO().setLubrication(this.lubrication);
        dto.getEnginesDTO().setFuelSystem(this.fuelSystem);
        dto.getEnginesDTO().setEmission(this.emission);
        dto.getEnginesDTO().setInduction(this.induction);
        dto.getEnginesDTO().setMileage(this.mileage);
        dto.getEnginesDTO().setTopSpeed(this.topSpeed);
        dto.getEnginesDTO().setClutch(this.clutch);
        dto.getEnginesDTO().setTransmissionGearCount(this.transmissionGearCount);
        dto.getEnginesDTO().setTransmissionType(this.transmissionType);
        dto.getEnginesDTO().setEngineStroke(this.engineStroke);
        dto.getEnginesDTO().setEngineCylinder(this.engineCylinder);
        dto.getEnginesDTO().setEngineCamshaft(this.engineCamshaft);
        dto.getEnginesDTO().setEngineType(this.engineType);
        dto.getEnginesDTO().setEngineCrankAngle(this.engineCrankAngle);
        dto.getEnginesDTO().setMaxPowerHp(this.maxPowerHp);
        dto.getEnginesDTO().setMaxPowerRpm(this.maxPowerRpm);
        dto.getEnginesDTO().setMaxTorqueNm(this.maxTorqueNm);
        dto.getEnginesDTO().setMaxTorqueRpm(this.maxTorqueRpm);

        return dto;
    }

    // fromDTO 메서드: DTO 데이터를 Form으로 변환
    public static MotorcycleForm fromDTO(MotorcycleDTO dto) {
        MotorcycleForm form = new MotorcycleForm();

// Motorcycle 기본 정보 설정
        form.setMotorcycleID(dto.getMotorcycleID());
        form.setBrand(dto.getBrand());
        form.setModel(dto.getModel());
        form.setYears(dto.getYears());
        form.setReplica(dto.getReplica());
        form.setCruiser(dto.getCruiser());
        form.setTourer(dto.getTourer());
        form.setAdventure(dto.getAdventure());
        form.setMultiPurpose(dto.getMultiPurpose());
        form.setNaked(dto.getNaked());
        form.setScrambler(dto.getScrambler());
        form.setOffRoad(dto.getOffRoad());
        form.setMotard(dto.getMotard());
        form.setTrial(dto.getTrial());
        form.setScooter(dto.getScooter());
        form.setClassic(dto.getClassic());
        form.setCafeRacer(dto.getCafeRacer());

        // Dimensions 정보 설정
        form.setWheelbase(dto.getDimensionsDTO().getWheelbase());
        form.setSeatHeight(dto.getDimensionsDTO().getSeatHeight());
        form.setWetWeight(dto.getDimensionsDTO().getWetWeight());
        form.setFuelCapacity(dto.getDimensionsDTO().getFuelCapacity());
        form.setGroundClearance(dto.getDimensionsDTO().getGroundClearance());
        form.setDryWeight(dto.getDimensionsDTO().getDryWeight());
        form.setLength(dto.getDimensionsDTO().getLength());
        form.setWidth(dto.getDimensionsDTO().getWidth());
        form.setHeight(dto.getDimensionsDTO().getHeight());
        form.setFrontTyreWidth(dto.getDimensionsDTO().getFrontTyreWidth());
        form.setFrontTyreAspectRatio(dto.getDimensionsDTO().getFrontTyreAspectRatio());
        form.setFrontTyreStructure(dto.getDimensionsDTO().getFrontTyreStructure());
        form.setFrontTyreDiameter(dto.getDimensionsDTO().getFrontTyreDiameter());
        form.setRearTyreWidth(dto.getDimensionsDTO().getRearTyreWidth());
        form.setRearTyreAspectRatio(dto.getDimensionsDTO().getRearTyreAspectRatio());
        form.setRearTyreStructure(dto.getDimensionsDTO().getRearTyreStructure());
        form.setRearTyreDiameter(dto.getDimensionsDTO().getRearTyreDiameter());
        form.setFrontBrakeDiscCount(dto.getDimensionsDTO().getFrontBrakeDiscCount());
        form.setFrontBrakeDiscSize(dto.getDimensionsDTO().getFrontBrakeDiscSize());
        form.setFrontBrakeDiscType(dto.getDimensionsDTO().getFrontBrakeDiscType());
        form.setFrontBrakePistonCount(dto.getDimensionsDTO().getFrontBrakePistonCount());
        form.setRearBrakeDiscSize(dto.getDimensionsDTO().getRearBrakeDiscSize());
        form.setRearBrakeDiscType(dto.getDimensionsDTO().getRearBrakeDiscType());
        form.setRearBrakePistonCount(dto.getDimensionsDTO().getRearBrakePistonCount());
        form.setFrameType(dto.getDimensionsDTO().getFrameType());
        form.setFrameMaterial(dto.getDimensionsDTO().getFrameMaterial());

        // Electronics 정보 설정
        form.setStartSystem(dto.getElectronicsDTO().getStartSystem());
        form.setAbs(dto.getElectronicsDTO().getAbs());
        form.setTcs(dto.getElectronicsDTO().getTcs());
        form.setCruiseControl(dto.getElectronicsDTO().getCruiseControl());
        form.setAssistSlipperClutch(dto.getElectronicsDTO().getAssistSlipperClutch());
        form.setElectricScreen(dto.getElectronicsDTO().getElectricScreen());
        form.setClutchAssistSystem(dto.getElectronicsDTO().getClutchAssistSystem());
        form.setImu(dto.getElectronicsDTO().getImu());
        form.setCorneringAbs(dto.getElectronicsDTO().getCorneringAbs());
        form.setWheelieControl(dto.getElectronicsDTO().getWheelieControl());
        form.setRidingModeChange(dto.getElectronicsDTO().getRidingModeChange());
        form.setBankingAngleDisplay(dto.getElectronicsDTO().getBankingAngleDisplay());
        form.setAbsLevelControl(dto.getElectronicsDTO().getAbsLevelControl());
        form.setQuickshiftUp(dto.getElectronicsDTO().getQuickshiftUp());
        form.setQuickshiftUpDown(dto.getElectronicsDTO().getQuickshiftUpDown());

        // Engines 정보 설정
        form.setCapacity(dto.getEnginesDTO().getCapacity());
        form.setBoreStroke(dto.getEnginesDTO().getBoreStroke());
        form.setCompressionRatio(dto.getEnginesDTO().getCompressionRatio());
        form.setCoolingSystem(dto.getEnginesDTO().getCoolingSystem());
        form.setLubrication(dto.getEnginesDTO().getLubrication());
        form.setFuelSystem(dto.getEnginesDTO().getFuelSystem());
        form.setEmission(dto.getEnginesDTO().getEmission());
        form.setInduction(dto.getEnginesDTO().getInduction());
        form.setMileage(dto.getEnginesDTO().getMileage());
        form.setTopSpeed(dto.getEnginesDTO().getTopSpeed());
        form.setClutch(dto.getEnginesDTO().getClutch());
        form.setTransmissionGearCount(dto.getEnginesDTO().getTransmissionGearCount());
        form.setTransmissionType(dto.getEnginesDTO().getTransmissionType());
        form.setEngineStroke(dto.getEnginesDTO().getEngineStroke());
        form.setEngineCylinder(dto.getEnginesDTO().getEngineCylinder());
        form.setEngineCamshaft(dto.getEnginesDTO().getEngineCamshaft());
        form.setEngineType(dto.getEnginesDTO().getEngineType());
        form.setEngineCrankAngle(dto.getEnginesDTO().getEngineCrankAngle());
        form.setMaxPowerHp(dto.getEnginesDTO().getMaxPowerHp());
        form.setMaxPowerRpm(dto.getEnginesDTO().getMaxPowerRpm());
        form.setMaxTorqueNm(dto.getEnginesDTO().getMaxTorqueNm());
        form.setMaxTorqueRpm(dto.getEnginesDTO().getMaxTorqueRpm());

        return form;
    }

}
