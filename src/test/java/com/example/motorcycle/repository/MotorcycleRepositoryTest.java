//package com.example.motorcycle.repository;
//
//import com.example.motorcycle.domain.Motorcycle;
//import com.example.motorcycle.dto.MotorcycleDTO;
//import com.example.motorcycle.service.MotorcycleService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@SpringBootTest
//@Configuration
//public class MotorcycleRepositoryTest {
//
//    @Autowired
//    private MotorcycleRepository motorcycleRepository;
//
//    @Autowired
//    private MotorcycleService motorcycleService;
//
//    @Test
//    public void testFindAll() {
//        List<Motorcycle> motorcycles = motorcycleRepository.findAll();
//        motorcycles.forEach(motorcycle -> System.out.println("Motorcycle: " + motorcycle.getBrand() + ", " + motorcycle.getModel()));
//
//        List<MotorcycleDTO> motorcycleDTOs = motorcycleService.findAllAsDTO();
//        motorcycleDTOs.forEach(dto -> System.out.println("MotorcycleDTO: " + dto.getBrand() + ", " + dto.getModel()));
//    }
//}