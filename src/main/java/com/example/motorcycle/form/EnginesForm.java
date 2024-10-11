package com.example.motorcycle.form;

import com.example.motorcycle.dto.EnginesDTO;
import lombok.Data;

@Data
public class EnginesForm {
    private Long enginesID;
    private Long motorcycleID;

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

    // toDTO 메서드: 도메인 객체에서 DTO로 변환
    public EnginesDTO toDTO() {
        EnginesDTO dto = new EnginesDTO();
        dto.setEnginesID(this.getEnginesID());
        dto.setMotorcycleID(this.getMotorcycleID());
        dto.setEngine(this.getEngine());
        dto.setCapacity(this.getCapacity());
        dto.setBoreStroke(this.getBoreStroke());
        dto.setCompressionRatio(this.getCompressionRatio());
        dto.setCoolingSystem(this.getCoolingSystem());
        dto.setLubrication(this.getLubrication());
        dto.setMaxPower(this.getMaxPower());
        dto.setMaxTorque(this.getMaxTorque());
        dto.setFuelSystem(this.getFuelSystem());
        dto.setExhaust(this.getExhaust());
        dto.setEngineOil(this.getEngineOil());
        dto.setMixtureControl(this.getMixtureControl());
        dto.setEmission(this.getEmission());
        dto.setInduction(this.getInduction());
        return dto;
    }
}
