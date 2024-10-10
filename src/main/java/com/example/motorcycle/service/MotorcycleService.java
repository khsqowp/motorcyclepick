package com.example.motorcycle.service;

import com.example.motorcycle.domain.*;
import com.example.motorcycle.dto.*; // 추가: DTO 사용을 위해 임포트
import com.example.motorcycle.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotorcycleService {

    @Autowired
    private MotorcycleMapper motorcycleMapper; // Motorcycle 테이블에 대한 매퍼

    @Autowired
    private DimensionsMapper dimensionsMapper; // Dimensions 테이블에 대한 매퍼

    @Autowired
    private ElectronicsMapper electronicsMapper; // Electronics 테이블에 대한 매퍼

    @Autowired
    private FramesMapper framesMapper; // Frames 테이블에 대한 매퍼

    @Autowired
    private EnginesMapper enginesMapper; // Engines 테이블에 대한 매퍼

    @Autowired
    private TransmissionsMapper transmissionsMapper; // Transmissions 테이블에 대한 매퍼

    // 일괄적으로 Motorcycle 데이터를 삽입하는 메서드
    @Transactional
    public void insertFullMotorcycle(MotorcycleDTO motorcycleDTO) { // 수정: DTO 사용
        // Motorcycle 데이터 삽입
        Motorcycle motorcycle = Motorcycle.fromDTO(motorcycleDTO); // DTO를 도메인 객체로 변환
        motorcycleMapper.insertMotorcycle(motorcycle); // Motorcycle 데이터 삽입

        Long motorcycleID = motorcycle.getMotorcycleID(); // 삽입된 Motorcycle의 ID 가져오기

        // 각 세부 정보 삽입 (motorcycleID를 매핑해서 삽입)
        if (motorcycleDTO.getDimensions() != null) { // Dimensions 정보가 있는 경우
            Dimensions dimensions = Dimensions.fromDTO(motorcycleDTO.getDimensionsDTO()); // DTO를 도메인 객체로 변환
            dimensions.setMotorcycleID(motorcycleID); // 외래 키 설정
            dimensionsMapper.insertDimensions(dimensions); // Dimensions 데이터 삽입
        }

        if (motorcycleDTO.getElectronics() != null) { // Electronics 정보가 있는 경우
            Electronics electronics = Electronics.fromDTO(motorcycleDTO.getElectronicsDTO()); // DTO를 도메인 객체로 변환
            electronics.setMotorcycleID(motorcycleID); // 외래 키 설정
            electronicsMapper.insertElectronics(electronics); // Electronics 데이터 삽입
        }

        if (motorcycleDTO.getFrames() != null) { // Frames 정보가 있는 경우
            Frames frames = Frames.fromDTO(motorcycleDTO.getFramesDTO()); // DTO를 도메인 객체로 변환
            frames.setMotorcycleID(motorcycleID); // 외래 키 설정
            framesMapper.insertFrames(frames); // Frames 데이터 삽입
        }

        if (motorcycleDTO.getEngines() != null) { // Engines 정보가 있는 경우
            Engines engines = Engines.fromDTO(motorcycleDTO.getEnginesDTO()); // DTO를 도메인 객체로 변환
            engines.setMotorcycleID(motorcycleID); // 외래 키 설정
            enginesMapper.insertEngines(engines); // Engines 데이터 삽입
        }

        if (motorcycleDTO.getTransmissions() != null) { // Transmissions 정보가 있는 경우
            Transmissions transmissions = Transmissions.fromDTO(motorcycleDTO.getTransmissionsDTO()); // DTO를 도메인 객체로 변환
            transmissions.setMotorcycleID(motorcycleID); // 외래 키 설정
            transmissionsMapper.insertTransmissions(transmissions); // Transmissions 데이터 삽입
        }
    }

    // 일괄적으로 Motorcycle 데이터를 업데이트하는 메서드
    @Transactional
    public void updateFullMotorcycle(MotorcycleDTO motorcycleDTO) { // 수정: DTO 사용
        // Motorcycle 데이터 업데이트
        Motorcycle motorcycle = Motorcycle.fromDTO(motorcycleDTO); // DTO를 도메인 객체로 변환
        motorcycleMapper.updateMotorcycle(motorcycle); // Motorcycle 데이터 업데이트

        Long motorcycleID = motorcycle.getMotorcycleID(); // 업데이트 대상 Motorcycle의 ID 가져오기

        // 각 세부 정보 업데이트
        if (motorcycleDTO.getDimensions() != null) { // Dimensions 정보가 있는 경우
            Dimensions dimensions = Dimensions.fromDTO(motorcycleDTO.getDimensionsDTO()); // DTO 변환 후 업데이트
            dimensions.setMotorcycleID(motorcycleID); // 외래 키 설정
            dimensionsMapper.updateDimensions(motorcycleID, dimensions); // Dimensions 데이터 업데이트
        }

        if (motorcycleDTO.getElectronics() != null) { // Electronics 정보가 있는 경우
            Electronics electronics = Electronics.fromDTO(motorcycleDTO.getElectronicsDTO()); // DTO 변환 후 업데이트
            electronics.setMotorcycleID(motorcycleID); // 외래 키 설정
            electronicsMapper.updateElectronics(motorcycleID, electronics); // Electronics 데이터 업데이트
        }

        if (motorcycleDTO.getFrames() != null) { // Frames 정보가 있는 경우
            Frames frames = Frames.fromDTO(motorcycleDTO.getFramesDTO()); // DTO 변환 후 업데이트
            frames.setMotorcycleID(motorcycleID); // 외래 키 설정
            framesMapper.updateFrames(motorcycleID, frames); // Frames 데이터 업데이트
        }

        if (motorcycleDTO.getEngines() != null) { // Engines 정보가 있는 경우
            Engines engines = Engines.fromDTO(motorcycleDTO.getEnginesDTO()); // DTO 변환 후 업데이트
            engines.setMotorcycleID(motorcycleID); // 외래 키 설정
            enginesMapper.updateEngines(motorcycleID, engines); // Engines 데이터 업데이트
        }

        if (motorcycleDTO.getTransmissions() != null) { // Transmissions 정보가 있는 경우
            Transmissions transmissions = Transmissions.fromDTO(motorcycleDTO.getTransmissionsDTO()); // DTO 변환 후 업데이트
            transmissions.setMotorcycleID(motorcycleID); // 외래 키 설정
            transmissionsMapper.updateTransmissions(motorcycleID, transmissions); // Transmissions 데이터 업데이트
        }
    }

    // 일괄적으로 Motorcycle 데이터를 삭제하는 메서드
    @Transactional
    public void deleteFullMotorcycle(Long motorcycleID) { // 입력받은 motorcycleID로 모든 관련 데이터 삭제
        // 일괄적으로 각 테이블의 데이터를 삭제
        dimensionsMapper.deleteDimensions(motorcycleID); // Dimensions 데이터 삭제
        electronicsMapper.deleteElectronics(motorcycleID); // Electronics 데이터 삭제
        framesMapper.deleteFrames(motorcycleID); // Frames 데이터 삭제
        enginesMapper.deleteEngines(motorcycleID); // Engines 데이터 삭제
        transmissionsMapper.deleteTransmissions(motorcycleID); // Transmissions 데이터 삭제

        // 최종적으로 Motorcycle 데이터 삭제
        motorcycleMapper.deleteMotorcycle(motorcycleID); // Motorcycle 데이터 삭제
    }

    // 일괄적으로 모든 데이터를 조회하는 메서드
    public MotorcycleDTO findFullMotorcycle(Long motorcycleID) { // 수정: 반환 타입을 DTO로 변경
        // 기본 Motorcycle 데이터 조회
        Motorcycle motorcycle = motorcycleMapper.findByMotorcycleId(motorcycleID); // Motorcycle 기본 데이터 조회

        if (motorcycle != null) { // 조회된 데이터가 있는 경우
            // 각 세부 정보 조회 후 motorcycle에 설정
            motorcycle.setDimensions(dimensionsMapper.findByMotorcycleId(motorcycleID)); // Dimensions 데이터 조회 후 설정
            motorcycle.setElectronics(electronicsMapper.findByMotorcycleId(motorcycleID)); // Electronics 데이터 조회 후 설정
            motorcycle.setFrames(framesMapper.findByMotorcycleId(motorcycleID)); // Frames 데이터 조회 후 설정
            motorcycle.setEngines(enginesMapper.findByMotorcycleId(motorcycleID)); // Engines 데이터 조회 후 설정
            motorcycle.setTransmissions(transmissionsMapper.findByMotorcycleId(motorcycleID)); // Transmissions 데이터 조회 후 설정
        }

        return MotorcycleDTO.fromDomain(motorcycle); // 도메인 객체를 DTO로 변환하여 반환
    }

    // 모든 Motorcycle 데이터를 조회하여 DTO 리스트로 반환하는 메서드
    public List<MotorcycleDTO> findFullMotorcycleList() { // 모든 Motorcycle 데이터를 리스트로 조회하여 DTO 형태로 반환
        List<Motorcycle> motorcycles = motorcycleMapper.findAll(); // 모든 Motorcycle 데이터를 조회
        return motorcycles.stream()
                .map(MotorcycleDTO::fromDomain) // 각 Motorcycle 도메인 객체를 DTO로 변환
                .collect(Collectors.toList()); // 리스트로 반환
    }

    // 여러 Motorcycle 데이터를 업데이트하는 메서드
    @Transactional
    public void updateMultipleMotorcycles(List<MotorcycleDTO> motorcycleDTOs) { // 여러 Motorcycle 데이터를 한 번에 업데이트
        for (MotorcycleDTO motorcycleDTO : motorcycleDTOs) { // 각 DTO에 대해 업데이트 수행
            updateFullMotorcycle(motorcycleDTO); // 기존 메서드를 재사용하여 업데이트 처리
        }
    }
}

// 주요 수정 사항:
// 1. DTO와 도메인 객체 간의 변환 메서드(`fromDTO`, `fromDomain`)를 추가하여 변환을 명확하게 처리.
// 2. 각 변환 부분을 수정하여 오류 해결.
// 3. DTO와 도메인 객체 간의 변환은 각 클래스에 적절한 정적 팩토리 메서드로 구현했다고 가정함.
// 4. 각 줄마다 기능을 주석으로 추가하여 가독성을 향상함.
// 5. `findFullMotorcycleList`와 `updateMultipleMotorcycles` 메서드를 추가하여 여러 데이터를 조회 및 수정할 수 있도록 확장함.