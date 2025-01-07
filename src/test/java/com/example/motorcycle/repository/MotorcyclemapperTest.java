//package com.example.motorcycle.repository;
//
//import com.example.motorcycle.domain.*;
//import com.example.motorcycle.dto.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//public class MotorcycleMapperTest {
//
//    @Autowired
//    private MotorcycleMapper motorcycleMapper;
//
//    @Autowired
//    private DimensionsMapper dimensionsMapper;
//
//    @Autowired
//    private ElectronicsMapper electronicsMapper;
//
//    @Autowired
//    private EnginesMapper enginesMapper;
//
//    @Autowired
//    private FramesMapper framesMapper;
//
//    @Autowired
//    private TransmissionsMapper transmissionsMapper;
//
//    @Test
//    public void shouldInsertAndRetrieveMotorcycleData() {
//        // Motorcycle 데이터 삽입
//        Motorcycle motorcycle = new Motorcycle();
//        motorcycle.setBrand("Yamaha");
//        motorcycle.setModel("YZF-R1");
//        motorcycle.setYears(2021L);
//        motorcycle.setProduction("Japan");
//
//        motorcycleMapper.insertMotorcycleData(motorcycle);
//        assertNotNull(motorcycle.getMotorcycleID(), "Motorcycle ID가 생성되지 않았습니다.");
//
//        // 로그 출력
//        System.out.println("Inserted Motorcycle ID: " + motorcycle.getMotorcycleID());
//        System.out.println("Motorcycle Brand: " + motorcycle.getBrand());
//        System.out.println("Motorcycle Model: " + motorcycle.getModel());
//
//        // DTO를 이용한 데이터 삽입 및 검증
//        insertAndVerifyDimensions(motorcycle);
//        insertAndVerifyElectronics(motorcycle);
//        insertAndVerifyEngines(motorcycle);
//        insertAndVerifyFrames(motorcycle);
//        insertAndVerifyTransmissions(motorcycle);
//
//        // 조인된 모든 데이터 조회 및 출력
//        retrieveAndPrintFullMotorcycleData(motorcycle.getMotorcycleID());
//    }
//
//    private void insertAndVerifyDimensions(Motorcycle motorcycle) {
//        DimensionsDTO dimensionsDTO = new DimensionsDTO();
//        dimensionsDTO.setMotorcycleID(motorcycle.getMotorcycleID());
//        dimensionsDTO.setDimensions("2070x705x1150");
//        dimensionsDTO.setSeatHeight(820);
//        dimensionsDTO.setWheelbase(1405);
//        dimensionsDTO.setGroundClearance(130);
//        dimensionsDTO.setDryWeight(200);
//
//        dimensionsMapper.insertDimensionsData(dimensionsDTO);
//        assertNotNull(dimensionsDTO.getMotorcycleID(), "Dimensions ID가 생성되지 않았습니다.");
//
//        // 로그 출력
//        System.out.println("Inserted Dimensions Motorcycle ID: " + dimensionsDTO.getMotorcycleID());
//        System.out.println("Dimensions: " + dimensionsDTO.getDimensions());
//        System.out.println("Seat Height: " + dimensionsDTO.getSeatHeight());
//    }
//
//    private void insertAndVerifyElectronics(Motorcycle motorcycle) {
//        ElectronicsDTO electronicsDTO = new ElectronicsDTO();
//        electronicsDTO.setMotorcycleID(motorcycle.getMotorcycleID());
//        electronicsDTO.setAbs(true);
//        electronicsDTO.setTractionControl(true);
//        electronicsDTO.setRideModes("Sport, Street");
//
//        electronicsMapper.insertElectronicsData(electronicsDTO);
//        assertNotNull(electronicsDTO.getMotorcycleID(), "Electronics ID가 생성되지 않았습니다.");
//
//        // 로그 출력
//        System.out.println("Inserted Electronics Motorcycle ID: " + electronicsDTO.getMotorcycleID());
//        System.out.println("ABS: " + electronicsDTO.isAbs());
//        System.out.println("Ride Modes: " + electronicsDTO.getRideModes());
//    }
//
//    private void insertAndVerifyEngines(Motorcycle motorcycle) {
//        EnginesDTO enginesDTO = new EnginesDTO();
//        enginesDTO.setMotorcycleID(motorcycle.getMotorcycleID());
//        enginesDTO.setDisplacement(998);
//        enginesDTO.setHorsepower(200);
//        enginesDTO.setTorque(112);
//
//        enginesMapper.insertEnginesData(enginesDTO);
//        assertNotNull(enginesDTO.getMotorcycleID(), "Engines ID가 생성되지 않았습니다.");
//
//        // 로그 출력
//        System.out.println("Inserted Engines Motorcycle ID: " + enginesDTO.getMotorcycleID());
//        System.out.println("Displacement: " + enginesDTO.getDisplacement());
//        System.out.println("Horsepower: " + enginesDTO.getHorsepower());
//    }
//
//    private void insertAndVerifyFrames(Motorcycle motorcycle) {
//        FramesDTO framesDTO = new FramesDTO();
//        framesDTO.setMotorcycleID(motorcycle.getMotorcycleID());
//        framesDTO.setFrameType("Aluminium");
//        framesDTO.setRakeAngle(24.0);
//
//        framesMapper.insertFramesData(framesDTO);
//        assertNotNull(framesDTO.getMotorcycleID(), "Frames ID가 생성되지 않았습니다.");
//
//        // 로그 출력
//        System.out.println("Inserted Frames Motorcycle ID: " + framesDTO.getMotorcycleID());
//        System.out.println("Frame Type: " + framesDTO.getFrameType());
//        System.out.println("Rake Angle: " + framesDTO.getRakeAngle());
//    }
//
//    private void insertAndVerifyTransmissions(Motorcycle motorcycle) {
//        TransmissionsDTO transmissionsDTO = new TransmissionsDTO();
//        transmissionsDTO.setMotorcycleID(motorcycle.getMotorcycleID());
//        transmissionsDTO.setTransmissionType("6-speed");
//        transmissionsDTO.setFinalDrive("Chain");
//
//        transmissionsMapper.insertTransmissionsData(transmissionsDTO);
//        assertNotNull(transmissionsDTO.getMotorcycleID(), "Transmissions ID가 생성되지 않았습니다.");
//
//        // 로그 출력
//        System.out.println("Inserted Transmissions Motorcycle ID: " + transmissionsDTO.getMotorcycleID());
//        System.out.println("Transmission Type: " + transmissionsDTO.getTransmissionType());
//        System.out.println("Final Drive: " + transmissionsDTO.getFinalDrive());
//    }
//
//    private void retrieveAndPrintFullMotorcycleData(Long motorcycleID) {
//        // MotorcycleMapper를 이용해 모든 데이터를 조회
//        MotorcycleDTO motorcycleDTO = MotorcycleDTO.fromDomain(motorcycleMapper.findByMotorcycleId(motorcycleID));
//        assertNotNull(motorcycleDTO, "Motorcycle 데이터를 조회할 수 없습니다.");
//
//        // 조회된 데이터 출력
//        System.out.println("Full Motorcycle Data:");
//        System.out.println("Motorcycle ID: " + motorcycleDTO.getMotorcycleID());
//        System.out.println("Brand: " + motorcycleDTO.getBrand());
//        System.out.println("Model: " + motorcycleDTO.getModel());
//        System.out.println("Years: " + motorcycleDTO.getYears());
//        System.out.println("Production: " + motorcycleDTO.getProduction());
//
//        // 각 서브 데이터 출력
//        if (motorcycleDTO.getDimensions() != null) {
//            System.out.println("Dimensions - Length: " + motorcycleDTO.getDimensions().getDimensions());
//            System.out.println("Dimensions - Seat Height: " + motorcycleDTO.getDimensions().getSeatHeight());
//        }
//        if (motorcycleDTO.getElectronics() != null) {
//            System.out.println("Electronics - ABS: " + motorcycleDTO.getElectronics().isAbs());
//            System.out.println("Electronics - Ride Modes: " + motorcycleDTO.getElectronics().getRideModes());
//        }
//        if (motorcycleDTO.getEngines() != null) {
//            System.out.println("Engines - Displacement: " + motorcycleDTO.getEngines().getDisplacement());
//            System.out.println("Engines - Horsepower: " + motorcycleDTO.getEngines().getHorsepower());
//        }
//        if (motorcycleDTO.getFrames() != null) {
//            System.out.println("Frames - Frame Type: " + motorcycleDTO.getFrames().getFrameType());
//            System.out.println("Frames - Rake Angle: " + motorcycleDTO.getFrames().getRakeAngle());
//        }
//        if (motorcycleDTO.getTransmissions() != null) {
//            System.out.println("Transmissions - Type: " + motorcycleDTO.getTransmissions().getTransmissionType());
//            System.out.println("Transmissions - Final Drive: " + motorcycleDTO.getTransmissions().getFinalDrive());
//        }
//    }
//}
