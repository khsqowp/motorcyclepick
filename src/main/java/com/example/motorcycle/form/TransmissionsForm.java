package com.example.motorcycle.form;

import com.example.motorcycle.dto.TransmissionsDTO;
import lombok.Data;

@Data
public class TransmissionsForm {
    private Long transmissionsID;
    private Long motorcycleID;

    private String transmissionDrive;
    private String transmission;
    private String finalDrive;
    private String primaryDriveRatio;
    private String primaryRatio;
    private String gearRatio;
    private String transmissionRatio;
    private String secondaryRatio;
    private String gearRatios;
    private String clutch;

    // toDTO 메서드: 도메인 객체에서 DTO로 변환
    public TransmissionsDTO toDTO() {
        TransmissionsDTO dto = new TransmissionsDTO();
        dto.setTransmissionsID(this.getTransmissionsID());
        dto.setMotorcycleID(this.getMotorcycleID());
        dto.setTransmissionDrive(this.getTransmissionDrive());
        dto.setTransmission(this.getTransmission());
        dto.setFinalDrive(this.getFinalDrive());
        dto.setPrimaryDriveRatio(this.getPrimaryDriveRatio());
        dto.setPrimaryRatio(this.getPrimaryRatio());
        dto.setGearRatio(this.getGearRatio());
        dto.setTransmissionRatio(this.getTransmissionRatio());
        dto.setSecondaryRatio(this.getSecondaryRatio());
        dto.setGearRatios(this.getGearRatios());
        dto.setClutch(this.getClutch());
        return dto;
    }
}
