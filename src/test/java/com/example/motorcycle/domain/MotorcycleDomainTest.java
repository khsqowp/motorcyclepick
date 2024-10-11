package com.example.motorcycle.domain;

import com.example.motorcycle.domain.Motorcycle;
import com.example.motorcycle.dto.MotorcycleDTO;
import com.example.motorcycle.repository.MotorcycleMapper;
import com.example.motorcycle.service.MotorcycleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MotorcycleDomainTest {

    private static final Logger logger = LoggerFactory.getLogger(MotorcycleDomainTest.class);

    @Mock
    private MotorcycleMapper motorcycleMapper;

    @InjectMocks
    private MotorcycleService motorcycleService;

    @BeforeEach
    @Transactional
    public void setUp() {
        Motorcycle motorcycle1 = new Motorcycle();
        motorcycle1.setBrand("Honda");
        motorcycle1.setModel("CBR600RR");
        motorcycle1.setYears(2024L);

        // Dimensions, Electronics, Engines, Frames, Transmissions 데이터를 추가
        Dimensions dimensions = new Dimensions();
        dimensions.setMotorcycleID(motorcycle1.getMotorcycleID());
        dimensions.setSeatHeight(820F);

        Electronics electronics = new Electronics();
        electronics.setMotorcycleID(motorcycle1.getMotorcycleID());
        electronics.setEngineManagement("ECU");

        Engines engines = new Engines();
        engines.setMotorcycleID(motorcycle1.getMotorcycleID());
        engines.setCapacity(600F);

        Frames frames = new Frames();
        frames.setMotorcycleID(motorcycle1.getMotorcycleID());
        frames.setFrame("Aluminium");

        Transmissions transmissions = new Transmissions();
        transmissions.setMotorcycleID(motorcycle1.getMotorcycleID());
        transmissions.setTransmission("Manual 6-speed");

        motorcycle1.setDimensions(dimensions);
        motorcycle1.setElectronics(electronics);
        motorcycle1.setEngines(engines);
        motorcycle1.setFrames(frames);
        motorcycle1.setTransmissions(transmissions);

        motorcycleMapper.insertMotorcycleData(motorcycle1);
    }

    @Test
    public void testGetMotorcycleById() {
        // 가짜 데이터 설정
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setMotorcycleID(1L);
        motorcycle.setBrand("Honda");
        motorcycle.setModel("CBR600RR");
        motorcycle.setYears(2024L);

        // 연관 도메인 데이터 추가
        Dimensions dimensions = new Dimensions();
        dimensions.setMotorcycleID(motorcycle.getMotorcycleID());
        dimensions.setSeatHeight(820F);
        motorcycle.setDimensions(dimensions);

        Electronics electronics = new Electronics();
        electronics.setMotorcycleID(motorcycle.getMotorcycleID());
        electronics.setEngineManagement("ECU");
        motorcycle.setElectronics(electronics);

        Engines engines = new Engines();
        engines.setMotorcycleID(motorcycle.getMotorcycleID());
        engines.setCapacity(600F);
        motorcycle.setEngines(engines);

        Frames frames = new Frames();
        frames.setMotorcycleID(motorcycle.getMotorcycleID());
        frames.setFrame("Aluminium");
        motorcycle.setFrames(frames);

        Transmissions transmissions = new Transmissions();
        transmissions.setMotorcycleID(motorcycle.getMotorcycleID());
        transmissions.setTransmission("Manual 6-speed");
        motorcycle.setTransmissions(transmissions);

        // 레포지토리에서 데이터를 가져올 때 반환될 값을 미리 정의
        when(motorcycleMapper.findByMotorcycleId(1L)).thenReturn(Optional.of(motorcycle));

        // 서비스에서 데이터를 가져옴
        MotorcycleDTO result = motorcycleService.findByIdAsDTO(1L);

        // 각 필드가 정상적으로 매핑되는지 확인 (assertions)
        assertNotNull(result);
        assertEquals("Honda", result.getBrand());
        assertEquals("CBR600RR", result.getModel());
        assertEquals(2024, result.getYears());

        // 로그로 출력
        logger.info("Motorcycle ID: {}", result.getMotorcycleID());
        logger.info("Brand: {}", result.getBrand());
        logger.info("Model: {}", result.getModel());
        logger.info("Years: {}", result.getYears());

        // 연관 데이터 로그로 출력
        logger.info("Dimensions - Seat Height: {}", result.getDimensions().getSeatHeight());
        logger.info("Electronics - Engine Management: {}", result.getElectronics().getEngineManagement());
        logger.info("Engines - Capacity: {}", result.getEngines().getCapacity());
        logger.info("Frames - Frame: {}", result.getFrames().getFrame());
        logger.info("Transmissions - Transmission: {}", result.getTransmissions().getTransmission());

        // Repository 메소드 호출 여부 검증
        verify(motorcycleMapper, times(1)).findById(1L);
    }
}
