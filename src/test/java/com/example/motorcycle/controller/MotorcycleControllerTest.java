package com.example.motorcycle.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class MotorcycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateMotorcycle() throws Exception {
        // Test for creating a motorcycle (from create.html)
        mockMvc.perform(post("/motorcycle/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("brand", "Honda")
                        .param("model", "CBR600RR")
                        .param("years", "2022")
                        .param("production", "100")
                        .param("replica", "0")
                        .param("cruiser", "1")
                        .param("tourer", "1")
                        .param("adventure", "0")
                        .param("multiPurpose", "0")
                        .param("naked", "1")
                        .param("cafeRacer", "0")
                        .param("scrambler", "0")
                        .param("offRoad", "1")
                        .param("motard", "0")
                        .param("trial", "0")
                        .param("scooter", "1")
                        .param("classic", "0")
                        .param("dimensionsDTO.dimensions", "200")
                        .param("dimensionsDTO.seatHeight", "80")
                        .param("dimensionsDTO.wheelbase", "140")
                        .param("dimensionsDTO.groundClearance", "14")
                        .param("dimensionsDTO.dryWeight", "180")
                        .param("dimensionsDTO.wetWeight", "200")
                        .param("dimensionsDTO.fuelCapacity", "16")
                        .param("dimensionsDTO.innerLegCurve", "1.2")
                        .param("dimensionsDTO.permittedTotalWeight", "350")
                        .param("electronicsDTO.engineManagement", "ECU")
                        .param("electronicsDTO.emissionControl", "Catalytic Converter")
                        .param("electronicsDTO.engineControl", "EFI")
                        .param("electronicsDTO.alternator", "450W")
                        .param("electronicsDTO.battery", "12V")
                        .param("electronicsDTO.headlight", "LED")
                        .param("electronicsDTO.ignition", "Electronic")
                        .param("electronicsDTO.starting", "Electric Start")
                        .param("enginesDTO.engine", "Inline-4")
                        .param("enginesDTO.capacity", "599")
                        .param("enginesDTO.boreStroke", "67 x 42.5 mm")
                        .param("enginesDTO.compressionRatio", "12.2:1")
                        .param("enginesDTO.coolingSystem", "Liquid-cooled")
                        .param("enginesDTO.lubrication", "Wet Sump")
                        .param("enginesDTO.maxPower", "121")
                        .param("enginesDTO.maxTorque", "66")
                        .param("enginesDTO.fuelSystem", "Fuel Injection")
                        .param("enginesDTO.exhaust", "4-2-1")
                        .param("framesDTO.frame", "Twin Spar")
                        .param("framesDTO.frontSuspension", "41mm USD Forks")
                        .param("framesDTO.rearSuspension", "Monoshock")
                        .param("framesDTO.frontWheelTravel", "120")
                        .param("framesDTO.rearWheelTravel", "130")
                        .param("transmissionsDTO.transmissionDrive", "Chain")
                        .param("transmissionsDTO.transmission", "6-speed")
                        .param("transmissionsDTO.finalDrive", "O-Ring Chain"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testEditMotorcycle() throws Exception {
        // Test for editing a motorcycle (from edit.html)
        mockMvc.perform(post("/motorcycle/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("motorcycleID", "1")
                        .param("brand", "Yamaha")
                        .param("model", "YZF-R1")
                        .param("years", "2021")
                        .param("production", "50"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testEditListMotorcycle() throws Exception {
        // Test for editing multiple motorcycles (from editList.html)
        mockMvc.perform(post("/motorcycle/edit/multiple")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("motorcycles[0].motorcycleID", "1")
                        .param("motorcycles[0].brand", "Yamaha")
                        .param("motorcycles[0].model", "YZF-R1")
                        .param("motorcycles[0].years", "2021")
                        .param("motorcycles[1].motorcycleID", "2")
                        .param("motorcycles[1].brand", "Honda")
                        .param("motorcycles[1].model", "CBR600RR"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSingleSearch() throws Exception {
        // Test for searching a motorcycle by ID (from motorcycle.html)
        mockMvc.perform(post("/motorcycle/singleSearchID")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("motorcycleID", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
