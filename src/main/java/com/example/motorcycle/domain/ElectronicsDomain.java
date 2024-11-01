package com.example.motorcycle.domain;

import com.example.motorcycle.dto.ElectronicsDTO;
import lombok.Data;

@Data
public class ElectronicsDomain {

    private Long electronicsID;
    private Long motorcycleID;

    private String engineManagement;
    private String emissionControl;
    private String engineControl;
    private String alternator;
    private String battery;
    private String headlight;
    private String ignition;
    private String starting;
    private String tractionControl;

    public static ElectronicsDomain fromDTO(ElectronicsDTO dto) {
        ElectronicsDomain electronics = new ElectronicsDomain();
        electronics.setElectronicsID(dto.getElectronicsID());
        electronics.setMotorcycleID(dto.getMotorcycleID());
        electronics.setEngineManagement(dto.getEngineManagement());
        electronics.setEmissionControl(dto.getEmissionControl());
        electronics.setEngineControl(dto.getEngineControl());
        electronics.setAlternator(dto.getAlternator());
        electronics.setBattery(dto.getBattery());
        electronics.setHeadlight(dto.getHeadlight());
        electronics.setIgnition(dto.getIgnition());
        electronics.setStarting(dto.getStarting());
        electronics.setTractionControl(dto.getTractionControl());
        return electronics;
    }


}
