package com.example.motorcycle.domain;

import com.example.motorcycle.dto.MotorcycleDTO;
import lombok.Data;
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

    private DimensionsDomain dimensionsDomain;
    private ElectronicsDomain electronicsDomain;
    private EnginesDomain enginesDomain;
    private FramesDomain framesDomain;
    private TransmissionsDomain transmissionsDomain;

    public static MotorcycleDomain fromDTO(MotorcycleDTO dto) {
        MotorcycleDomain motorcycle = new MotorcycleDomain();
        motorcycle.setMotorcycleID(dto.getMotorcycleID());
        motorcycle.setBrand(dto.getBrand());
        motorcycle.setModel(dto.getModel());
        motorcycle.setYears(dto.getYears());
        motorcycle.setProduction(dto.getProduction());
        motorcycle.setReplica(dto.getReplica());
        motorcycle.setCruiser(dto.getCruiser());
        motorcycle.setTourer(dto.getTourer());
        motorcycle.setAdventure(dto.getAdventure());
        motorcycle.setMultiPurpose(dto.getMultiPurpose());
        motorcycle.setNaked(dto.getNaked());
        motorcycle.setCafeRacer(dto.getCafeRacer());
        motorcycle.setScrambler(dto.getScrambler());
        motorcycle.setOffRoad(dto.getOffRoad());
        motorcycle.setMotard(dto.getMotard());
        motorcycle.setTrial(dto.getTrial());
        motorcycle.setScooter(dto.getScooter());
        motorcycle.setClassic(dto.getClassic());

        motorcycle.setDimensionsDomain(DimensionsDomain.fromDTO(dto.getDimensionsDTO()));
        motorcycle.setElectronicsDomain(ElectronicsDomain.fromDTO(dto.getElectronicsDTO()));
        motorcycle.setEnginesDomain(EnginesDomain.fromDTO(dto.getEnginesDTO()));
        motorcycle.setFramesDomain(FramesDomain.fromDTO(dto.getFramesDTO()));
        motorcycle.setTransmissionsDomain(TransmissionsDomain.fromDTO(dto.getTransmissionsDTO()));

        return motorcycle;
    }

}
