//package com.example.motorcycle.form;
//
//import com.example.motorcycle.dto.FramesDTO;
//import lombok.Data;
//
//@Data
//public class FramesForm {
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
//    // toDTO 메서드: 도메인 객체에서 DTO로 변환
//    public FramesDTO toDTO() {
//        FramesDTO dto = new FramesDTO();
//        dto.setFramesID(this.getFramesID());
//        dto.setMotorcycleID(this.getMotorcycleID());
//        dto.setFrame(this.getFrame());
//        dto.setFrontSuspension(this.getFrontSuspension());
//        dto.setRearSuspension(this.getRearSuspension());
//        dto.setFrontWheelTravel(this.getFrontWheelTravel());
//        dto.setRearWheelTravel(this.getRearWheelTravel());
//        dto.setFrontBrakes(this.getFrontBrakes());
//        dto.setRearBrakes(this.getRearBrakes());
//        dto.setAbsSystem(this.getAbsSystem());
//        dto.setFrontWheel(this.getFrontWheel());
//        dto.setRearWheel(this.getRearWheel());
//        dto.setFrontTyre(this.getFrontTyre());
//        dto.setRearTyre(this.getRearTyre());
//        dto.setWheels(this.getWheels());
//        dto.setAbs(this.getAbs());
//        dto.setAbsPro(this.getAbsPro());
//        dto.setRake(this.getRake());
//        dto.setTrail(this.getTrail());
//        dto.setFrontRim(this.getFrontRim());
//        dto.setRearRim(this.getRearRim());
//        dto.setCastor(this.getCastor());
//        dto.setSteeringAngle(this.getSteeringAngle());
//        dto.setSteeringHeadAngle(this.getSteeringHeadAngle());
//        return dto;
//    }
//}
