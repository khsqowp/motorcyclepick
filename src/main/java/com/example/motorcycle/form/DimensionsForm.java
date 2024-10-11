package com.example.motorcycle.form;

import com.example.motorcycle.dto.DimensionsDTO;
import lombok.Data;

@Data
public class DimensionsForm {
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

    // toDTO 메서드: 도메인 객체에서 DTO로 변환
    public DimensionsDTO toDTO() {
        DimensionsDTO dto = new DimensionsDTO();
        dto.setDimensionsID(this.dimensionsID);
        dto.setMotorcycleID(this.motorcycleID);
        dto.setDimensions(this.dimensions);
        dto.setSeatHeight(this.seatHeight);
        dto.setWheelbase(this.wheelbase);
        dto.setGroundClearance(this.groundClearance);
        dto.setDryWeight(this.dryWeight);
        dto.setWetWeight(this.wetWeight);
        dto.setFuelCapacity(this.fuelCapacity);
        dto.setInnerLegCurve(this.innerLegCurve);
        dto.setPermittedTotalWeight(this.permittedTotalWeight);
        return dto;
    }
}
