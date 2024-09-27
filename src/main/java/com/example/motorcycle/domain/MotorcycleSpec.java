package com.example.motorcycle.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MotorcycleSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String brand;
    private String model;
    private Long years;
    private Long engine;
    private Long capacity;
    private Long wheelbase;
    private Long fuel_catacity;
}
