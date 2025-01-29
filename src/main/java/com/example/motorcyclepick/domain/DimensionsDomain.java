package com.example.motorcyclepick.domain;

import com.example.motorcyclepick.dto.DimensionsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DimensionsDomain {

    private Long dimensionsID;
    private Long motorcycleID;

    private Float wheelbase;
    private Float seatHeight;
    private Float wetWeight;
    private Float fuelCapacity;
    private Float groundClearance;
    private Float dryWeight;
    private Float length;
    private Float width;
    private Float height;
    private Float frontTyreWidth;
    private Float frontTyreAspectRatio;
    private String frontTyreStructure;
    private Float frontTyreDiameter;
    private Float rearTyreWidth;
    private Float rearTyreAspectRatio;
    private String rearTyreStructure;
    private Float rearTyreDiameter;
    private String frontBrakeDiscCount;
    private Float frontBrakeDiscSize;
    private String frontBrakeDiscType;
    private Float frontBrakePistonCount;
    private Float rearBrakeDiscSize;
    private String rearBrakeDiscType;
    private Float rearBrakePistonCount;
    private String frameType;
    private String frameMaterial;

    public static DimensionsDomain fromDTO(DimensionsDTO dto) {
        DimensionsDomain dimensionsDomain = new DimensionsDomain();
        dimensionsDomain.setDimensionsID(dto.getDimensionsID());
        dimensionsDomain.setMotorcycleID(dto.getMotorcycleID());
        dimensionsDomain.setWheelbase(dto.getWheelbase());
        dimensionsDomain.setSeatHeight(dto.getSeatHeight());
        dimensionsDomain.setWetWeight(dto.getWetWeight());
        dimensionsDomain.setFuelCapacity(dto.getFuelCapacity());
        dimensionsDomain.setGroundClearance(dto.getGroundClearance());
        dimensionsDomain.setDryWeight(dto.getDryWeight());
        dimensionsDomain.setLength(dto.getLength());
        dimensionsDomain.setWidth(dto.getWidth());
        dimensionsDomain.setHeight(dto.getHeight());
        dimensionsDomain.setFrontTyreWidth(dto.getFrontTyreWidth());
        dimensionsDomain.setFrontTyreAspectRatio(dto.getFrontTyreAspectRatio());
        dimensionsDomain.setFrontTyreStructure(dto.getFrontTyreStructure());
        dimensionsDomain.setFrontTyreDiameter(dto.getFrontTyreDiameter());
        dimensionsDomain.setRearTyreWidth(dto.getRearTyreWidth());
        dimensionsDomain.setRearTyreAspectRatio(dto.getRearTyreAspectRatio());
        dimensionsDomain.setRearTyreStructure(dto.getRearTyreStructure());
        dimensionsDomain.setRearTyreDiameter(dto.getRearTyreDiameter());
        dimensionsDomain.setFrontBrakeDiscCount(dto.getFrontBrakeDiscCount());
        dimensionsDomain.setFrontBrakeDiscSize(dto.getFrontBrakeDiscSize());
        dimensionsDomain.setFrontBrakeDiscType(dto.getFrontBrakeDiscType());
        dimensionsDomain.setFrontBrakePistonCount(dto.getFrontBrakePistonCount());
        dimensionsDomain.setRearBrakeDiscSize(dto.getRearBrakeDiscSize());
        dimensionsDomain.setRearBrakeDiscType(dto.getRearBrakeDiscType());
        dimensionsDomain.setRearBrakePistonCount(dto.getRearBrakePistonCount());
        dimensionsDomain.setFrameType(dto.getFrameType());
        dimensionsDomain.setFrameMaterial(dto.getFrameMaterial());
        return dimensionsDomain;
    }


}
