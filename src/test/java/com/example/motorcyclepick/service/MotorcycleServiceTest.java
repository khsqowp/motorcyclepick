    //package com.example.motorcycle.service;
    //
    //import com.example.motorcycle.dto.MotorcycleDTO;
    //import com.example.motorcycle.domain.*; // 엔티티 클래스 임포트
    //import org.junit.jupiter.api.Test;
    //import org.springframework.beans.factory.annotation.Autowired;
    //import org.springframework.boot.test.context.SpringBootTest;
    //import org.springframework.context.annotation.Configuration;
    //
    //import java.util.ArrayList;
    //import java.util.List;
    //
    //@SpringBootTest
    //@Configuration
    //public class MotorcycleServiceTest {
    //
    //    @Autowired
    //    private MotorcycleService motorcycleService;
    //
    //    @Test
    //    public void testFindAllAsDTO() {
    //        // 테스트용 데이터 생성
    //        Motorcycle motorcycle = new Motorcycle();
    //        motorcycle.setMotorcycleID(1L);
    //        motorcycle.setBrand("Honda");
    //        motorcycle.setModel("GL 1800 Gold Wing Tour / Automatic-DCT");
    //        motorcycle.setYears(2024L);
    //        motorcycle.setProduction(null);  // 생산 여부가 NULL이므로 NULL 값 그대로 유지
    //
    //        // Engines 설정
    //        Engines engines = new Engines();
    //        engines.setEngine("Four stroke, opposed boxer six cylinders, SOHC, 4-valve per cylinder, finger-follower rocker arm on intake, roller-rocker arm on exhaust");
    //        engines.setCapacity(1833.0f);
    //        engines.setBoreStroke("73 x 73");
    //        engines.setCompressionRatio("10.5:1");
    //        engines.setCoolingSystem("Liquid cooled");
    //        engines.setLubrication(null);  // NULL 값
    //        engines.setMaxPower("93 kW / 124.7 hp @ 5500 rpm");
    //        engines.setMaxTorque("170 Nm @ 4500 rpm");
    //        engines.setFuelSystem("PGM-FI electronic fuel injection, 50mm throttle body");
    //        engines.setExhaust(null);  // NULL 값
    //        engines.setEngineOil(null);  // NULL 값
    //        engines.setMixtureControl(null);  // NULL 값
    //        engines.setEmission(null);  // NULL 값
    //        engines.setInduction(null);  // NULL 값
    //        motorcycle.setEngines(engines);
    //
    //        // Transmissions 설정
    //        Transmissions transmissions = new Transmissions();
    //        transmissions.setClutch("(MT) Hydraulic, wet, multiplate with coil springs, assist slipper cam (DCT) Hydraulic, wet, multiplate with oil pressure");
    //        transmissions.setTransmissionDrive(null);  // NULL 값
    //        transmissions.setTransmission("6 speed MT (including overdrive. Plus electric reverse) 7-speed forward and reverse DCT");
    //        transmissions.setFinalDrive("Enclosed shaft");
    //        motorcycle.setTransmissions(transmissions);
    //
    //        // Frames 설정
    //        Frames frames = new Frames();
    //        frames.setFrame("Aluminum die-cast, twin tube");
    //        frames.setFrontSuspension("Double-wishbone");
    //        frames.setRearSuspension("Pro-Link system with Showa shock absorber");
    //        frames.setFrontWheelTravel((int) 110.0f);
    //        frames.setRearWheelTravel((int) 104.0f);
    //        frames.setFrontBrakes("2x 320mm hydraulic discs 6-piston Nissin calipers, floating rotors and sintered metal pads");
    //        frames.setRearBrakes("Single 316mm ventilated disc 3-piston Nissin caliper, sintered metal pads");
    //        frames.setAbsSystem("Electronically controlled combined ABS system");
    //        frames.setFrontWheel("3.5 x 18");
    //        frames.setRearWheel("6.0 x 16");
    //        frames.setFrontTyre("130/70R 18");
    //        frames.setRearTyre("200/55R 16");
    //        frames.setRake(30.5f);
    //        frames.setTrail(109.0f);
    //        motorcycle.setFrames(frames);
    //
    //        // Dimensions 설정
    //        Dimensions dimensions = new Dimensions();
    //        dimensions.setSeatHeight(745.0f);
    //        dimensions.setWheelbase(1694.0f);
    //        dimensions.setGroundClearance(130.0f);
    //        dimensions.setWetWeight(385.0f);
    //        dimensions.setFuelCapacity(21.1f);
    //        motorcycle.setDimensions(dimensions);
    //
    //        // Electronics 설정
    //        Electronics electronics = new Electronics();
    //        electronics.setIgnition("Computer controlled digital");
    //        electronics.setBattery("12V/20AH");
    //        electronics.setStarting("Integrated Starter Generator system");
    //        motorcycle.setElectronics(electronics);
    //
    //        // MotorcycleService에 데이터를 제공할 수 있도록 리스트에 추가
    //        List<Motorcycle> motorcycleList = new ArrayList<>();
    //        motorcycleList.add(motorcycle);
    //
    //        // Mock 데이터를 이용해 서비스 메서드 호출 후 DTO로 변환된 결과 확인
    //        List<MotorcycleDTO> motorcycles = motorcycleService.convertToDTOList(motorcycleList);
    //        motorcycles.forEach(motorcycleDTO -> {
    //            System.out.println("MotorcycleDTO: " + motorcycleDTO.getBrand() + ", " + motorcycleDTO.getModel());
    //
    //            // DimensionsDTO 출력
    //            if (motorcycleDTO.getDimensionsDTO() != null) {
    //                System.out.println("Dimensions - Seat Height: " +
    //                        (motorcycleDTO.getDimensionsDTO().getSeatHeight() != null ? motorcycleDTO.getDimensionsDTO().getSeatHeight() : "null"));
    //                System.out.println("Dimensions - Wheelbase: " +
    //                        (motorcycleDTO.getDimensionsDTO().getWheelbase() != null ? motorcycleDTO.getDimensionsDTO().getWheelbase() : "null"));
    //                System.out.println("Dimensions - Ground Clearance: " +
    //                        (motorcycleDTO.getDimensionsDTO().getGroundClearance() != null ? motorcycleDTO.getDimensionsDTO().getGroundClearance() : "null"));
    //                System.out.println("Dimensions - Wet Weight: " +
    //                        (motorcycleDTO.getDimensionsDTO().getWetWeight() != null ? motorcycleDTO.getDimensionsDTO().getWetWeight() : "null"));
    //                System.out.println("Dimensions - Fuel Capacity: " +
    //                        (motorcycleDTO.getDimensionsDTO().getFuelCapacity() != null ? motorcycleDTO.getDimensionsDTO().getFuelCapacity() : "null"));
    //            } else {
    //                System.out.println("No DimensionsDTO data available.");
    //            }
    //
    //            // ElectronicsDTO 출력
    //            if (motorcycleDTO.getElectronicsDTO() != null) {
    //                System.out.println("Electronics - Ignition: " +
    //                        (motorcycleDTO.getElectronicsDTO().getIgnition() != null ? motorcycleDTO.getElectronicsDTO().getIgnition() : "null"));
    //                System.out.println("Electronics - Battery: " +
    //                        (motorcycleDTO.getElectronicsDTO().getBattery() != null ? motorcycleDTO.getElectronicsDTO().getBattery() : "null"));
    //                System.out.println("Electronics - Starting: " +
    //                        (motorcycleDTO.getElectronicsDTO().getStarting() != null ? motorcycleDTO.getElectronicsDTO().getStarting() : "null"));
    //            } else {
    //                System.out.println("No ElectronicsDTO data available.");
    //            }
    //
    //            // EnginesDTO 출력
    //            if (motorcycleDTO.getEnginesDTO() != null) {
    //                System.out.println("Engines - Engine: " +
    //                        (motorcycleDTO.getEnginesDTO().getEngine() != null ? motorcycleDTO.getEnginesDTO().getEngine() : "null"));
    //                System.out.println("Engines - Capacity: " +
    //                        (motorcycleDTO.getEnginesDTO().getCapacity() != null ? motorcycleDTO.getEnginesDTO().getCapacity() : "null"));
    //            } else {
    //                System.out.println("No EnginesDTO data available.");
    //            }
    //
    //            // FramesDTO 출력
    //            if (motorcycleDTO.getFramesDTO() != null) {
    //                System.out.println("Frames - Frame: " +
    //                        (motorcycleDTO.getFramesDTO().getFrame() != null ? motorcycleDTO.getFramesDTO().getFrame() : "null"));
    //                System.out.println("Frames - Front Suspension: " +
    //                        (motorcycleDTO.getFramesDTO().getFrontSuspension() != null ? motorcycleDTO.getFramesDTO().getFrontSuspension() : "null"));
    //                System.out.println("Frames - Rear Suspension: " +
    //                        (motorcycleDTO.getFramesDTO().getRearSuspension() != null ? motorcycleDTO.getFramesDTO().getRearSuspension() : "null"));
    //            } else {
    //                System.out.println("No FramesDTO data available.");
    //            }
    //
    //            // TransmissionsDTO 출력
    //            if (motorcycleDTO.getTransmissionsDTO() != null) {
    //                System.out.println("Transmissions - Clutch: " +
    //                        (motorcycleDTO.getTransmissionsDTO().getClutch() != null ? motorcycleDTO.getTransmissionsDTO().getClutch() : "null"));
    //                System.out.println("Transmissions - Transmission: " +
    //                        (motorcycleDTO.getTransmissionsDTO().getTransmission() != null ? motorcycleDTO.getTransmissionsDTO().getTransmission() : "null"));
    //                System.out.println("Transmissions - Final Drive: " +
    //                        (motorcycleDTO.getTransmissionsDTO().getFinalDrive() != null ? motorcycleDTO.getTransmissionsDTO().getFinalDrive() : "null"));
    //            } else {
    //                System.out.println("No TransmissionsDTO data available.");
    //            }
    //        });
    //    }
    //}