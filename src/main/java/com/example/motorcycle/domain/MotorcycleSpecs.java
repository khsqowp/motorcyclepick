//package com.example.motorcycle.domain;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//public class MotorcycleSpecs {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "motorcycleSpecsID")
//    private Long motorcycleSpecsID;
//    private Long id;
//
//    private String production;
//
//    @OneToOne(mappedBy = "motorcycleSpecs")
//    private Motorcycle motorcycle;
//}
