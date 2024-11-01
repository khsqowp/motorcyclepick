package com.example.motorcycle.dto;

import com.example.motorcycle.domain.DimensionsDomain;
import com.example.motorcycle.domain.ElectronicsDomain;
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

    public ElectronicsDomain toDomain() {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        ElectronicsDomain electronics = new ElectronicsDomain();
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

    public static ElectronicsDTO fromDomain(ElectronicsDomain electronics) {
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
