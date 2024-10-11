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

    public Dimensions toDomain() {
        // Motorcycle 객체를 MotorcycleDTO 객체로 변환
        Dimensions dimensions1 = new Dimensions();
        dimensions1.setDimensionsID(this.getDimensionsID());
        dimensions1.setMotorcycleID(this.getMotorcycleID());
        dimensions1.setDimensions(this.getDimensions());
        dimensions1.setSeatHeight(this.getSeatHeight());
        dimensions1.setWheelbase(this.getWheelbase());
        dimensions1.setGroundClearance(this.getGroundClearance());
        dimensions1.setDryWeight(this.getDryWeight());
        dimensions1.setFuelCapacity(this.getFuelCapacity());
        dimensions1.setInnerLegCurve(this.getInnerLegCurve());
        dimensions1.setPermittedTotalWeight(this.getPermittedTotalWeight());
        // 필요한 필드를 설정합니다.
        return dimensions1;
    }
}
