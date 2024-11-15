//package com.example.motorcycle.domain;
//
//import com.example.motorcycle.dto.TransmissionsDTO;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//public class TransmissionsDomain {
//
//    private Long transmissionsID;
//    private Long motorcycleID;
//
//    private String transmissionDrive;
//    private String transmission;
//    private String finalDrive;
//    private String primaryDriveRatio;
//    private String primaryRatio;
//    private String gearRatio;
//    private String transmissionRatio;
//    private String secondaryRatio;
//    private String gearRatios;
//    private String clutch;
//
//    public static TransmissionsDomain fromDTO(TransmissionsDTO dto) {
//        TransmissionsDomain transmissionsDomain = new TransmissionsDomain();
//        transmissionsDomain.setTransmissionsID(dto.getTransmissionsID());
//        transmissionsDomain.setMotorcycleID(dto.getMotorcycleID());
//        transmissionsDomain.setTransmissionDrive(dto.getTransmissionDrive());
//        transmissionsDomain.setTransmission(dto.getTransmission());
//        transmissionsDomain.setFinalDrive(dto.getFinalDrive());
//        transmissionsDomain.setPrimaryDriveRatio(dto.getPrimaryDriveRatio());
//        transmissionsDomain.setPrimaryRatio(dto.getPrimaryRatio());
//        transmissionsDomain.setGearRatio(dto.getGearRatio());
//        transmissionsDomain.setTransmissionRatio(dto.getTransmissionRatio());
//        transmissionsDomain.setSecondaryRatio(dto.getSecondaryRatio());
//        transmissionsDomain.setGearRatios(dto.getGearRatios());
//        transmissionsDomain.setClutch(dto.getClutch());
//        return transmissionsDomain;
//    }
//
//}
