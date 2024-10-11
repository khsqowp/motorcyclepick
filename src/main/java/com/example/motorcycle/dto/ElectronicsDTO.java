package com.example.motorcycle.dto;

import com.example.motorcycle.domain.Dimensions;
import com.example.motorcycle.domain.Electronics;
import lombok.Data;

@Data
public class ElectronicsDTO {
    private Long electronicsID;
    private Long motorcycleID;  // 외래키
    private String engineManagement;
    private String emissionControl;
    private String engineControl;
    private String alternator;
    private String battery;
    private String headlight;
    private String ignition;
    private String starting;
    private String tractionControl;

    public Electronics toDomain() {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        Electronics electronics = new Electronics();
        electronics.setElectronicsID(this.getElectronicsID());
        electronics.setMotorcycleID(this.getMotorcycleID());
        electronics.setEngineManagement(this.getEngineManagement());
        electronics.setEmissionControl(this.getEmissionControl());
        electronics.setEngineControl(this.getEngineControl());
        electronics.setAlternator(this.getAlternator());
        electronics.setBattery(this.getBattery());
        electronics.setHeadlight(this.getHeadlight());
        electronics.setIgnition(this.getIgnition());
        electronics.setStarting(this.getStarting());
        electronics.setTractionControl(this.getTractionControl());
        // 필요한 필드를 설정합니다.
        return electronics;
    }
}
