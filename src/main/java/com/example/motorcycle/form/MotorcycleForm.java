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

    private Dimensions dimensions;
    private Electronics electronics;
    private Engines engines;
    private Frames frames;
    private Transmissions transmissions;

    // toDTO 메서드: 도메인 객체에서 DTO로 변환
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
}
