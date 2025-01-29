package com.example.motorcyclepick;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.motorcyclepick.repository")
public class MotorcyclepickApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotorcyclepickApplication.class, args);
	}

}
