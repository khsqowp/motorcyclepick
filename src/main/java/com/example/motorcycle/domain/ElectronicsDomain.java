package com.example.motorcycle.domain;

import com.example.motorcycle.dto.ElectronicsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.pl.NIP;

@Data
@NoArgsConstructor
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
        ElectronicsDomain electronicsDomain = new ElectronicsDomain();
        electronicsDomain.setElectronicsID(dto.getElectronicsID());
        electronicsDomain.setMotorcycleID(dto.getMotorcycleID());
        electronicsDomain.setEngineManagement(dto.getEngineManagement());
        electronicsDomain.setEmissionControl(dto.getEmissionControl());
        electronicsDomain.setEngineControl(dto.getEngineControl());
        electronicsDomain.setAlternator(dto.getAlternator());
        electronicsDomain.setBattery(dto.getBattery());
        electronicsDomain.setHeadlight(dto.getHeadlight());
        electronicsDomain.setIgnition(dto.getIgnition());
        electronicsDomain.setStarting(dto.getStarting());
        electronicsDomain.setTractionControl(dto.getTractionControl());
        return electronicsDomain;
    }


}
