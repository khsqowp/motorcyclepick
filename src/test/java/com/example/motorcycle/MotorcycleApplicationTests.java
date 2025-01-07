package com.example.motorcycle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MotorcycleApplicationTests {
	@Test
	void contextLoads() {
		assert(true);
	}
}