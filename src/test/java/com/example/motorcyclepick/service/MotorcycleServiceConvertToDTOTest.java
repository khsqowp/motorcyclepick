//package com.example.motorcycle.service;
//
//import com.example.motorcycle.domain.*;
//import com.example.motorcycle.dto.MotorcycleDTO;
//import com.example.motorcycle.dto.DimensionsDTO;
//import com.example.motorcycle.dto.ElectronicsDTO;
//import com.example.motorcycle.dto.EnginesDTO;
//import com.example.motorcycle.dto.FramesDTO;
//import com.example.motorcycle.dto.TransmissionsDTO;
//import com.example.motorcycle.repository.*;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class MotorcycleServiceConvertToDTOTest {
//
//    @InjectMocks
//    private MotorcycleService motorcycleService;
//
//    @Mock
//    private MotorcycleRepository motorcycleRepository;
//
//    @Mock
//    private DimensionsRepository dimensionsRepository;
//
//    @Mock
//    private ElectronicsRepository electronicsRepository;
//
//    @Mock
//    private EnginesRepository enginesRepository;
//
//    @Mock
//    private FramesRepository framesRepository;
//
//    @Mock
//    private TransmissionsRepository transmissionsRepository;
//
//    public MotorcycleServiceConvertToDTOTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testConvertToDTO() {
//        // Motorcycle 엔티티 설정
//        Motorcycle motorcycle = new Motorcycle();
//        motorcycle.setMotorcycleID(1L);
//        motorcycle.setBrand("Honda");
//        motorcycle.setModel("CBR600RR");
//        motorcycle.setYears(2024L);
//
//        // Dimensions 설정
//        Dimensions dimensions = new Dimensions();
//        dimensions.setId(1L);
//        dimensions.setSeatHeight(512F);
//        dimensions.setWheelbase(512F);
//        motorcycle.setDimensions(dimensions);
//
//        // Electronics 설정
//        Electronics electronics = new Electronics();
//        electronics.setId(1L);
//        electronics.setEngineManagement("EMS");
//        motorcycle.setElectronics(electronics);
//
//        // Engines 설정
//        Engines engines = new Engines();
//        engines.setId(1L);
//        engines.setEngine("4-cylinder");
//        engines.setCapacity(600.0f);  // 여기에서 capacity에 값을 설정
//        motorcycle.setEngines(engines);
//
//
//        // Frames 설정
//        Frames frames = new Frames();
//        frames.setId(1L);
//        frames.setFrame("Alloy");
//        motorcycle.setFrames(frames);
//
//        // Transmissions 설정
//        Transmissions transmissions = new Transmissions();
//        transmissions.setId(1L);
//        transmissions.setTransmission("6-speed");
//        motorcycle.setTransmissions(transmissions);
//
//        // Mockito 설정
//        when(motorcycleRepository.findById(1L)).thenReturn(Optional.of(motorcycle));
//
//        // Motorcycle을 DTO로 변환
//        MotorcycleDTO motorcycleDTO = motorcycleService.convertToDTO(motorcycle);
//
//        // Assertions로 변환된 DTO가 정상적인지 확인
//        assertEquals("Honda", motorcycleDTO.getBrand());
//        assertEquals("CBR600RR", motorcycleDTO.getModel());
//        assertEquals(2024L, motorcycleDTO.getYears());
//
//        // Dimensions DTO 확인
//        assertNotNull(motorcycleDTO.getDimensionsDTO());
//        DimensionsDTO dimensionsDTO = motorcycleDTO.getDimensionsDTO();
//        assertEquals(32, dimensionsDTO.getSeatHeight());
//        assertEquals(56, dimensionsDTO.getWheelbase());
//
//        // Electronics DTO 확인
//        assertNotNull(motorcycleDTO.getElectronicsDTO());
//        ElectronicsDTO electronicsDTO = motorcycleDTO.getElectronicsDTO();
//        assertEquals("EMS", electronicsDTO.getEngineManagement());
//
//        // Engines DTO 확인
//        assertNotNull(motorcycleDTO.getEnginesDTO());
//        EnginesDTO enginesDTO = motorcycleDTO.getEnginesDTO();
//        assertEquals("4-cylinder", enginesDTO.getEngine());
//
//        // Frames DTO 확인
//        assertNotNull(motorcycleDTO.getFramesDTO());
//        FramesDTO framesDTO = motorcycleDTO.getFramesDTO();
//        assertEquals("Alloy", framesDTO.getFrame());
//
//        // Transmissions DTO 확인
//        assertNotNull(motorcycleDTO.getTransmissionsDTO());
//        TransmissionsDTO transmissionsDTO = motorcycleDTO.getTransmissionsDTO();
//        assertEquals("6-speed", transmissionsDTO.getTransmission());
//
//        // 모든 데이터가 정상적으로 출력되는지 로그로 확인
//        System.out.println("MotorcycleDTO: " + motorcycleDTO.getBrand() + ", " + motorcycleDTO.getModel());
//        System.out.println("DimensionsDTO: " + dimensionsDTO.getSeatHeight() + ", " + dimensionsDTO.getWheelbase());
//        System.out.println("ElectronicsDTO: " + electronicsDTO.getEngineManagement());
//        System.out.println("EnginesDTO: " + enginesDTO.getEngine());
//        System.out.println("FramesDTO: " + framesDTO.getFrame());
//        System.out.println("TransmissionsDTO: " + transmissionsDTO.getTransmission());
//    }
//}