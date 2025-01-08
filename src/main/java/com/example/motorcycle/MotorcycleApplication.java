package com.example.motorcycle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.motorcycle.repository")
public class MotorcycleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotorcycleApplication.class, args);
	}

}
