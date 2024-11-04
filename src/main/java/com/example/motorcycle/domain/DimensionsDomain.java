package com.example.motorcycle.domain;

import com.example.motorcycle.dto.DimensionsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.pl.NIP;

@Data
@NoArgsConstructor
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
        DimensionsDomain dimensionsDomain = new DimensionsDomain();
        dimensionsDomain.setDimensionsID(dto.getDimensionsID());
        dimensionsDomain.setMotorcycleID(dto.getMotorcycleID());
        dimensionsDomain.setDimensions(dto.getDimensions());
        dimensionsDomain.setSeatHeight(dto.getSeatHeight());
        dimensionsDomain.setWheelbase(dto.getWheelbase());
        dimensionsDomain.setGroundClearance(dto.getGroundClearance());
        dimensionsDomain.setDryWeight(dto.getDryWeight());
        dimensionsDomain.setWetWeight(dto.getWetWeight());
        dimensionsDomain.setFuelCapacity(dto.getFuelCapacity());
        dimensionsDomain.setInnerLegCurve(dto.getInnerLegCurve());
        dimensionsDomain.setPermittedTotalWeight(dto.getPermittedTotalWeight());
        return dimensionsDomain;
    }


}
