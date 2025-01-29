package com.example.motorcyclepick.form;

import com.example.motorcyclepick.dto.MotorcycleDTO;
import lombok.Data;

@Data
public class

MotorcycleForm {
    private Long motorcycleID;
    private String maker;  // maker 필드 추가 (brand와 동일한 의미로 사용)


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
    private Float scooter;
    private Float classic;
    private Float cafeRacer;
    private Float price;

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
    private String classGrade;

    public String getMaker() {
        return this.brand;  // brand 값을 반환
    }

    public void setMaker(String maker) {
        this.brand = maker;  // brand 필드에 저장
    }

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
        dto.setScooter(this.scooter);
        dto.setClassic(this.classic);
        dto.setCafeRacer(this.cafeRacer);
        dto.setPrice(this.price);

        dto.setWheelbase(this.wheelbase);
        dto.setSeatHeight(this.seatHeight);
        dto.setWetWeight(this.wetWeight);
        dto.setFuelCapacity(this.fuelCapacity);
        dto.setGroundClearance(this.groundClearance);
        dto.setDryWeight(this.dryWeight);
        dto.setLength(this.length);
        dto.setWidth(this.width);
        dto.setHeight(this.height);
        dto.setFrontTyreWidth(this.frontTyreWidth);
        dto.setFrontTyreAspectRatio(this.frontTyreAspectRatio);
        dto.setFrontTyreStructure(this.frontTyreStructure);
        dto.setFrontTyreDiameter(this.frontTyreDiameter);
        dto.setRearTyreWidth(this.rearTyreWidth);
        dto.setRearTyreAspectRatio(this.rearTyreAspectRatio);
        dto.setRearTyreStructure(this.rearTyreStructure);
        dto.setRearTyreDiameter(this.rearTyreDiameter);
        dto.setFrontBrakeDiscCount(this.frontBrakeDiscCount);
        dto.setFrontBrakeDiscSize(this.frontBrakeDiscSize);
        dto.setFrontBrakeDiscType(this.frontBrakeDiscType);
        dto.setFrontBrakePistonCount(this.frontBrakePistonCount);
        dto.setRearBrakeDiscSize(this.rearBrakeDiscSize);
        dto.setRearBrakeDiscType(this.rearBrakeDiscType);
        dto.setRearBrakePistonCount(this.rearBrakePistonCount);
        dto.setFrameType(this.frameType);
        dto.setFrameMaterial(this.frameMaterial);

        dto.setStartSystem(this.startSystem);
        dto.setAbs(this.abs);
        dto.setTcs(this.tcs);
        dto.setCruiseControl(this.cruiseControl);
        dto.setAssistSlipperClutch(this.assistSlipperClutch);
        dto.setElectricScreen(this.electricScreen);
        dto.setClutchAssistSystem(this.clutchAssistSystem);
        dto.setImu(this.imu);
        dto.setCorneringAbs(this.corneringAbs);
        dto.setWheelieControl(this.wheelieControl);
        dto.setRidingModeChange(this.ridingModeChange);
        dto.setBankingAngleDisplay(this.bankingAngleDisplay);
        dto.setAbsLevelControl(this.absLevelControl);
        dto.setQuickshiftUp(this.quickshiftUp);
        dto.setQuickshiftUpDown(this.quickshiftUpDown);

        dto.setCapacity(this.capacity);
        dto.setBoreStroke(this.boreStroke);
        dto.setCompressionRatio(this.compressionRatio);
        dto.setCoolingSystem(this.coolingSystem);
        dto.setLubrication(this.lubrication);
        dto.setFuelSystem(this.fuelSystem);
        dto.setEmission(this.emission);
        dto.setInduction(this.induction);
        dto.setMileage(this.mileage);
        dto.setTopSpeed(this.topSpeed);
        dto.setClutch(this.clutch);
        dto.setTransmissionGearCount(this.transmissionGearCount);
        dto.setTransmissionType(this.transmissionType);
        dto.setEngineStroke(this.engineStroke);
        dto.setEngineCylinder(this.engineCylinder);
        dto.setEngineCamshaft(this.engineCamshaft);
        dto.setEngineType(this.engineType);
        dto.setEngineCrankAngle(this.engineCrankAngle);
        dto.setMaxPowerHp(this.maxPowerHp);
        dto.setMaxPowerRpm(this.maxPowerRpm);
        dto.setMaxTorqueNm(this.maxTorqueNm);
        dto.setMaxTorqueRpm(this.maxTorqueRpm);
        dto.setClassGrade(this.classGrade);


//        DimensionsDTO dimensionsDTO = new DimensionsDTO();
//        dimensionsDTO.setMotorcycleID(this.getMotorcycleID());
//        dto.setDimensionsDTO(dimensionsDTO);
//        
//
//        ElectronicsDTO electronicsDTO = new ElectronicsDTO();
//        electronicsDTO.setMotorcycleID(this.getMotorcycleID());
//        dto.setElectronicsDTO(electronicsDTO);
//
//        
//
//        EnginesDTO enginesDTO = new EnginesDTO();
//        enginesDTO.setMotorcycleID(this.getMotorcycleID());
//        dto.setEnginesDTO(enginesDTO);
//        dto.setMotorcycleID(this.getMotorcycleID());
//        

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
        form.setScooter(dto.getScooter());
        form.setClassic(dto.getClassic());
        form.setCafeRacer(dto.getCafeRacer());
        form.setPrice(dto.getPrice());

        // Dimensions 정보 설정
        form.setWheelbase(dto.getWheelbase());
        form.setSeatHeight(dto.getSeatHeight());
        form.setWetWeight(dto.getWetWeight());
        form.setFuelCapacity(dto.getFuelCapacity());
        form.setGroundClearance(dto.getGroundClearance());
        form.setDryWeight(dto.getDryWeight());
        form.setLength(dto.getLength());
        form.setWidth(dto.getWidth());
        form.setHeight(dto.getHeight());
        form.setFrontTyreWidth(dto.getFrontTyreWidth());
        form.setFrontTyreAspectRatio(dto.getFrontTyreAspectRatio());
        form.setFrontTyreStructure(dto.getFrontTyreStructure());
        form.setFrontTyreDiameter(dto.getFrontTyreDiameter());
        form.setRearTyreWidth(dto.getRearTyreWidth());
        form.setRearTyreAspectRatio(dto.getRearTyreAspectRatio());
        form.setRearTyreStructure(dto.getRearTyreStructure());
        form.setRearTyreDiameter(dto.getRearTyreDiameter());
        form.setFrontBrakeDiscCount(dto.getFrontBrakeDiscCount());
        form.setFrontBrakeDiscSize(dto.getFrontBrakeDiscSize());
        form.setFrontBrakeDiscType(dto.getFrontBrakeDiscType());
        form.setFrontBrakePistonCount(dto.getFrontBrakePistonCount());
        form.setRearBrakeDiscSize(dto.getRearBrakeDiscSize());
        form.setRearBrakeDiscType(dto.getRearBrakeDiscType());
        form.setRearBrakePistonCount(dto.getRearBrakePistonCount());
        form.setFrameType(dto.getFrameType());
        form.setFrameMaterial(dto.getFrameMaterial());

        // Electronics 정보 설정
        form.setStartSystem(dto.getStartSystem());
        form.setAbs(dto.getAbs());
        form.setTcs(dto.getTcs());
        form.setCruiseControl(dto.getCruiseControl());
        form.setAssistSlipperClutch(dto.getAssistSlipperClutch());
        form.setElectricScreen(dto.getElectricScreen());
        form.setClutchAssistSystem(dto.getClutchAssistSystem());
        form.setImu(dto.getImu());
        form.setCorneringAbs(dto.getCorneringAbs());
        form.setWheelieControl(dto.getWheelieControl());
        form.setRidingModeChange(dto.getRidingModeChange());
        form.setBankingAngleDisplay(dto.getBankingAngleDisplay());
        form.setAbsLevelControl(dto.getAbsLevelControl());
        form.setQuickshiftUp(dto.getQuickshiftUp());
        form.setQuickshiftUpDown(dto.getQuickshiftUpDown());

        // Engines 정보 설정
        form.setCapacity(dto.getCapacity());
        form.setBoreStroke(dto.getBoreStroke());
        form.setCompressionRatio(dto.getCompressionRatio());
        form.setCoolingSystem(dto.getCoolingSystem());
        form.setLubrication(dto.getLubrication());
        form.setFuelSystem(dto.getFuelSystem());
        form.setEmission(dto.getEmission());
        form.setInduction(dto.getInduction());
        form.setMileage(dto.getMileage());
        form.setTopSpeed(dto.getTopSpeed());
        form.setClutch(dto.getClutch());
        form.setTransmissionGearCount(dto.getTransmissionGearCount());
        form.setTransmissionType(dto.getTransmissionType());
        form.setEngineStroke(dto.getEngineStroke());
        form.setEngineCylinder(dto.getEngineCylinder());
        form.setEngineCamshaft(dto.getEngineCamshaft());
        form.setEngineType(dto.getEngineType());
        form.setEngineCrankAngle(dto.getEngineCrankAngle());
        form.setMaxPowerHp(dto.getMaxPowerHp());
        form.setMaxPowerRpm(dto.getMaxPowerRpm());
        form.setMaxTorqueNm(dto.getMaxTorqueNm());
        form.setMaxTorqueRpm(dto.getMaxTorqueRpm());
        form.setClassGrade(dto.getClassGrade());

        return form;
    }

}
