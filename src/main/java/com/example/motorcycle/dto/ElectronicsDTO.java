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

    public static ElectronicsDTO fromDomain(Electronics electronics) {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        ElectronicsDTO dto = new ElectronicsDTO();
        dto.setElectronicsID(electronics.getElectronicsID());
        dto.setMotorcycleID(electronics.getMotorcycleID());
        dto.setEngineManagement(electronics.getEngineManagement());
        dto.setEmissionControl(electronics.getEmissionControl());
        dto.setEngineControl(electronics.getEngineControl());
        dto.setAlternator(electronics.getAlternator());
        dto.setBattery(electronics.getBattery());
        dto.setHeadlight(electronics.getHeadlight());
        dto.setIgnition(electronics.getIgnition());
        dto.setStarting(electronics.getStarting());
        dto.setTractionControl(electronics.getTractionControl());
        // 필요한 필드를 설정합니다.
        return dto;
    }
}
