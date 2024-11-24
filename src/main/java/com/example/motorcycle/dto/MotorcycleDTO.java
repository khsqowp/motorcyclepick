package com.example.motorcycle.dto;

import com.example.motorcycle.domain.*;
import lombok.Data;

@Data
public class MotorcycleDTO {
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
    private Float trial;
    private Float scooter;
    private Float classic;
    private Float cafeRacer;
    private Float price;

    private DimensionsDTO dimensionsDTO;
    private ElectronicsDTO electronicsDTO;
    private EnginesDTO enginesDTO;


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
        dto.setTrial(motorcycle.getTrial());
        dto.setScooter(motorcycle.getScooter());
        dto.setClassic(motorcycle.getClassic());
        dto.setCafeRacer(motorcycle.getCafeRacer());
        dto.setPrice(motorcycle.getPrice());

        // 각 도메인이 null이더라도 빈 DTO 객체 생성
        dto.setDimensionsDTO(motorcycle.getDimensionsDomain() != null ?
                DimensionsDTO.fromDomain(motorcycle.getDimensionsDomain()) :
                new DimensionsDTO());

        dto.setElectronicsDTO(motorcycle.getElectronicsDomain() != null ?
                ElectronicsDTO.fromDomain(motorcycle.getElectronicsDomain()) :
                new ElectronicsDTO());

        dto.setEnginesDTO(motorcycle.getEnginesDomain() != null ?
                EnginesDTO.fromDomain(motorcycle.getEnginesDomain()) :
                new EnginesDTO());

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
        motorcycle.setTrial(this.trial);
        motorcycle.setScooter(this.scooter);
        motorcycle.setClassic(this.classic);
        motorcycle.setCafeRacer(this.cafeRacer);
        motorcycle.setPrice(this.price);

        // 서브 엔티티를 각 도메인 객체로 변환
        if (this.dimensionsDTO != null) {
            motorcycle.setDimensionsDomain(this.dimensionsDTO.toDomain());
        }
        if (this.electronicsDTO != null) {
            motorcycle.setElectronicsDomain(this.electronicsDTO.toDomain());
        }
        if (this.enginesDTO != null) {
            motorcycle.setEnginesDomain(this.enginesDTO.toDomain());
        }

        return motorcycle;
    }
}
