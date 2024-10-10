package com.example.motorcycle.dto;

import com.example.motorcycle.domain.Transmissions;
import lombok.Data;

@Data
public class TransmissionsDTO {
    private Long transmissionsID;
    private Long motorcycleID;  // 외래키
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

    // 도메인 객체를 DTO로 변환하는 메서드 추가
    public static TransmissionsDTO fromDomain(Transmissions transmissions) {
        TransmissionsDTO dto = new TransmissionsDTO();
        dto.setTransmissionsID(transmissions.getTransmissionsID()); // Transmission 고유 식별자 설정
        dto.setMotorcycleID(transmissions.getMotorcycleID()); // Motorcycle 외래 키 설정
        dto.setTransmissionDrive(transmissions.getTransmissionDrive());
        dto.setTransmission(transmissions.getTransmission());
        dto.setFinalDrive(transmissions.getFinalDrive());
        dto.setPrimaryDriveRatio(transmissions.getPrimaryDriveRatio());
        dto.setPrimaryRatio(transmissions.getPrimaryRatio());
        dto.setGearRatio(transmissions.getGearRatio());
        dto.setTransmissionRatio(transmissions.getTransmissionRatio());
        dto.setSecondaryRatio(transmissions.getSecondaryRatio());
        dto.setGearRatios(transmissions.getGearRatios());
        dto.setClutch(transmissions.getClutch());
        return dto;
    }
}
