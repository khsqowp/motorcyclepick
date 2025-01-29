//package com.example.motorcycle;
//
//import com.example.motorcycle.domain.Motorcycle;
//import com.example.motorcycle.dto.MotorcycleDTO;
//import com.example.motorcycle.repository.MotorcycleRepository;
//import com.example.motorcycle.service.MotorcycleService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class MotorcycleIntegrationTest {
//
//    @Autowired
//    private MotorcycleService motorcycleService;
//
//    @Autowired
//    private MotorcycleRepository motorcycleRepository;
//
//    @Test
//    public void testFindAndConvertToDTO() {
//        Motorcycle motorcycle = motorcycleRepository.findById(1L).orElseThrow();
//        MotorcycleDTO dto = motorcycleService.convertToDTO(motorcycle);
//
//        assertEquals(motorcycle.getMotorcycleID(), dto.getMotorcycleID());
//        // 추가적인 필드 매핑 확인
//    }
//}