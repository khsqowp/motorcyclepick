package com.example.motorcycle.dto;

import com.example.motorcycle.domain.*;
import lombok.Data;

@Data
public class MotorcycleDTO {
    private Long motorcycleID;
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

    private DimensionsDTO dimensionsDTO;
    private ElectronicsDTO electronicsDTO;
    private EnginesDTO enginesDTO;
    private FramesDTO framesDTO;
    private TransmissionsDTO transmissionsDTO;


    // fromDomain 메서드 추가
    public static MotorcycleDTO fromDomain(Motorcycle motorcycle) {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        MotorcycleDTO dto = new MotorcycleDTO();
        dto.setMotorcycleID(motorcycle.getMotorcycleID());
        dto.setBrand(motorcycle.getBrand());
        dto.setModel(motorcycle.getModel());
        dto.setYears(motorcycle.getYears());
        dto.setProduction(motorcycle.getProduction());
        dto.setReplica(motorcycle.getReplica());
        dto.setCruiser(motorcycle.getCruiser());
        dto.setTourer(motorcycle.getTourer());
        dto.setAdventure(motorcycle.getAdventure());
        dto.setMultiPurpose(motorcycle.getMultiPurpose());
        dto.setNaked(motorcycle.getNaked());
        dto.setCafeRacer(motorcycle.getCafeRacer());
        dto.setScrambler(motorcycle.getScrambler());
        dto.setOffRoad(motorcycle.getOffRoad());
        dto.setMotard(motorcycle.getMotard());
        dto.setTrial(motorcycle.getTrial());
        dto.setScooter(motorcycle.getScooter());
        dto.setClassic(motorcycle.getClassic());

        // 각 서브 엔티티를 해당 DTO로 변환하여 설정
        if (motorcycle.getDimensions() != null) {
            dto.setDimensionsDTO(DimensionsDTO.fromDomain(motorcycle.getDimensions()));
        }
        if (motorcycle.getElectronics() != null) {
            dto.setElectronicsDTO(ElectronicsDTO.fromDomain(motorcycle.getElectronics()));
        }
        if (motorcycle.getEngines() != null) {
            dto.setEnginesDTO(EnginesDTO.fromDomain(motorcycle.getEngines()));
        }
        if (motorcycle.getFrames() != null) {
            dto.setFramesDTO(FramesDTO.fromDomain(motorcycle.getFrames()));
        }
//        if (motorcycle.getMotorcycleSpecs() != null) {
//            dto.setMotorcycleSpecsDTO(MotorcycleSpecsDTO.fromDomain(motorcycle.getMotorcycleSpecs()));
//        }
        if (motorcycle.getTransmissions() != null) {
            dto.setTransmissionsDTO(TransmissionsDTO.fromDomain(motorcycle.getTransmissions()));
        }

        return dto;
    }

    public Motorcycle toDomain() {
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setMotorcycleID(this.motorcycleID);
        motorcycle.setBrand(this.brand);
        motorcycle.setModel(this.model);
        motorcycle.setYears(this.years);
        motorcycle.setProduction(this.production);
        motorcycle.setReplica(this.replica);
        motorcycle.setCruiser(this.cruiser);
        motorcycle.setTourer(this.tourer);
        motorcycle.setAdventure(this.adventure);
        motorcycle.setMultiPurpose(this.multiPurpose);
        motorcycle.setNaked(this.naked);
        motorcycle.setCafeRacer(this.cafeRacer);
        motorcycle.setScrambler(this.scrambler);
        motorcycle.setOffRoad(this.offRoad);
        motorcycle.setMotard(this.motard);
        motorcycle.setTrial(this.trial);
        motorcycle.setScooter(this.scooter);
        motorcycle.setClassic(this.classic);

        // 서브 엔티티를 각 도메인 객체로 변환
        if (this.dimensionsDTO != null) {
            motorcycle.setDimensions(this.dimensionsDTO.toDomain());
        }
        if (this.electronicsDTO != null) {
            motorcycle.setElectronics(this.electronicsDTO.toDomain());
        }
        if (this.enginesDTO != null) {
            motorcycle.setEngines(this.enginesDTO.toDomain());
        }
        if (this.framesDTO != null) {
            motorcycle.setFrames(this.framesDTO.toDomain());
        }
        if (this.transmissionsDTO != null) {
            motorcycle.setTransmissions(this.transmissionsDTO.toDomain());
        }

        return motorcycle;
    }
}
