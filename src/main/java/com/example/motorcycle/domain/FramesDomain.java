//package com.example.motorcycle.domain;
//
//import com.example.motorcycle.dto.FramesDTO;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//public class FramesDomain {
//
//    private Long framesID;
//    private Long motorcycleID;
//
//    private String frame;
//    private String frontSuspension;
//    private String rearSuspension;
//    private Integer frontWheelTravel;
//    private Integer rearWheelTravel;
//    private String frontBrakes;
//    private String rearBrakes;
//    private String absSystem;
//    private String frontWheel;
//    private String rearWheel;
//    private String frontTyre;
//    private String rearTyre;
//    private String wheels;
//    private String abs;
//    private String absPro;
//    private Float rake;
//    private Float trail;
//    private String frontRim;
//    private String rearRim;
//    private String castor;
//    private String steeringAngle;
//    private String steeringHeadAngle;
//
//    public static FramesDomain fromDTO(FramesDTO dto) {
//        FramesDomain framesDomain = new FramesDomain();
//        framesDomain.setFramesID(dto.getFramesID());
//        framesDomain.setMotorcycleID(dto.getMotorcycleID());
//        framesDomain.setFrame(dto.getFrame());
//        framesDomain.setFrontSuspension(dto.getFrontSuspension());
//        framesDomain.setRearSuspension(dto.getRearSuspension());
//        framesDomain.setFrontWheelTravel(dto.getFrontWheelTravel());
//        framesDomain.setRearWheelTravel(dto.getRearWheelTravel());
//        framesDomain.setFrontBrakes(dto.getFrontBrakes());
//        framesDomain.setRearBrakes(dto.getRearBrakes());
//        framesDomain.setAbsSystem(dto.getAbsSystem());
//        framesDomain.setFrontWheel(dto.getFrontWheel());
//        framesDomain.setRearWheel(dto.getRearWheel());
//        framesDomain.setFrontTyre(dto.getFrontTyre());
//        framesDomain.setRearTyre(dto.getRearTyre());
//        framesDomain.setWheels(dto.getWheels());
//        framesDomain.setAbs(dto.getAbs());
//        framesDomain.setAbsPro(dto.getAbsPro());
//        framesDomain.setRake(dto.getRake());
//        framesDomain.setTrail(dto.getTrail());
//        framesDomain.setFrontRim(dto.getFrontRim());
//        framesDomain.setRearRim(dto.getRearRim());
//        framesDomain.setCastor(dto.getCastor());
//        framesDomain.setSteeringAngle(dto.getSteeringAngle());
//        framesDomain.setSteeringHeadAngle(dto.getSteeringHeadAngle());
//        return framesDomain;
//    }
//
//}
