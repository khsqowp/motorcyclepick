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

    @Getter
    private DimensionsDomain dimensionsDomain;
    @Getter
    private ElectronicsDomain electronicsDomain;
    @Getter
    private EnginesDomain enginesDomain;
    @Getter
    private FramesDomain framesDomain;
    @Getter
    private TransmissionsDomain transmissionsDomain;

    public static MotorcycleDomain fromDTO(MotorcycleDTO dto) {
        MotorcycleDomain motorcycleDomain = new MotorcycleDomain();
        motorcycleDomain.setMotorcycleID(dto.getMotorcycleID());
        motorcycleDomain.setBrand(dto.getBrand());
        motorcycleDomain.setModel(dto.getModel());
        motorcycleDomain.setYears(dto.getYears());
        motorcycleDomain.setProduction(dto.getProduction());
        motorcycleDomain.setReplica(dto.getReplica());
        motorcycleDomain.setCruiser(dto.getCruiser());
        motorcycleDomain.setTourer(dto.getTourer());
        motorcycleDomain.setAdventure(dto.getAdventure());
        motorcycleDomain.setMultiPurpose(dto.getMultiPurpose());
        motorcycleDomain.setNaked(dto.getNaked());
        motorcycleDomain.setCafeRacer(dto.getCafeRacer());
        motorcycleDomain.setScrambler(dto.getScrambler());
        motorcycleDomain.setOffRoad(dto.getOffRoad());
        motorcycleDomain.setMotard(dto.getMotard());
        motorcycleDomain.setTrial(dto.getTrial());
        motorcycleDomain.setScooter(dto.getScooter());
        motorcycleDomain.setClassic(dto.getClassic());

        motorcycleDomain.setDimensionsDomain(DimensionsDomain.fromDTO(dto.getDimensionsDTO()));
        motorcycleDomain.setElectronicsDomain(ElectronicsDomain.fromDTO(dto.getElectronicsDTO()));
        motorcycleDomain.setEnginesDomain(EnginesDomain.fromDTO(dto.getEnginesDTO()));
        motorcycleDomain.setFramesDomain(FramesDomain.fromDTO(dto.getFramesDTO()));
        motorcycleDomain.setTransmissionsDomain(TransmissionsDomain.fromDTO(dto.getTransmissionsDTO()));

        return motorcycleDomain;
    }

}
