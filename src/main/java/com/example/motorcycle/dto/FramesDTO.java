package com.example.motorcycle.dto;

import com.example.motorcycle.domain.FramesDomain;
import lombok.Data;

@Data
public class FramesDTO {
    private Long framesID;
    private Long motorcycleID;  // 외래키
    private String frame;
    private String frontSuspension;
    private String rearSuspension;
    private Integer frontWheelTravel;
    private Integer rearWheelTravel;
    private String frontBrakes;
    private String rearBrakes;
    private String absSystem;
    private String frontWheel;
    private String rearWheel;
    private String frontTyre;
    private String rearTyre;
    private String wheels;
    private String abs;
    private String absPro;
    private Float rake;
    private Float trail;
    private String frontRim;
    private String rearRim;
    private String castor;
    private String steeringAngle;
    private String steeringHeadAngle;

    public FramesDomain toDomain() {
        FramesDomain frames = new FramesDomain();
        frames.setFramesID(this.getFramesID());
        frames.setMotorcycleID(this.getMotorcycleID());
        frames.setFrame(this.getFrame());
        frames.setFrontSuspension(this.getFrontSuspension());
        frames.setRearSuspension(this.getRearSuspension());
        frames.setFrontWheelTravel(this.getFrontWheelTravel());
        frames.setRearWheelTravel(this.getRearWheelTravel());
        frames.setFrontBrakes(this.getFrontBrakes());
        frames.setRearBrakes(this.getRearBrakes());
        frames.setAbsSystem(this.getAbsSystem());
        frames.setFrontWheel(this.getFrontWheel());
        frames.setRearWheel(this.getRearWheel());
        frames.setFrontTyre(this.getFrontTyre());
        frames.setRearTyre(this.getRearTyre());
        frames.setWheels(this.getWheels());
        frames.setAbs(this.getAbs());
        frames.setAbsPro(this.getAbsPro());
        frames.setRake(this.getRake());
        frames.setTrail(this.getTrail());
        frames.setFrontRim(this.getFrontRim());
        frames.setRearRim(this.getRearRim());
        frames.setCastor(this.getCastor());
        frames.setSteeringAngle(this.getSteeringAngle());
        frames.setSteeringHeadAngle(this.getSteeringHeadAngle());
        return frames;
    }

    public static FramesDTO fromDomain(FramesDomain frames) {
        FramesDTO dto = new FramesDTO();
        dto.setFramesID(frames.getFramesID());
        dto.setMotorcycleID(frames.getMotorcycleID());
        dto.setFrame(frames.getFrame());
        dto.setFrontSuspension(frames.getFrontSuspension());
        dto.setRearSuspension(frames.getRearSuspension());
        dto.setFrontWheelTravel(frames.getFrontWheelTravel());
        dto.setRearWheelTravel(frames.getRearWheelTravel());
        dto.setFrontBrakes(frames.getFrontBrakes());
        dto.setRearBrakes(frames.getRearBrakes());
        dto.setAbsSystem(frames.getAbsSystem());
        dto.setFrontWheel(frames.getFrontWheel());
        dto.setRearWheel(frames.getRearWheel());
        dto.setFrontTyre(frames.getFrontTyre());
        dto.setRearTyre(frames.getRearTyre());
        dto.setWheels(frames.getWheels());
        dto.setAbs(frames.getAbs());
        dto.setAbsPro(frames.getAbsPro());
        dto.setRake(frames.getRake());
        dto.setTrail(frames.getTrail());
        dto.setFrontRim(frames.getFrontRim());
        dto.setRearRim(frames.getRearRim());
        dto.setCastor(frames.getCastor());
        dto.setSteeringAngle(frames.getSteeringAngle());
        dto.setSteeringHeadAngle(frames.getSteeringHeadAngle());
        return dto;
    }
}
