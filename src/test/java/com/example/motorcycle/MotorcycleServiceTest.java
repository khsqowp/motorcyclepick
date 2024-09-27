package com.example.motorcycle;

import com.example.motorcycle.motorcycleData.Motorcycle;
import com.example.motorcycle.motorcycleData.MotorcycleService;
import com.example.motorcycle.motorcycleData.MotorcycleServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MotorcycleServiceTest {

    MotorcycleService motorcycleService = new MotorcycleServiceImpl();

    @Test
    void findMotorcycle(){
        //given
        Motorcycle motorcycle = new Motorcycle("CMX500", "y2024");

        //when
        motorcycleService.findMotorcycle(motorcycle);
        Motorcycle findMotorcycle = MotorcycleService.findMotorcycle("y2024");

        //then
        Assertions.assertThat(motorcycle).isEqualTo(findMotorcycle);
    }
}
