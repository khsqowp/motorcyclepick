//package com.example.motorcycle.controller;
//
//import com.example.motorcycle.domain.DimensionsDomain;
//import com.example.motorcycle.domain.ElectronicsDomain;
//import com.example.motorcycle.domain.EnginesDomain;
//import com.example.motorcycle.domain.MotorcycleDomain;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class TestController {
//
//    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
//
//    @GetMapping("/test-result")  // URL을 /test/resultPage 에서 /test-result로 변경
//    public String testResultPage(Model model) {
//        logger.info("testResultPage called");
//
//        // Create test motorcycle data
//        MotorcycleDomain motorcycle = createTestMotorcycle();
//
//        // Add to model
//        model.addAttribute("motorcycle", motorcycle);
//        model.addAttribute("currentIndex", 0);
//        model.addAttribute("totalResults", 1);
//
//        logger.info("Test data added to model");
//        return "resultPage";
//    }
//
//    private MotorcycleDomain createTestMotorcycle() {
//        MotorcycleDomain motorcycle = new MotorcycleDomain();
//
//        // Basic motorcycle information
//        motorcycle.setMotorcycleID(1L);
//        motorcycle.setBrand("Honda");
//        motorcycle.setModel("CBR1000RR-R");
//        motorcycle.setYears(2024F);
//        motorcycle.setPrice(2890F);
//
//        // Motorcycle types (설정된 값은 해당 타입의 특성을 가지고 있음을 나타냄)
//        motorcycle.setReplica(1F);      // 레플리카
//        motorcycle.setCruiser(0F);      // 크루저
//        motorcycle.setTourer(0F);       // 투어러
//        motorcycle.setAdventure(0F);    // 어드벤처
//        motorcycle.setMultiPurpose(0F); // 다목적
//        motorcycle.setNaked(0F);        // 네이키드
//        motorcycle.setScrambler(0F);    // 스크램블러
//        motorcycle.setOffRoad(0F);      // 오프로드
//        motorcycle.setMotard(0F);       // 모타드
//        motorcycle.setTrial(0F);        // 트라이얼
//        motorcycle.setScooter(0F);      // 스쿠터
//        motorcycle.setClassic(0F);      // 클래식
//        motorcycle.setCafeRacer(0F);    // 카페레이서
//
//        // Create and set Engines domain
//        EnginesDomain engines = new EnginesDomain();
//        engines.setEnginesID(1L);
//        engines.setMotorcycleID(1L);
//        engines.setCapacity(999F);
//        engines.setBoreStroke("81 x 48.5");
//        engines.setCompressionRatio("13.0:1");
//        engines.setCoolingSystem("수랭식");
//        engines.setLubrication("Wet sump");
//        engines.setFuelSystem("PGM-DSFI with 52mm throttle bodies");
//        engines.setEmission("Euro 5");
//        engines.setInduction("RAM Air");
//        engines.setMileage(16.5F);
//        engines.setTopSpeed(299F);
//        engines.setClutch("Wet, multi-plate");
//        engines.setTransmissionGearCount("6단");
//        engines.setTransmissionType("수동");
//        engines.setEngineStroke(4F);
//        engines.setEngineCylinder(4F);
//        engines.setEngineCamshaft("DOHC");
//        engines.setEngineType("직렬 4기통");
//        engines.setEngineCrankAngle("180도");
//        engines.setMaxPowerHp(217F);
//        engines.setMaxPowerRpm(14500F);
//        engines.setMaxTorqueNm(113F);
//        engines.setMaxTorqueRpm(12500F);
//        engines.setClassGrade("대형");
//
//        // Create and set Dimensions domain
//        DimensionsDomain dimensions = new DimensionsDomain();
//        dimensions.setDimensionsID(1L);
//        dimensions.setMotorcycleID(1L);
//        dimensions.setLength(2100F);
//        dimensions.setWidth(750F);
//        dimensions.setHeight(1140F);
//        dimensions.setSeatHeight(830F);
//        dimensions.setWheelbase(1460F);
//        dimensions.setGroundClearance(115F);
//        dimensions.setWetWeight(201F);
//        dimensions.setFuelCapacity(16.1F);
//        dimensions.setFrameType("Twin-spar");
//        dimensions.setFrameMaterial("알루미늄");
//        dimensions.setFrontBrakeDiscCount("4");
//        dimensions.setFrontBrakeDiscType("디스크");
//        dimensions.setFrontBrakeDiscSize(330F);
//        dimensions.setRearBrakeDiscType("디스크");
//        dimensions.setRearBrakeDiscSize(220F);
//        dimensions.setFrontTyreWidth(120F);
//        dimensions.setFrontTyreAspectRatio(70F);
//        dimensions.setFrontTyreDiameter(17F);
//        dimensions.setRearTyreWidth(190F);
//        dimensions.setRearTyreAspectRatio(55F);
//        dimensions.setRearTyreDiameter(17F);
//
//        // Create and set Electronics domain
//        ElectronicsDomain electronics = new ElectronicsDomain();
//        electronics.setElectronicsID(1L);
//        electronics.setMotorcycleID(1L);
//        electronics.setStartSystem("전동");
//        electronics.setAbs(true);
//        electronics.setTcs(true);
//        electronics.setCruiseControl(true);
//        electronics.setAssistSlipperClutch(true);
//        electronics.setElectricScreen(false);
//        electronics.setClutchAssistSystem(true);
//        electronics.setImu(true);
//        electronics.setCorneringAbs(true);
//        electronics.setWheelieControl(true);
//        electronics.setRidingModeChange(true);
//        electronics.setBankingAngleDisplay(true);
//        electronics.setAbsLevelControl(true);
//        electronics.setQuickshiftUp(true);
//        electronics.setQuickshiftUpDown(true);
//
//        // Set all domains to motorcycle
//        motorcycle.setEnginesDomain(engines);
//        motorcycle.setDimensionsDomain(dimensions);
//        motorcycle.setElectronicsDomain(electronics);
//
//        return motorcycle;
//    }
//}