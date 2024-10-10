package com.example.motorcycle.dto;

import com.example.motorcycle.domain.Electronics;
import com.example.motorcycle.domain.Engines;
import lombok.Data;

@Data
public class EnginesDTO {
    private Long enginesID;
    private Long motorcycleID;  // 외래키
    private String engine;
    private Float capacity;
    private String boreStroke;  // bore_stroke -> boreStroke
    private String compressionRatio;  // compression_ratio -> compressionRatio
    private String coolingSystem;  // cooling_system -> coolingSystem
    private String lubrication;
    private String maxPower;  // max_power -> maxPower
    private String maxTorque;  // max_torque -> maxTorque
    private String fuelSystem;  // fuel_system -> fuelSystem
    private String exhaust;
    private String engineOil;  // engine_oil -> engineOil
    private String mixtureControl;  // mixture_control -> mixtureControl
    private String emission;
    private String induction;
    
    public static EnginesDTO fromDomain(Engines engines) {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        EnginesDTO dto = new EnginesDTO();
        dto.setEnginesID(engines.getEnginesID());
        dto.setMotorcycleID(engines.getMotorcycleID());
        dto.setEngine(engines.getEngine());
        dto.setCapacity(engines.getCapacity());
        dto.setBoreStroke(engines.getBoreStroke());
        dto.setCompressionRatio(engines.getCompressionRatio());
        dto.setCoolingSystem(engines.getCoolingSystem());
        dto.setLubrication(engines.getLubrication());
        dto.setMaxPower(engines.getMaxPower());
        dto.setMaxTorque(engines.getMaxTorque());
        dto.setFuelSystem(engines.getFuelSystem());
        dto.setExhaust(engines.getExhaust());
        dto.setMixtureControl(engines.getMixtureControl());
        dto.setEmission(engines.getEmission());
        dto.setInduction(engines.getInduction());
        // 필요한 필드를 설정합니다.
        return dto;
    }
}
