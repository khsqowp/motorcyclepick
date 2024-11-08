package com.example.motorcycle.service;

import com.example.motorcycle.domain.*;
import com.example.motorcycle.dto.*;
import com.example.motorcycle.form.MotorcycleForm;
import com.example.motorcycle.repository.MotorcycleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MotorcycleService {

    private final MotorcycleMapper motorcycleMapper;

    @Autowired
    public MotorcycleService(MotorcycleMapper motorcycleMapper){
        this.motorcycleMapper = motorcycleMapper;
    }

    // Motorcycle 데이터 삽입
    public void insertFullMotorcycle(MotorcycleForm form) {

        //Form -> DTO -> Domain
        MotorcycleDTO motorcycleDTO = form.toDTO();  // Form 객체를 DTO로 변환
        MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환

        // 기본 Motorcycle 데이터 삽입
        motorcycleMapper.insertMotorcycleData(motorcycle);
        Long motorcycleID = motorcycle.getMotorcycleID();

//        // 필드가 NULL일 경우 빈 객체로 초기화
//        if (motorcycleDTO.getDimensionsDTO() == null) {
//            motorcycleDTO.setDimensionsDTO(new DimensionsDTO());
//        }
//        if (motorcycleDTO.getElectronicsDTO() == null) {
//            motorcycleDTO.setElectronicsDTO(new ElectronicsDTO());
//        }
//        if (motorcycleDTO.getEnginesDTO() == null) {
//            motorcycleDTO.setEnginesDTO(new EnginesDTO());
//        }
//        if (motorcycleDTO.getFramesDTO() == null) {
//            motorcycleDTO.setFramesDTO(new FramesDTO());
//        }
//        if (motorcycleDTO.getTransmissionsDTO() == null) {
//            motorcycleDTO.setTransmissionsDTO(new TransmissionsDTO());
//        }
//
//        //motorcycleID를 각 관련 도메인에 설정]
//        DimensionsDomain dimensionsDomain = new DimensionsDomain();
//        motorcycle.getDimensionsDomain().setMotorcycleID(motorcycleID);
//        ElectronicsDomain electronicsDomain = new ElectronicsDomain();
//        motorcycle.getElectronicsDomain().setMotorcycleID(motorcycleID);
//        EnginesDomain enginesDomain = new EnginesDomain();
//        motorcycle.getEnginesDomain().setMotorcycleID(motorcycleID);
//        FramesDomain framesDomain = new FramesDomain();
//        motorcycle.getFramesDomain().setMotorcycleID(motorcycleID);
//        TransmissionsDomain transmissionsDomain = new TransmissionsDomain();
//        motorcycle.getTransmissionsDomain().setMotorcycleID(motorcycleID);

        motorcycle.getDimensionsDomain().setMotorcycleID(motorcycleID);
        motorcycle.getElectronicsDomain().setMotorcycleID(motorcycleID);
        motorcycle.getEnginesDomain().setMotorcycleID(motorcycleID);
        motorcycle.getFramesDomain().setMotorcycleID(motorcycleID);
        motorcycle.getTransmissionsDomain().setMotorcycleID(motorcycleID);

        //관련 데이터 삽입
        motorcycleMapper.insertDimensionsData(motorcycle.getDimensionsDomain());
        motorcycleMapper.insertElectronicsData(motorcycle.getElectronicsDomain());
        motorcycleMapper.insertEnginesData(motorcycle.getEnginesDomain());
        motorcycleMapper.insertFramesData(motorcycle.getFramesDomain());
        motorcycleMapper.insertTransmissionsData(motorcycle.getTransmissionsDomain());
    }

    // Motorcycle 데이터 업데이트
    public void updateFullMotorcycle(MotorcycleForm form) {
        MotorcycleDTO motorcycleDTO = form.toDTO();  // Form 객체를 DTO로 변환
        MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환
        Long motorcycleID = motorcycle.getMotorcycleID();

        if(motorcycleID == null){
            throw new IllegalArgumentException("Motorcycle ID cannot be null for update operation");
        }

        //각 도메인의 motorcycleID설정
        motorcycle.getDimensionsDomain().setMotorcycleID(motorcycleID);
        motorcycle.getElectronicsDomain().setMotorcycleID(motorcycleID);
        motorcycle.getEnginesDomain().setMotorcycleID(motorcycleID);
        motorcycle.getFramesDomain().setMotorcycleID(motorcycleID);
        motorcycle.getTransmissionsDomain().setMotorcycleID(motorcycleID);

        motorcycleMapper.updateMotorcycle(motorcycle);  // Motorcycle 기본 데이터 업데이트
        motorcycleMapper.updateDimensions(motorcycleID, motorcycle.getDimensionsDomain());
        motorcycleMapper.updateElectronics(motorcycleID, motorcycle.getElectronicsDomain());
        motorcycleMapper.updateEngines(motorcycleID, motorcycle.getEnginesDomain());
        motorcycleMapper.updateFrames(motorcycleID, motorcycle.getFramesDomain());
        motorcycleMapper.updateTransmissions(motorcycleID, motorcycle.getTransmissionsDomain());
    }

    // Motorcycle 데이터 삭제
    public void deleteFullMotorcycle(Long motorcycleID) {
        //관련 테이블 삭제
        motorcycleMapper.deleteDimensions(motorcycleID);
        motorcycleMapper.deleteElectronics(motorcycleID);
        motorcycleMapper.deleteEngines(motorcycleID);
        motorcycleMapper.deleteFrames(motorcycleID);
        motorcycleMapper.deleteTransmissions(motorcycleID);
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
//            MotorcycleDTO motorcycleDTO = form.toDTO();  // Form 객체를 DTO로 변환
//            MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);  // DTO를 도메인 객체로 변환
//            motorcycleMapper.updateMotorcycle(motorcycle);  // 각 Motorcycle 데이터를 업데이트
            updateFullMotorcycle(form);
        }
    }

}
