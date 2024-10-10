package com.example.motorcycle.domain;

import com.example.motorcycle.dto.EnginesDTO;
import lombok.Data;

@Data
public class Engines {

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

    public static Engines fromDTO(EnginesDTO dto) {
        Engines engines = new Engines();
        engines.setEnginesID(dto.getEnginesID());
        engines.setMotorcycleID(dto.getMotorcycleID());
        engines.setEngine(dto.getEngine());
        engines.setCapacity(dto.getCapacity());
        engines.setBoreStroke(dto.getBoreStroke());
        engines.setCompressionRatio(dto.getCompressionRatio());
        engines.setCoolingSystem(dto.getCoolingSystem());
        engines.setLubrication(dto.getLubrication());
        engines.setMaxPower(dto.getMaxPower());
        engines.setMaxTorque(dto.getMaxTorque());
        engines.setFuelSystem(dto.getFuelSystem());
        engines.setExhaust(dto.getExhaust());
        engines.setEngineOil(dto.getEngineOil());
        engines.setMixtureControl(dto.getMixtureControl());
        engines.setEmission(dto.getEmission());
        engines.setInduction(dto.getInduction());
        return engines;
    }
}
