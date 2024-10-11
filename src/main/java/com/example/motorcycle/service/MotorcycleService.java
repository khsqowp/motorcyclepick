package com.example.motorcycle.service;

import com.example.motorcycle.domain.*;
import com.example.motorcycle.dto.*;  // 추가: DTO 사용을 위해 임포트
import com.example.motorcycle.form.MotorcycleForm;
import com.example.motorcycle.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotorcycleService {

    @Autowired
    private MotorcycleMapper motorcycleMapper;
    @Autowired
    private DimensionsMapper dimensionsMapper;
    @Autowired
    private ElectronicsMapper electronicsMapper;
    @Autowired
    private FramesMapper framesMapper;
    @Autowired
    private EnginesMapper enginesMapper;
    @Autowired
    private TransmissionsMapper transmissionsMapper;

    // Motorcycle 데이터 삽입
    @Transactional  // 데이터 삽입을 하나의 트랜잭션으로 처리
    public void insertFullMotorcycle(MotorcycleForm form) { // 수정: Form 사용
        MotorcycleDTO motorcycleDTO = form.toDTO();  // Form을 DTO로 변환
        Motorcycle motorcycle = Motorcycle.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환
        motorcycleMapper.insertMotorcycleData(motorcycle);  // Motorcycle 데이터베이스에 삽입

        Long motorcycleID = motorcycle.getMotorcycleID();  // 삽입된 ID를 받아옴

        // 각 서브 데이터 삽입 (motorcycleID 연결)
        if (motorcycleDTO.getDimensionsDTO() != null) {
            Dimensions dimensions = Dimensions.fromDTO(motorcycleDTO.getDimensionsDTO());
            dimensions.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            dimensionsMapper.insertDimensionsData(dimensions);  // Dimensions 데이터베이스에 삽입
        }
        if (motorcycleDTO.getElectronicsDTO() != null) {
            Electronics electronics = Electronics.fromDTO(motorcycleDTO.getElectronicsDTO());
            electronics.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            electronicsMapper.insertElectronicsData(electronics);  // Electronics 데이터베이스에 삽입
        }
        if (motorcycleDTO.getFramesDTO() != null) {
            Frames frames = Frames.fromDTO(motorcycleDTO.getFramesDTO());
            frames.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            framesMapper.insertFramesData(frames);  // Frames 데이터베이스에 삽입
        }
        if (motorcycleDTO.getEnginesDTO() != null) {
            Engines engines = Engines.fromDTO(motorcycleDTO.getEnginesDTO());
            engines.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            enginesMapper.insertEnginesData(engines);  // Engines 데이터베이스에 삽입
        }
        if (motorcycleDTO.getTransmissionsDTO() != null) {
            Transmissions transmissions = Transmissions.fromDTO(motorcycleDTO.getTransmissionsDTO());
            transmissions.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            transmissionsMapper.insertTransmissionsData(transmissions);  // Transmissions 데이터베이스에 삽입
        }
    }

    // Motorcycle 데이터 업데이트
    @Transactional  // 데이터 업데이트를 하나의 트랜잭션으로 처리
    public void updateFullMotorcycle(MotorcycleForm form) {
        MotorcycleDTO motorcycleDTO = form.toDTO();  // Form을 DTO로 변환
        Motorcycle motorcycle = Motorcycle.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환
        motorcycleMapper.updateMotorcycle(motorcycle);  // Motorcycle 데이터베이스 업데이트

        Long motorcycleID = motorcycle.getMotorcycleID();  // 업데이트할 Motorcycle ID

        // 각 서브 데이터 업데이트 (motorcycleID 연결)
        if (motorcycleDTO.getDimensionsDTO() != null) {
            Dimensions dimensions = Dimensions.fromDTO(motorcycleDTO.getDimensionsDTO());
            dimensions.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            dimensionsMapper.updateDimensions(motorcycleID, dimensions);  // Dimensions 데이터베이스 업데이트
        }
        if (motorcycleDTO.getElectronicsDTO() != null) {
            Electronics electronics = Electronics.fromDTO(motorcycleDTO.getElectronicsDTO());
            electronics.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            electronicsMapper.updateElectronics(motorcycleID, electronics);  // Electronics 데이터베이스 업데이트
        }
        if (motorcycleDTO.getFramesDTO() != null) {
            Frames frames = Frames.fromDTO(motorcycleDTO.getFramesDTO());
            frames.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            framesMapper.updateFrames(motorcycleID, frames);  // Frames 데이터베이스 업데이트
        }
        if (motorcycleDTO.getEnginesDTO() != null) {
            Engines engines = Engines.fromDTO(motorcycleDTO.getEnginesDTO());
            engines.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            enginesMapper.updateEngines(motorcycleID, engines);  // Engines 데이터베이스 업데이트
        }
        if (motorcycleDTO.getTransmissionsDTO() != null) {
            Transmissions transmissions = Transmissions.fromDTO(motorcycleDTO.getTransmissionsDTO());
            transmissions.setMotorcycleID(motorcycleID);  // motorcycleID 설정
            transmissionsMapper.updateTransmissions(motorcycleID, transmissions);  // Transmissions 데이터베이스 업데이트
        }
    }

    // Motorcycle 데이터 삭제
    @Transactional  // 데이터 삭제를 하나의 트랜잭션으로 처리
    public void deleteFullMotorcycle(Long motorcycleID) {
        // 각 서브 데이터 삭제
        dimensionsMapper.deleteDimensions(motorcycleID);  // Dimensions 데이터 삭제
        electronicsMapper.deleteElectronics(motorcycleID);  // Electronics 데이터 삭제
        framesMapper.deleteFrames(motorcycleID);  // Frames 데이터 삭제
        enginesMapper.deleteEngines(motorcycleID);  // Engines 데이터 삭제
        transmissionsMapper.deleteTransmissions(motorcycleID);  // Transmissions 데이터 삭제
        motorcycleMapper.deleteMotorcycle(motorcycleID);  // Motorcycle 데이터 삭제
    }

    // Motorcycle 데이터 조회 (단일)
    public MotorcycleDTO findFullMotorcycle(Long motorcycleID) {
        Motorcycle motorcycle = motorcycleMapper.findByMotorcycleId(motorcycleID);  // Motorcycle 데이터베이스에서 조회
        if (motorcycle != null) {
            motorcycle.setDimensions(dimensionsMapper.findByMotorcycleId(motorcycleID));  // Dimensions 조회
            motorcycle.setElectronics(electronicsMapper.findByMotorcycleId(motorcycleID));  // Electronics 조회
            motorcycle.setFrames(framesMapper.findByMotorcycleId(motorcycleID));  // Frames 조회
            motorcycle.setEngines(enginesMapper.findByMotorcycleId(motorcycleID));  // Engines 조회
            motorcycle.setTransmissions(transmissionsMapper.findByMotorcycleId(motorcycleID));  // Transmissions 조회
        }
        return MotorcycleDTO.fromDomain(motorcycle);  // 도메인 객체를 DTO로 변환하여 반환
    }

    // Motorcycle 데이터 조회 (리스트)
    public List<MotorcycleDTO> findFullMotorcycleList() {
        List<Motorcycle> motorcycles = motorcycleMapper.findAll();  // 모든 Motorcycle 데이터를 조회
        return motorcycles.stream()
                .map(MotorcycleDTO::fromDomain)  // 도메인 객체를 DTO로 변환
                .collect(Collectors.toList());  // 리스트로 반환
    }
}
