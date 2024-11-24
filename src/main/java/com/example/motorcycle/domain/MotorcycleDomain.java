package com.example.motorcycle.domain;

import com.example.motorcycle.dto.MotorcycleDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MotorcycleDomain {
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


    @Getter
    private DimensionsDomain dimensionsDomain;
    @Getter
    private ElectronicsDomain electronicsDomain;
    @Getter
    private EnginesDomain enginesDomain;

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
        motorcycleDomain.setTrial(dto.getTrial());
        motorcycleDomain.setScooter(dto.getScooter());
        motorcycleDomain.setClassic(dto.getClassic());
        motorcycleDomain.setCafeRacer(dto.getCafeRacer());
        motorcycleDomain.setPrice(dto.getPrice());


        motorcycleDomain.setDimensionsDomain(DimensionsDomain.fromDTO(dto.getDimensionsDTO()));
        motorcycleDomain.setElectronicsDomain(ElectronicsDomain.fromDTO(dto.getElectronicsDTO()));
        motorcycleDomain.setEnginesDomain(EnginesDomain.fromDTO(dto.getEnginesDTO()));

        // 각 DTO가 null이더라도 빈 도메인 객체 생성
        motorcycleDomain.setDimensionsDomain(dto.getDimensionsDTO() != null ?
                DimensionsDomain.fromDTO(dto.getDimensionsDTO()) :
                new DimensionsDomain());

        motorcycleDomain.setElectronicsDomain(dto.getElectronicsDTO() != null ?
                ElectronicsDomain.fromDTO(dto.getElectronicsDTO()) :
                new ElectronicsDomain());

        motorcycleDomain.setEnginesDomain(dto.getEnginesDTO() != null ?
                EnginesDomain.fromDTO(dto.getEnginesDTO()) :
                new EnginesDomain());

        return motorcycleDomain;
    }

}
