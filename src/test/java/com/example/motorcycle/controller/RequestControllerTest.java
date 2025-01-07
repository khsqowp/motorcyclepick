//package com.example.motorcycle.controller;
//
//import com.example.motorcycle.dto.MotorcycleDTO;
//import com.example.motorcycle.service.MotorcycleService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.mockito.Mockito.when;
//
//@WebMvcTest(MotorcycleController.class)
//public class RequestControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MotorcycleService motorcycleService;
//
//    @Test
//    public void shouldFetchAllMotorcycleData() throws Exception {
//        // MotorcycleDTO 객체 생성 및 데이터 설정
//        MotorcycleDTO motorcycleDTO = new MotorcycleDTO();
//        motorcycleDTO.setMotorcycleID(1L);
//        motorcycleDTO.setBrand("Yamaha");
//        motorcycleDTO.setModel("YZF-R1");
//        motorcycleDTO.setYears(2021L);
//        motorcycleDTO.setProduction("Japan");
//
//        // 각 서브 테이블의 데이터는 MotorcycleDTO에서 이미 연관됨 (생략된 부분)
//        // 즉, MotorcycleDTO 내부에 dimensionsDTO, electronicsDTO, enginesDTO, 등 정보가 포함된 상태
//
//        // Mocking Service Layer
//        when(motorcycleService.findOneMotorcycle(1L)).thenReturn(motorcycleDTO);
//
//        // 실제 컨트롤러 호출과 검증
//        mockMvc.perform(get("/motorcycle/list")
//                        .accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("list"));
//
//        // 로그 출력 (테스트 중 데이터를 확인)
//        System.out.println("Motorcycle Brand: " + motorcycleDTO.getBrand());
//        System.out.println("Motorcycle Model: " + motorcycleDTO.getModel());
//        System.out.println("Dimensions: " + motorcycleDTO.getDimensionsDTO());
//        System.out.println("Electronics: " + motorcycleDTO.getElectronicsDTO());
//        System.out.println("Engines: " + motorcycleDTO.getEnginesDTO());
//        System.out.println("Frames: " + motorcycleDTO.getFramesDTO());
//        System.out.println("Transmissions: " + motorcycleDTO.getTransmissionsDTO());
//    }
//}
