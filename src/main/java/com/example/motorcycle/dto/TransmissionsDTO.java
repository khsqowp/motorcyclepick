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
    public Transmissions toDomain() {
        Transmissions transmissions = new Transmissions();
        transmissions.setTransmissionsID(this.getTransmissionsID()); // Transmission 고유 식별자 설정
        transmissions.setMotorcycleID(this.getMotorcycleID()); // Motorcycle 외래 키 설정
        transmissions.setTransmissionDrive(this.getTransmissionDrive());
        transmissions.setTransmission(this.getTransmission());
        transmissions.setFinalDrive(this.getFinalDrive());
        transmissions.setPrimaryDriveRatio(this.getPrimaryDriveRatio());
        transmissions.setPrimaryRatio(this.getPrimaryRatio());
        transmissions.setGearRatio(this.getGearRatio());
        transmissions.setTransmissionRatio(this.getTransmissionRatio());
        transmissions.setSecondaryRatio(this.getSecondaryRatio());
        transmissions.setGearRatios(this.getGearRatios());
        transmissions.setClutch(this.getClutch());
        return transmissions;
    }
}
