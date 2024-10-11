package com.example.motorcycle.dto;

import com.example.motorcycle.domain.Frames;
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

    public Frames toDomain() {
        Frames frames = new Frames();
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
}
