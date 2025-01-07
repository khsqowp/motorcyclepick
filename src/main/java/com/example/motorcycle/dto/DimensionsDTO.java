package com.example.motorcycle.dto;

import com.example.motorcycle.domain.DimensionsDomain;
import lombok.Data;

@Data
public class DimensionsDTO {
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

    public DimensionsDomain toDomain() {
        DimensionsDomain dimensions = new DimensionsDomain();
        dimensions.setDimensionsID(this.getDimensionsID());
        dimensions.setMotorcycleID(this.getMotorcycleID());
        dimensions.setWheelbase(this.getWheelbase());
        dimensions.setSeatHeight(this.getSeatHeight());
        dimensions.setWetWeight(this.getWetWeight());
        dimensions.setFuelCapacity(this.getFuelCapacity());
        dimensions.setGroundClearance(this.getGroundClearance());
        dimensions.setDryWeight(this.getDryWeight());
        dimensions.setLength(this.getLength());
        dimensions.setWidth(this.getWidth());
        dimensions.setHeight(this.getHeight());
        dimensions.setFrontTyreWidth(this.getFrontTyreWidth());
        dimensions.setFrontTyreAspectRatio(this.getFrontTyreAspectRatio());
        dimensions.setFrontTyreStructure(this.getFrontTyreStructure());
        dimensions.setFrontTyreDiameter(this.getFrontTyreDiameter());
        dimensions.setRearTyreWidth(this.getRearTyreWidth());
        dimensions.setRearTyreAspectRatio(this.getRearTyreAspectRatio());
        dimensions.setRearTyreStructure(this.getRearTyreStructure());
        dimensions.setRearTyreDiameter(this.getRearTyreDiameter());
        dimensions.setFrontBrakeDiscCount(this.getFrontBrakeDiscCount());
        dimensions.setFrontBrakeDiscSize(this.getFrontBrakeDiscSize());
        dimensions.setFrontBrakeDiscType(this.getFrontBrakeDiscType());
        dimensions.setFrontBrakePistonCount(this.getFrontBrakePistonCount());
        dimensions.setRearBrakeDiscSize(this.getRearBrakeDiscSize());
        dimensions.setRearBrakeDiscType(this.getRearBrakeDiscType());
        dimensions.setRearBrakePistonCount(this.getRearBrakePistonCount());
        dimensions.setFrameType(this.getFrameType());
        dimensions.setFrameMaterial(this.getFrameMaterial());
        return dimensions;
    }

    public static DimensionsDTO fromDomain(DimensionsDomain dimensions) {
        DimensionsDTO dto = new DimensionsDTO();
        dto.setDimensionsID(dimensions.getDimensionsID());
        dto.setMotorcycleID(dimensions.getMotorcycleID());
        dto.setWheelbase(dimensions.getWheelbase());
        dto.setSeatHeight(dimensions.getSeatHeight());
        dto.setWetWeight(dimensions.getWetWeight());
        dto.setFuelCapacity(dimensions.getFuelCapacity());
        dto.setGroundClearance(dimensions.getGroundClearance());
        dto.setDryWeight(dimensions.getDryWeight());
        dto.setLength(dimensions.getLength());
        dto.setWidth(dimensions.getWidth());
        dto.setHeight(dimensions.getHeight());
        dto.setFrontTyreWidth(dimensions.getFrontTyreWidth());
        dto.setFrontTyreAspectRatio(dimensions.getFrontTyreAspectRatio());
        dto.setFrontTyreStructure(dimensions.getFrontTyreStructure());
        dto.setFrontTyreDiameter(dimensions.getFrontTyreDiameter());
        dto.setRearTyreWidth(dimensions.getRearTyreWidth());
        dto.setRearTyreAspectRatio(dimensions.getRearTyreAspectRatio());
        dto.setRearTyreStructure(dimensions.getRearTyreStructure());
        dto.setRearTyreDiameter(dimensions.getRearTyreDiameter());
        dto.setFrontBrakeDiscCount(dimensions.getFrontBrakeDiscCount());
        dto.setFrontBrakeDiscSize(dimensions.getFrontBrakeDiscSize());
        dto.setFrontBrakeDiscType(dimensions.getFrontBrakeDiscType());
        dto.setFrontBrakePistonCount(dimensions.getFrontBrakePistonCount());
        dto.setRearBrakeDiscSize(dimensions.getRearBrakeDiscSize());
        dto.setRearBrakeDiscType(dimensions.getRearBrakeDiscType());
        dto.setRearBrakePistonCount(dimensions.getRearBrakePistonCount());
        dto.setFrameType(dimensions.getFrameType());
        dto.setFrameMaterial(dimensions.getFrameMaterial());
        return dto;
    }
}