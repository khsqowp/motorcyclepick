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
    
    public Engines toDomain() {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        Engines engines = new Engines();
        engines.setEnginesID(this.getEnginesID());
        engines.setMotorcycleID(this.getMotorcycleID());
        engines.setEngine(this.getEngine());
        engines.setCapacity(this.getCapacity());
        engines.setBoreStroke(this.getBoreStroke());
        engines.setCompressionRatio(this.getCompressionRatio());
        engines.setCoolingSystem(this.getCoolingSystem());
        engines.setLubrication(this.getLubrication());
        engines.setMaxPower(this.getMaxPower());
        engines.setMaxTorque(this.getMaxTorque());
        engines.setFuelSystem(this.getFuelSystem());
        engines.setExhaust(this.getExhaust());
        engines.setMixtureControl(this.getMixtureControl());
        engines.setEmission(this.getEmission());
        engines.setInduction(this.getInduction());
        // 필요한 필드를 설정합니다.
        return engines;
    }
}
