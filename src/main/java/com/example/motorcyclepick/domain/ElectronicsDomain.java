package com.example.motorcyclepick.domain;

import com.example.motorcyclepick.dto.ElectronicsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElectronicsDomain {
    private Long electronicsID;          // bigint
    private Long motorcycleID;           // bigint
    private String startSystem;          // varchar(255)
    private Boolean abs;                 // tinyint(1)
    private Boolean tcs;                 // tinyint(1)
    private Boolean cruiseControl;       // tinyint(1)
    private Boolean assistSlipperClutch; // tinyint(1)
    private Boolean electricScreen;    // tinyint(1)
    private Boolean clutchAssistSystem;  // tinyint(1)
    private Boolean imu;                 // tinyint(1)
    private Boolean corneringAbs;        // tinyint(1)
    private Boolean wheelieControl;      // tinyint(1)
    private Boolean ridingModeChange;    // tinyint(1)
    private Boolean bankingAngleDisplay; // tinyint(1)
    private Boolean absLevelControl;     // tinyint(1)
    private Boolean quickshiftUp;        // tinyint(1)
    private Boolean quickshiftUpDown;    // tinyint(1)

    public static ElectronicsDomain fromDTO(ElectronicsDTO dto) {
        ElectronicsDomain domain = new ElectronicsDomain();
        domain.setElectronicsID(dto.getElectronicsID());
        domain.setMotorcycleID(dto.getMotorcycleID());
        domain.setStartSystem(dto.getStartSystem());
        domain.setAbs(dto.getAbs());
        domain.setTcs(dto.getTcs());
        domain.setCruiseControl(dto.getCruiseControl());
        domain.setAssistSlipperClutch(dto.getAssistSlipperClutch());
        domain.setElectricScreen(dto.getElectricScreen());
        domain.setClutchAssistSystem(dto.getClutchAssistSystem());
        domain.setImu(dto.getImu());
        domain.setCorneringAbs(dto.getCorneringAbs());
        domain.setWheelieControl(dto.getWheelieControl());
        domain.setRidingModeChange(dto.getRidingModeChange());
        domain.setBankingAngleDisplay(dto.getBankingAngleDisplay());
        domain.setAbsLevelControl(dto.getAbsLevelControl());
        domain.setQuickshiftUp(dto.getQuickshiftUp());
        domain.setQuickshiftUpDown(dto.getQuickshiftUpDown());
        return domain;
    }
}