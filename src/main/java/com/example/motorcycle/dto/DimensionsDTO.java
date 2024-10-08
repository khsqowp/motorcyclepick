package com.example.motorcycle.dto;

import com.example.motorcycle.domain.Dimensions;
import lombok.Data;

@Data
public class DimensionsDTO {
    private Long dimensionsID;
    private Long motorcycleID;  // 외래키

    private String dimensions;
    private Float seatHeight;  // seat_height -> seatHeight
    private Float wheelbase;
    private Float groundClearance;  // ground_clearance -> groundClearance
    private Float dryWeight;  // dry_weight -> dryWeight
    private Float wetWeight;  // wet_weight -> wetWeight
    private Float fuelCapacity;  // fuel_capacity -> fuelCapacity
    private String innerLegCurve;  // inner_leg_curve -> innerLegCurve
    private String permittedTotalWeight;  // permitted_total_weight -> permittedTotalWeight

    public static DimensionsDTO fromDomain(Dimensions dimensions) {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        DimensionsDTO dto = new DimensionsDTO();
        dto.setDimensionsID(dimensions.getDimensionsID());
        dto.setMotorcycleID(dimensions.getMotorcycleID());
        dto.setDimensions(dimensions.getDimensions());
        dto.setSeatHeight(dimensions.getSeatHeight());
        dto.setWheelbase(dimensions.getWheelbase());
        dto.setGroundClearance(dimensions.getGroundClearance());
        dto.setDryWeight(dimensions.getDryWeight());
        dto.setFuelCapacity(dimensions.getFuelCapacity());
        dto.setInnerLegCurve(dimensions.getInnerLegCurve());
        dto.setPermittedTotalWeight(dimensions.getPermittedTotalWeight());
        // 필요한 필드를 설정합니다.
        return dto;
    }
}
