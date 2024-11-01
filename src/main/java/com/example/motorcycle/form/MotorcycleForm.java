package com.example.motorcycle.form;

import com.example.motorcycle.domain.*;
import com.example.motorcycle.dto.MotorcycleDTO;
import lombok.Data;

@Data
public class MotorcycleForm {
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

    private DimensionsForm dimensionsForm;
    private ElectronicsForm electronicsForm;
    private EnginesForm enginesForm;
    private FramesForm framesForm;
    private TransmissionsForm transmissionsForm;


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
        return dto;
    }

    // fromDTO 메서드: DTO 데이터를 Form으로 변환
    public static MotorcycleForm fromDTO(MotorcycleDTO dto) {
        MotorcycleForm form = new MotorcycleForm();
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
        return form;
    }
}
