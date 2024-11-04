//package com.example.motorcycle.form;
//
//import com.example.motorcycle.dto.ElectronicsDTO;
//import lombok.Data;
//
//@Data
//public class ElectronicsForm {
//    private String engineManagement;
//    private String emissionControl;
//    private String engineControl;
//    private String alternator;
//    private String battery;
//    private String headlight;
//    private String ignition;
//    private String starting;
//    private String tractionControl;
//
//    public ElectronicsDTO toDTO(Long motorcycleID) {
//        ElectronicsDTO dto = new ElectronicsDTO();
//        dto.setMotorcycleID(motorcycleID);  // 외래키 설정
//        dto.setEngineManagement(this.engineManagement);
//        dto.setEmissionControl(this.emissionControl);
//        dto.setEngineControl(this.engineControl);
//        dto.setAlternator(this.alternator);
//        dto.setBattery(this.battery);
//        dto.setHeadlight(this.headlight);
//        dto.setIgnition(this.ignition);
//        dto.setStarting(this.starting);
//        dto.setTractionControl(this.tractionControl);
//        return dto;
//    }
//}
