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

    // 각 서브 엔티티의 DTO 추가
    private Dimensions dimensions;  // 수정: ID가 아닌 전체  객체
    private Electronics electronics;  // 수정: ID가 아닌 전체  객체
    private Engines engines;  // 수정: ID가 아닌 전체  객체
    private Frames frames;  // 수정: ID가 아닌 전체  객체
//    private MotorcycleSpecs motorcycleSpecs;  // 수정: ID가 아닌 전체  객체
    private Transmissions transmissions;  // 수정: ID가 아닌 전체  객체



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
}
