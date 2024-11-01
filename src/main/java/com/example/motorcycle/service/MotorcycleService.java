package com.example.motorcycle.service;

import com.example.motorcycle.domain.MotorcycleDomain;
import com.example.motorcycle.dto.MotorcycleDTO;
import com.example.motorcycle.form.MotorcycleForm;
import com.example.motorcycle.repository.MotorcycleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotorcycleService {

    @Autowired
    private MotorcycleMapper motorcycleMapper;  // Motorcycle 테이블 관련 작업을 위한 매퍼

    // Motorcycle 데이터 삽입
    @Transactional  // 데이터 삽입을 하나의 트랜잭션으로 처리
    public void insertFullMotorcycle(MotorcycleForm form) {
        MotorcycleDTO motorcycleDTO = form.toDTO();  // Form 객체를 DTO로 변환
        MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환
        motorcycleMapper.insertMotorcycleData(motorcycle);  // Motorcycle 기본 데이터를 데이터베이스에 삽입
        motorcycleMapper.insertDimensionsData(motorcycle.getDimensionsDomain());
        motorcycleMapper.insertElectronicsData(motorcycle.getElectronicsDomain());
        motorcycleMapper.insertEnginesData(motorcycle.getEnginesDomain());
        motorcycleMapper.insertFramesData(motorcycle.getFramesDomain());
        motorcycleMapper.insertTransmissionsData(motorcycle.getTransmissionsDomain());
    }

    // Motorcycle 데이터 업데이트
    @Transactional  // 데이터 업데이트를 하나의 트랜잭션으로 처리
    public void updateFullMotorcycle(MotorcycleForm form) {
        MotorcycleDTO motorcycleDTO = form.toDTO();  // Form 객체를 DTO로 변환
        MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환
        motorcycleMapper.updateMotorcycle(motorcycle);  // Motorcycle 기본 데이터 업데이트
    }

    // Motorcycle 데이터 삭제
    @Transactional  // 데이터 삭제를 하나의 트랜잭션으로 처리
    public void deleteFullMotorcycle(Long motorcycleID) {
        motorcycleMapper.deleteMotorcycle(motorcycleID);  // Motorcycle 데이터 삭제
    }

    // Motorcycle 데이터 조회 (단일)
    public MotorcycleDTO findOneMotorcycle(Long motorcycleID) {
        MotorcycleDomain motorcycle = motorcycleMapper.findByMotorcycleId(motorcycleID);  // 조인된 모든 데이터를 포함한 Motorcycle 객체 조회
        if(motorcycle == null){
            throw new IllegalArgumentException("Motorcycle ID " + motorcycleID + "에 해당하는 데이터가 없습니다.");
        }
        return MotorcycleDTO.fromDomain(motorcycle);  // 도메인 객체를 DTO로 변환하여 반환
    }

    // Motorcycle 데이터 조회 (리스트)
    public List<MotorcycleDTO> findFullMotorcycleList() {
        List<MotorcycleDomain> motorcycles = motorcycleMapper.findAll();  // 조인된 모든 데이터를 포함한 Motorcycle 리스트 조회
        return motorcycles.stream()
                .map(MotorcycleDTO::fromDomain)  // 도메인 객체를 DTO로 변환하여 반환
                .collect(Collectors.toList());  // 리스트로 반환
    }

    // 다수의 Motorcycle 데이터를 업데이트하는 메서드
    @Transactional
    public void updateMultipleMotorcycles(List<MotorcycleForm> forms) {
        // 각각의 MotorcycleForm을 순회하여 업데이트 작업 수행
        for (MotorcycleForm form : forms) {
            MotorcycleDTO motorcycleDTO = form.toDTO();  // Form 객체를 DTO로 변환
            MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환
            motorcycleMapper.updateMotorcycle(motorcycle);  // 각 Motorcycle 데이터를 업데이트
        }
    }

}
