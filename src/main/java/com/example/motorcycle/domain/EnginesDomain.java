package com.example.motorcycle.domain;

import com.example.motorcycle.dto.EnginesDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnginesDomain {

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

    public static EnginesDomain fromDTO(EnginesDTO dto) {
        EnginesDomain enginesDomain = new EnginesDomain();
        enginesDomain.setEnginesID(dto.getEnginesID());
        enginesDomain.setMotorcycleID(dto.getMotorcycleID());
        enginesDomain.setEngine(dto.getEngine());
        enginesDomain.setCapacity(dto.getCapacity());
        enginesDomain.setBoreStroke(dto.getBoreStroke());
        enginesDomain.setCompressionRatio(dto.getCompressionRatio());
        enginesDomain.setCoolingSystem(dto.getCoolingSystem());
        enginesDomain.setLubrication(dto.getLubrication());
        enginesDomain.setMaxPower(dto.getMaxPower());
        enginesDomain.setMaxTorque(dto.getMaxTorque());
        enginesDomain.setFuelSystem(dto.getFuelSystem());
        enginesDomain.setExhaust(dto.getExhaust());
        enginesDomain.setEngineOil(dto.getEngineOil());
        enginesDomain.setMixtureControl(dto.getMixtureControl());
        enginesDomain.setEmission(dto.getEmission());
        enginesDomain.setInduction(dto.getInduction());
        return enginesDomain;
    }

}
