//package com.example.motorcycle.domain;
//
//import com.example.motorcycle.domain.Motorcycle;
//import com.example.motorcycle.dto.MotorcycleDTO;
//import com.example.motorcycle.repository.MotorcycleRepository;
//import com.example.motorcycle.service.MotorcycleService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class MotorcycleDomainTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(MotorcycleDomainTest.class);
//
//    @Mock
//    private MotorcycleRepository motorcycleRepository;
//
//    @InjectMocks
//    private MotorcycleService motorcycleService;
//
//    @BeforeEach
//    @Transactional
//    public void setUp() {
//        Motorcycle motorcycle1 = new Motorcycle();
//        motorcycle1.setBrand("Honda");
//        motorcycle1.setModel("CBR600RR");
//        motorcycle1.setYears(2024L);
//        motorcycleRepository.save(motorcycle1);
//
//        Motorcycle motorcycle2 = new Motorcycle();
//        motorcycle2.setBrand("Yamaha");
//        motorcycle2.setModel("YZF-R1");
//        motorcycle2.setYears(2023L);
//        motorcycleRepository.save(motorcycle2);
//    }
//
//    @Test
//    public void testGetMotorcycleById() {
//        // 가짜 데이터 설정
//        Motorcycle motorcycle = new Motorcycle();
//        motorcycle.setMotorcycleID(1L);
//        motorcycle.setBrand("Honda");
//        motorcycle.setModel("CBR600RR");
//        motorcycle.setYears(2024L);
//
//        // 레포지토리에서 데이터를 가져올 때 반환될 값을 미리 정의
//        when(motorcycleRepository.findById(1L)).thenReturn(Optional.of(motorcycle));
//
//        // 서비스에서 데이터를 가져옴
//        MotorcycleDTO result = motorcycleService.findByIdAsDTO(1L);
//
//        // 각 필드가 정상적으로 매핑되는지 확인 (assertions)
//        assertNotNull(result);
//        assertEquals("Honda", result.getBrand());
//        assertEquals("CBR600RR", result.getModel());
//        assertEquals(2024, result.getYears());
//
//        // 로그로 출력
//        logger.info("Motorcycle ID: {}", result.getMotorcycleID());
//        logger.info("Brand: {}", result.getBrand());
//        logger.info("Model: {}", result.getModel());
//        logger.info("Years: {}", result.getYears());
//
//        // Repository 메소드 호출 여부 검증
//        verify(motorcycleRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testGetAllMotorcycles() {
//        // 가짜 데이터 리스트 설정
//        Motorcycle motorcycle1 = new Motorcycle();
//        motorcycle1.setMotorcycleID(1L);
//        motorcycle1.setBrand("Honda");
//        motorcycle1.setModel("CBR600RR");
//        motorcycle1.setYears(2024L);
//
//        Motorcycle motorcycle2 = new Motorcycle();
//        motorcycle2.setMotorcycleID(2L);
//        motorcycle2.setBrand("Yamaha");
//        motorcycle2.setModel("YZF-R1");
//        motorcycle2.setYears(2023L);
//
//        // 레포지토리에서 데이터를 가져올 때 반환될 값을 미리 정의
//        when(motorcycleRepository.findAll()).thenReturn(List.of(motorcycle1, motorcycle2));
//
//        // 서비스에서 데이터를 가져옴
//        List<MotorcycleDTO> result = motorcycleService.findAllAsDTO();
//
//        // 각 필드가 정상적으로 매핑되는지 확인
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//        MotorcycleDTO motorcycleDTO1 = result.get(0);
//        MotorcycleDTO motorcycleDTO2 = result.get(1);
//
//        assertEquals("Honda", motorcycleDTO1.getBrand());
//        assertEquals("Yamaha", motorcycleDTO2.getBrand());
//
//        // 로그로 출력
//        logger.info("Motorcycle 1 - ID: {}, Brand: {}, Model: {}", motorcycleDTO1.getMotorcycleID(), motorcycleDTO1.getBrand(), motorcycleDTO1.getModel());
//        logger.info("Motorcycle 2 - ID: {}, Brand: {}, Model: {}", motorcycleDTO2.getMotorcycleID(), motorcycleDTO2.getBrand(), motorcycleDTO2.getModel());
//
//        // Repository 메소드 호출 여부 검증
//        verify(motorcycleRepository, times(1)).findAll();
//    }
//}