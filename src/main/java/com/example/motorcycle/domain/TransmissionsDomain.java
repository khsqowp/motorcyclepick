package com.example.motorcycle.domain;

import com.example.motorcycle.dto.TransmissionsDTO;
import lombok.Data;

@Data
public class TransmissionsDomain {

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

    public static TransmissionsDomain fromDTO(TransmissionsDTO dto) {
        TransmissionsDomain transmissions = new TransmissionsDomain();
        transmissions.setTransmissionsID(dto.getTransmissionsID());
        transmissions.setMotorcycleID(dto.getMotorcycleID());
        transmissions.setTransmissionDrive(dto.getTransmissionDrive());
        transmissions.setTransmission(dto.getTransmission());
        transmissions.setFinalDrive(dto.getFinalDrive());
        transmissions.setPrimaryDriveRatio(dto.getPrimaryDriveRatio());
        transmissions.setPrimaryRatio(dto.getPrimaryRatio());
        transmissions.setGearRatio(dto.getGearRatio());
        transmissions.setTransmissionRatio(dto.getTransmissionRatio());
        transmissions.setSecondaryRatio(dto.getSecondaryRatio());
        transmissions.setGearRatios(dto.getGearRatios());
        transmissions.setClutch(dto.getClutch());
        return transmissions;
    }

}
