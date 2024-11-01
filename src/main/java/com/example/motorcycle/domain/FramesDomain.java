package com.example.motorcycle.domain;

import com.example.motorcycle.dto.FramesDTO;
import lombok.Data;

@Data
public class FramesDomain {

    private Long framesID;
    private Long motorcycleID;

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

    public static FramesDomain fromDTO(FramesDTO dto) {
        FramesDomain frames = new FramesDomain();
        frames.setFramesID(dto.getFramesID());
        frames.setMotorcycleID(dto.getMotorcycleID());
        frames.setFrame(dto.getFrame());
        frames.setFrontSuspension(dto.getFrontSuspension());
        frames.setRearSuspension(dto.getRearSuspension());
        frames.setFrontWheelTravel(dto.getFrontWheelTravel());
        frames.setRearWheelTravel(dto.getRearWheelTravel());
        frames.setFrontBrakes(dto.getFrontBrakes());
        frames.setRearBrakes(dto.getRearBrakes());
        frames.setAbsSystem(dto.getAbsSystem());
        frames.setFrontWheel(dto.getFrontWheel());
        frames.setRearWheel(dto.getRearWheel());
        frames.setFrontTyre(dto.getFrontTyre());
        frames.setRearTyre(dto.getRearTyre());
        frames.setWheels(dto.getWheels());
        frames.setAbs(dto.getAbs());
        frames.setAbsPro(dto.getAbsPro());
        frames.setRake(dto.getRake());
        frames.setTrail(dto.getTrail());
        frames.setFrontRim(dto.getFrontRim());
        frames.setRearRim(dto.getRearRim());
        frames.setCastor(dto.getCastor());
        frames.setSteeringAngle(dto.getSteeringAngle());
        frames.setSteeringHeadAngle(dto.getSteeringHeadAngle());
        return frames;
    }

}
