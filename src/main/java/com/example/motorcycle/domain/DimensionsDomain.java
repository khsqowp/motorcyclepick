package com.example.motorcycle.domain;

import com.example.motorcycle.dto.DimensionsDTO;
import lombok.Data;

@Data
public class DimensionsDomain {

    private Long dimensionsID;
    private Long motorcycleID;

    private String dimensions;
    private Float seatHeight;
    private Float wheelbase;
    private Float groundClearance;
    private Float dryWeight;
    private Float wetWeight;
    private Float fuelCapacity;
    private String innerLegCurve;
    private String permittedTotalWeight;

    public static DimensionsDomain fromDTO(DimensionsDTO dto) {
        DimensionsDomain dimensions = new DimensionsDomain();
        dimensions.setDimensionsID(dto.getDimensionsID());
        dimensions.setMotorcycleID(dto.getMotorcycleID());
        dimensions.setDimensions(dto.getDimensions());
        dimensions.setSeatHeight(dto.getSeatHeight());
        dimensions.setWheelbase(dto.getWheelbase());
        dimensions.setGroundClearance(dto.getGroundClearance());
        dimensions.setDryWeight(dto.getDryWeight());
        dimensions.setWetWeight(dto.getWetWeight());
        dimensions.setFuelCapacity(dto.getFuelCapacity());
        dimensions.setInnerLegCurve(dto.getInnerLegCurve());
        dimensions.setPermittedTotalWeight(dto.getPermittedTotalWeight());
        return dimensions;
    }


}
