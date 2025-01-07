package com.example.motorcycle.dto;

import com.example.motorcycle.domain.ElectronicsDomain;
import lombok.Data;

@Data
public class ElectronicsDTO {
    private Long electronicsID;
    private Long motorcycleID;
    private String startSystem;
    private Boolean abs;
    private Boolean tcs;
    private Boolean cruiseControl;
    private Boolean assistSlipperClutch;
    private Boolean electricScreen;
    private Boolean clutchAssistSystem;
    private Boolean imu;
    private Boolean corneringAbs;
    private Boolean wheelieControl;
    private Boolean ridingModeChange;
    private Boolean bankingAngleDisplay;
    private Boolean absLevelControl;
    private Boolean quickshiftUp;
    private Boolean quickshiftUpDown;

    public ElectronicsDomain toDomain() {
        ElectronicsDomain electronics = new ElectronicsDomain();
        electronics.setElectronicsID(this.getElectronicsID());
        electronics.setMotorcycleID(this.getMotorcycleID());
        electronics.setStartSystem(this.getStartSystem());
        electronics.setAbs(this.getAbs());
        electronics.setTcs(this.getTcs());
        electronics.setCruiseControl(this.getCruiseControl());
        electronics.setAssistSlipperClutch(this.getAssistSlipperClutch());
        electronics.setElectricScreen(this.getElectricScreen());
        electronics.setClutchAssistSystem(this.getClutchAssistSystem());
        electronics.setImu(this.getImu());
        electronics.setCorneringAbs(this.getCorneringAbs());
        electronics.setWheelieControl(this.getWheelieControl());
        electronics.setRidingModeChange(this.getRidingModeChange());
        electronics.setBankingAngleDisplay(this.getBankingAngleDisplay());
        electronics.setAbsLevelControl(this.getAbsLevelControl());
        electronics.setQuickshiftUp(this.getQuickshiftUp());
        electronics.setQuickshiftUpDown(this.getQuickshiftUpDown());
        return electronics;
    }

    public static ElectronicsDTO fromDomain(ElectronicsDomain electronics) {
        ElectronicsDTO dto = new ElectronicsDTO();
        dto.setElectronicsID(electronics.getElectronicsID());
        dto.setMotorcycleID(electronics.getMotorcycleID());
        dto.setStartSystem(electronics.getStartSystem());
        dto.setAbs(electronics.getAbs());
        dto.setTcs(electronics.getTcs());
        dto.setCruiseControl(electronics.getCruiseControl());
        dto.setAssistSlipperClutch(electronics.getAssistSlipperClutch());
        dto.setElectricScreen(electronics.getElectricScreen());
        dto.setClutchAssistSystem(electronics.getClutchAssistSystem());
        dto.setImu(electronics.getImu());
        dto.setCorneringAbs(electronics.getCorneringAbs());
        dto.setWheelieControl(electronics.getWheelieControl());
        dto.setRidingModeChange(electronics.getRidingModeChange());
        dto.setBankingAngleDisplay(electronics.getBankingAngleDisplay());
        dto.setAbsLevelControl(electronics.getAbsLevelControl());
        dto.setQuickshiftUp(electronics.getQuickshiftUp());
        dto.setQuickshiftUpDown(electronics.getQuickshiftUpDown());
        return dto;
    }
}