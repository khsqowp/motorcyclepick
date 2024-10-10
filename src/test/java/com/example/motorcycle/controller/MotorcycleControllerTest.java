//package com.example.motorcycle.controller;
//
//import com.example.motorcycle.domain.*;
//import com.example.motorcycle.dto.MotorcycleDTO;
//import com.example.motorcycle.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.ui.ConcurrentModel;
//import org.springframework.ui.Model;
//
//import java.util.List;
//
//@SpringBootTest
//@Configuration
//public class MotorcycleControllerTest {
//
//    @Autowired
//    private MotorcycleController motorcycleController;
//
//    @Autowired
//    private MotorcycleRepository motorcycleRepository;
//
//    // 테스트 실행 전에 데이터베이스에 테스트 데이터 삽입
//    @BeforeEach
//    public void setUp() {
//        // Motorcycle 엔티티 생성 및 저장
//        Motorcycle motorcycle = new Motorcycle();
//        motorcycle.setBrand("Honda");
//        motorcycle.setModel("CBR 600RR");
//        motorcycle.setYears(Long.valueOf("2024"));
//        motorcycle.setReplica(95);
//        motorcycle.setCruiser(0);
//        motorcycle.setTourer(15);
//        motorcycle.setAdventure(0);
//        motorcycle.setMultiPurpose(30);
//        motorcycle.setNaked(10);
//        motorcycle.setCafeRacer(0);
//        motorcycle.setScrambler(0);
//        motorcycle.setOffRoad(5);
//        motorcycle.setMotard(0);
//        motorcycle.setTrial(0);
//        motorcycle.setScooter(0);
//        motorcycle.setClassic(0);
//        motorcycleRepository.save(motorcycle); // motorcycle 저장
//    }
//
//    @Test
//    public void testListMotorcycles() {
//        // Model 객체 생성
//        Model model = new ConcurrentModel();
//
//        // Controller 메서드 호출하여 데이터 로드 및 뷰 이름 반환
//        String viewName = motorcycleController.listMotorcycles(model);
//
//        // 뷰 이름 출력
//        System.out.println("View Name: " + viewName);
//
//        // Model에 저장된 'motorcycles' 속성 값 가져오기
//        List<MotorcycleDTO> motorcycleList = (List<MotorcycleDTO>) model.getAttribute("motorcycles");
//
//        if (motorcycleList != null) {
//            // 각 MotorcycleDTO에 대해 모든 필드 출력
//            motorcycleList.forEach(motorcycle -> {
//                System.out.println("Controller DTO: ");
//                System.out.println("Brand: " + motorcycle.getBrand());
//                System.out.println("Model: " + motorcycle.getModel());
//                System.out.println("Years: " + motorcycle.getYears());
//                System.out.println("Replica: " + motorcycle.getReplica());
//                System.out.println("Cruiser: " + motorcycle.getCruiser());
//                System.out.println("Tourer: " + motorcycle.getTourer());
//                System.out.println("Adventure: " + motorcycle.getAdventure());
//                System.out.println("MultiPurpose: " + motorcycle.getMultiPurpose());
//                System.out.println("Naked: " + motorcycle.getNaked());
//                System.out.println("CafeRacer: " + motorcycle.getCafeRacer());
//                System.out.println("Scrambler: " + motorcycle.getScrambler());
//                System.out.println("OffRoad: " + motorcycle.getOffRoad());
//                System.out.println("Motard: " + motorcycle.getMotard());
//                System.out.println("Trial: " + motorcycle.getTrial());
//                System.out.println("Scooter: " + motorcycle.getScooter());
//                System.out.println("Classic: " + motorcycle.getClassic());
//            });
//        } else {
//            System.out.println("Motorcycle list is empty.");
//        }
//    }
//}