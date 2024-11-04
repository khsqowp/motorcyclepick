package com.example.motorcycle.form;

import com.example.motorcycle.domain.*;
import com.example.motorcycle.dto.MotorcycleDTO;
import lombok.Data;

@Data
public class MotorcycleForm {
    private Long motorcycleID;

    // Motorcycle 기본 정보
    private String brand;
    private String model;
    private Long years;
    private String production;
    private Integer replica;
    private Integer cruiser;
    private Integer tourer;
    private Integer adventure;
    private Integer multiPurpose;
    private Integer naked;
    private Integer cafeRacer;
    private Integer scrambler;
    private Integer offRoad;
    private Integer motard;
    private Integer trial;
    private Integer scooter;
    private Integer classic;

    // Dimensions 필드
    private String dimensions;
    private Float seatHeight;
    private Float wheelbase;
    private Float groundClearance;
    private Float dryWeight;
    private Float wetWeight;
    private Float fuelCapacity;
    private String innerLegCurve;
    private String permittedTotalWeight;

    // Electronics 필드
    private String engineManagement;
    private String emissionControl;
    private String engineControl;
    private String alternator;
    private String battery;
    private String headlight;
    private String ignition;
    private String starting;
    private String tractionControl;

    // Engines 필드
    private String engine;
    private Float capacity;
    private String boreStroke;
    private String compressionRatio;
    private String coolingSystem;
    private String lubrication;
    private String maxPower;
    private String maxTorque;
    private String fuelSystem;
    private String exhaust;
    private String engineOil;
    private String mixtureControl;
    private String emission;
    private String induction;

    // Frames 필드
    private String frame;
    private String frontSuspension;
    private String rearSuspension;
    private Integer frontWheelTravel;
    private Integer rearWheelTravel;
    private String frontBrakes;
    private String rearBrakes;
    private String absSystem;
    private String frontWheel;
    private String rearWheel;
    private String frontTyre;
    private String rearTyre;
    private String wheels;
    private String abs;
    private String absPro;
    private Float rake;
    private Float trail;
    private String frontRim;
    private String rearRim;
    private String castor;
    private String steeringAngle;
    private String steeringHeadAngle;

    // Transmissions 필드
    private String transmissionDrive;
    private String transmission;
    private String finalDrive;
    private String primaryDriveRatio;
    private String primaryRatio;
    private String gearRatio;
    private String transmissionRatio;
    private String secondaryRatio;
    private String gearRatios;
    private String clutch;


    // toDTO 메서드: Form 데이터를 DTO로 변환
    public MotorcycleDTO toDTO() {
        MotorcycleDTO dto = new MotorcycleDTO();
        dto.setMotorcycleID(this.getMotorcycleID());
        dto.setBrand(this.getBrand());
        dto.setModel(this.getModel());
        dto.setYears(this.getYears());
        dto.setProduction(this.getProduction());
        dto.setReplica(this.getReplica());
        dto.setCruiser(this.getCruiser());
        dto.setTourer(this.getTourer());
        dto.setAdventure(this.getAdventure());
        dto.setMultiPurpose(this.getMultiPurpose());
        dto.setNaked(this.getNaked());
        dto.setCafeRacer(this.getCafeRacer());
        dto.setScrambler(this.getScrambler());
        dto.setOffRoad(this.getOffRoad());
        dto.setMotard(this.getMotard());
        dto.setTrial(this.getTrial());
        dto.setScooter(this.getScooter());
        dto.setClassic(this.getClassic());

        //DimensionsDTO
//        dto.getDimensionsDTO().setDimensionsID(this.dimensionsID);
        dto.getDimensionsDTO().setMotorcycleID(this.motorcycleID);
        dto.getDimensionsDTO().setDimensions(this.dimensions);
        dto.getDimensionsDTO().setSeatHeight(this.seatHeight);
        dto.getDimensionsDTO().setWheelbase(this.wheelbase);
        dto.getDimensionsDTO().setGroundClearance(this.groundClearance);
        dto.getDimensionsDTO().setDryWeight(this.dryWeight);
        dto.getDimensionsDTO().setWetWeight(this.wetWeight);
        dto.getDimensionsDTO().setFuelCapacity(this.fuelCapacity);
        dto.getDimensionsDTO().setInnerLegCurve(this.innerLegCurve);
        dto.getDimensionsDTO().setPermittedTotalWeight(this.permittedTotalWeight);

        //ElectronicsDTO
//        dto.setMotorcycleID(motorcycleID);  // 외래키 설정
        dto.getElectronicsDTO().setEngineManagement(this.engineManagement);
        dto.getElectronicsDTO().setEmissionControl(this.emissionControl);
        dto.getElectronicsDTO().setEngineControl(this.engineControl);
        dto.getElectronicsDTO().setAlternator(this.alternator);
        dto.getElectronicsDTO().setBattery(this.battery);
        dto.getElectronicsDTO().setHeadlight(this.headlight);
        dto.getElectronicsDTO().setIgnition(this.ignition);
        dto.getElectronicsDTO().setStarting(this.starting);
        dto.getElectronicsDTO().setTractionControl(this.tractionControl);

        //EnginesDTO
        dto.getEnginesDTO().setMotorcycleID(this.getMotorcycleID());
        dto.getEnginesDTO().setEngine(this.getEngine());
        dto.getEnginesDTO().setCapacity(this.getCapacity());
        dto.getEnginesDTO().setBoreStroke(this.getBoreStroke());
        dto.getEnginesDTO().setCompressionRatio(this.getCompressionRatio());
        dto.getEnginesDTO().setCoolingSystem(this.getCoolingSystem());
        dto.getEnginesDTO().setLubrication(this.getLubrication());
        dto.getEnginesDTO().setMaxPower(this.getMaxPower());
        dto.getEnginesDTO().setMaxTorque(this.getMaxTorque());
        dto.getEnginesDTO().setFuelSystem(this.getFuelSystem());
        dto.getEnginesDTO().setExhaust(this.getExhaust());
        dto.getEnginesDTO().setEngineOil(this.getEngineOil());
        dto.getEnginesDTO().setMixtureControl(this.getMixtureControl());
        dto.getEnginesDTO().setEmission(this.getEmission());
        dto.getEnginesDTO().setInduction(this.getInduction());

        //FramesDTO
        dto.getFramesDTO().setFrame(this.getFrame());
        dto.getFramesDTO().setFrontSuspension(this.getFrontSuspension());
        dto.getFramesDTO().setRearSuspension(this.getRearSuspension());
        dto.getFramesDTO().setFrontWheelTravel(this.getFrontWheelTravel());
        dto.getFramesDTO().setRearWheelTravel(this.getRearWheelTravel());
        dto.getFramesDTO().setFrontBrakes(this.getFrontBrakes());
        dto.getFramesDTO().setRearBrakes(this.getRearBrakes());
        dto.getFramesDTO().setAbsSystem(this.getAbsSystem());
        dto.getFramesDTO().setFrontWheel(this.getFrontWheel());
        dto.getFramesDTO().setRearWheel(this.getRearWheel());
        dto.getFramesDTO().setFrontTyre(this.getFrontTyre());
        dto.getFramesDTO().setRearTyre(this.getRearTyre());
        dto.getFramesDTO().setWheels(this.getWheels());
        dto.getFramesDTO().setAbs(this.getAbs());
        dto.getFramesDTO().setAbsPro(this.getAbsPro());
        dto.getFramesDTO().setRake(this.getRake());
        dto.getFramesDTO().setTrail(this.getTrail());
        dto.getFramesDTO().setFrontRim(this.getFrontRim());
        dto.getFramesDTO().setRearRim(this.getRearRim());
        dto.getFramesDTO().setCastor(this.getCastor());
        dto.getFramesDTO().setSteeringAngle(this.getSteeringAngle());
        dto.getFramesDTO().setSteeringHeadAngle(this.getSteeringHeadAngle());

        //TransmissionsDTO
        dto.getTransmissionsDTO().setTransmissionDrive(this.getTransmissionDrive());
        dto.getTransmissionsDTO().setTransmission(this.getTransmission());
        dto.getTransmissionsDTO().setFinalDrive(this.getFinalDrive());
        dto.getTransmissionsDTO().setPrimaryDriveRatio(this.getPrimaryDriveRatio());
        dto.getTransmissionsDTO().setPrimaryRatio(this.getPrimaryRatio());
        dto.getTransmissionsDTO().setGearRatio(this.getGearRatio());
        dto.getTransmissionsDTO().setTransmissionRatio(this.getTransmissionRatio());
        dto.getTransmissionsDTO().setSecondaryRatio(this.getSecondaryRatio());
        dto.getTransmissionsDTO().setGearRatios(this.getGearRatios());
        dto.getTransmissionsDTO().setClutch(this.getClutch());

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
        form.setProduction(dto.getProduction());
        form.setReplica(dto.getReplica());
        form.setCruiser(dto.getCruiser());
        form.setTourer(dto.getTourer());
        form.setAdventure(dto.getAdventure());
        form.setMultiPurpose(dto.getMultiPurpose());
        form.setNaked(dto.getNaked());
        form.setCafeRacer(dto.getCafeRacer());
        form.setScrambler(dto.getScrambler());
        form.setOffRoad(dto.getOffRoad());
        form.setMotard(dto.getMotard());
        form.setTrial(dto.getTrial());
        form.setScooter(dto.getScooter());
        form.setClassic(dto.getClassic());

        // Dimensions 정보 설정
        form.setDimensions(dto.getDimensionsDTO().getDimensions());
        form.setSeatHeight(dto.getDimensionsDTO().getSeatHeight());
        form.setWheelbase(dto.getDimensionsDTO().getWheelbase());
        form.setGroundClearance(dto.getDimensionsDTO().getGroundClearance());
        form.setDryWeight(dto.getDimensionsDTO().getDryWeight());
        form.setWetWeight(dto.getDimensionsDTO().getWetWeight());
        form.setFuelCapacity(dto.getDimensionsDTO().getFuelCapacity());
        form.setInnerLegCurve(dto.getDimensionsDTO().getInnerLegCurve());
        form.setPermittedTotalWeight(dto.getDimensionsDTO().getPermittedTotalWeight());

        // Electronics 정보 설정
        form.setEngineManagement(dto.getElectronicsDTO().getEngineManagement());
        form.setEmissionControl(dto.getElectronicsDTO().getEmissionControl());
        form.setEngineControl(dto.getElectronicsDTO().getEngineControl());
        form.setAlternator(dto.getElectronicsDTO().getAlternator());
        form.setBattery(dto.getElectronicsDTO().getBattery());
        form.setHeadlight(dto.getElectronicsDTO().getHeadlight());
        form.setIgnition(dto.getElectronicsDTO().getIgnition());
        form.setStarting(dto.getElectronicsDTO().getStarting());
        form.setTractionControl(dto.getElectronicsDTO().getTractionControl());

        // Engines 정보 설정
        form.setEngine(dto.getEnginesDTO().getEngine());
        form.setCapacity(dto.getEnginesDTO().getCapacity());
        form.setBoreStroke(dto.getEnginesDTO().getBoreStroke());
        form.setCompressionRatio(dto.getEnginesDTO().getCompressionRatio());
        form.setCoolingSystem(dto.getEnginesDTO().getCoolingSystem());
        form.setLubrication(dto.getEnginesDTO().getLubrication());
        form.setMaxPower(dto.getEnginesDTO().getMaxPower());
        form.setMaxTorque(dto.getEnginesDTO().getMaxTorque());
        form.setFuelSystem(dto.getEnginesDTO().getFuelSystem());
        form.setExhaust(dto.getEnginesDTO().getExhaust());
        form.setEngineOil(dto.getEnginesDTO().getEngineOil());
        form.setMixtureControl(dto.getEnginesDTO().getMixtureControl());
        form.setEmission(dto.getEnginesDTO().getEmission());
        form.setInduction(dto.getEnginesDTO().getInduction());

        // Frames 정보 설정
        form.setFrame(dto.getFramesDTO().getFrame());
        form.setFrontSuspension(dto.getFramesDTO().getFrontSuspension());
        form.setRearSuspension(dto.getFramesDTO().getRearSuspension());
        form.setFrontWheelTravel(dto.getFramesDTO().getFrontWheelTravel());
        form.setRearWheelTravel(dto.getFramesDTO().getRearWheelTravel());
        form.setFrontBrakes(dto.getFramesDTO().getFrontBrakes());
        form.setRearBrakes(dto.getFramesDTO().getRearBrakes());
        form.setAbsSystem(dto.getFramesDTO().getAbsSystem());
        form.setFrontWheel(dto.getFramesDTO().getFrontWheel());
        form.setRearWheel(dto.getFramesDTO().getRearWheel());
        form.setFrontTyre(dto.getFramesDTO().getFrontTyre());
        form.setRearTyre(dto.getFramesDTO().getRearTyre());
        form.setWheels(dto.getFramesDTO().getWheels());
        form.setAbs(dto.getFramesDTO().getAbs());
        form.setAbsPro(dto.getFramesDTO().getAbsPro());
        form.setRake(dto.getFramesDTO().getRake());
        form.setTrail(dto.getFramesDTO().getTrail());
        form.setFrontRim(dto.getFramesDTO().getFrontRim());
        form.setRearRim(dto.getFramesDTO().getRearRim());
        form.setCastor(dto.getFramesDTO().getCastor());
        form.setSteeringAngle(dto.getFramesDTO().getSteeringAngle());
        form.setSteeringHeadAngle(dto.getFramesDTO().getSteeringHeadAngle());

        // Transmissions 정보 설정
        form.setTransmissionDrive(dto.getTransmissionsDTO().getTransmissionDrive());
        form.setTransmission(dto.getTransmissionsDTO().getTransmission());
        form.setFinalDrive(dto.getTransmissionsDTO().getFinalDrive());
        form.setPrimaryDriveRatio(dto.getTransmissionsDTO().getPrimaryDriveRatio());
        form.setPrimaryRatio(dto.getTransmissionsDTO().getPrimaryRatio());
        form.setGearRatio(dto.getTransmissionsDTO().getGearRatio());
        form.setTransmissionRatio(dto.getTransmissionsDTO().getTransmissionRatio());
        form.setSecondaryRatio(dto.getTransmissionsDTO().getSecondaryRatio());
        form.setGearRatios(dto.getTransmissionsDTO().getGearRatios());
        form.setClutch(dto.getTransmissionsDTO().getClutch());

        return form;
    }
}
