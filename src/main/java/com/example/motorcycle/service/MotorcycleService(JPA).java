//package com.example.motorcycle.service;
//
//import com.example.motorcycle.domain.*;
//import com.example.motorcycle.dto.*;
//import com.example.motorcycle.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class MotorcycleService(JPA) {
//
//    private final MotorcycleRepository motorcycleRepository;
//    private final DimensionsRepository dimensionsRepository;
//    private final ElectronicsRepository electronicsRepository;
//    private final EnginesRepository enginesRepository;
//    private final FramesRepository framesRepository;
//    private final MotorcycleSpecsRepository motorcycleSpecsRepository;
//    private final TransmissionsRepository transmissionsRepository;
//
//    // 모든 Motorcycle 데이터를 DTO로 변환하여 반환
//    public List<MotorcycleDTO> findAllAsDTO() {
//        List<Motorcycle> motorcycles = motorcycleRepository.findAllWithDetails();
//
//        return motorcycles.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // 특정 Motorcycle 데이터를 DTO로 반환
//    public MotorcycleDTO findByIdAsDTO(Long id) {
//        Motorcycle motorcycle = motorcycleRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Motorcycle not found"));
//        return convertToDTO(motorcycle);
//    }
//
//    // Motorcycle 삭제
//    public void deleteById(Long id) {
//        motorcycleRepository.deleteById(id);
//    }
//
//    // Motorcycle을 DTO로 변환
//    public MotorcycleDTO convertToDTO(Motorcycle motorcycle) {
//        MotorcycleDTO dto = new MotorcycleDTO();
//        dto.setMotorcycleID(motorcycle.getMotorcycleID());
//        dto.setBrand(motorcycle.getBrand());
//        dto.setModel(motorcycle.getModel());
//        dto.setYears(motorcycle.getYears());
//        dto.setProduction(motorcycle.getProduction());
//
//        // 서브 엔티티가 존재하는지 확인 후 DTO 설정 (NullPointerException 방지)
//        if (motorcycle.getDimensions() != null) {
//            Dimensions dimensions = motorcycle.getDimensions();
//            DimensionsDTO dimensionsDTO = new DimensionsDTO();
//            dimensionsDTO.setDimensionID(dimensions.getId());
//            dimensionsDTO.setSeatHeight(dimensions.getSeatHeight());
//            dimensionsDTO.setWheelbase(dimensions.getWheelbase());
//            dimensionsDTO.setGroundClearance(dimensions.getGroundClearance());
//            dimensionsDTO.setDryWeight(dimensions.getDryWeight());
//            dimensionsDTO.setWetWeight(dimensions.getWetWeight());
//            dimensionsDTO.setFuelCapacity(dimensions.getFuelCapacity());
//            dimensionsDTO.setInnerLegCurve(dimensions.getInnerLegCurve());
//            dimensionsDTO.setPermittedTotalWeight(dimensions.getPermittedTotalWeight());
//            dto.setDimensionsDTO(dimensionsDTO);  // DimensionsDTO 설정
//        }
//
//        if (motorcycle.getElectronics() != null) {
//            Electronics electronics = motorcycle.getElectronics();
//            ElectronicsDTO electronicsDTO = new ElectronicsDTO();
//            electronicsDTO.setElectronicsID(electronics.getId());
//            electronicsDTO.setEngineManagement(electronics.getEngineManagement());
//            electronicsDTO.setEmissionControl(electronics.getEmissionControl());
//            electronicsDTO.setEngineControl(electronics.getEngineControl());
//            electronicsDTO.setAlternator(electronics.getAlternator());
//            electronicsDTO.setBattery(electronics.getBattery());
//            electronicsDTO.setHeadlight(electronics.getHeadlight());
//            electronicsDTO.setIgnition(electronics.getIgnition());
//            electronicsDTO.setStarting(electronics.getStarting());
//            electronicsDTO.setTractionControl(electronics.getTractionControl());
//            dto.setElectronicsDTO(electronicsDTO);  // ElectronicsDTO 설정
//        }
//
//        if (motorcycle.getEngines() != null) {
//            Engines engines = motorcycle.getEngines();
//            EnginesDTO enginesDTO = new EnginesDTO();
//            enginesDTO.setEngineID(engines.getId());
//            enginesDTO.setEngine(engines.getEngine());
//            enginesDTO.setCapacity(engines.getCapacity());  // Null 체크 필요할 경우 추가 가능
//            enginesDTO.setBoreStroke(engines.getBoreStroke());
//            enginesDTO.setCompressionRatio(engines.getCompressionRatio());
//            enginesDTO.setCoolingSystem(engines.getCoolingSystem());
//            enginesDTO.setLubrication(engines.getLubrication());
//            enginesDTO.setMaxPower(engines.getMaxPower());
//            enginesDTO.setMaxTorque(engines.getMaxTorque());
//            enginesDTO.setFuelSystem(engines.getFuelSystem());
//            enginesDTO.setExhaust(engines.getExhaust());
//            enginesDTO.setEngineOil(engines.getEngineOil());
//            enginesDTO.setMixtureControl(engines.getMixtureControl());
//            enginesDTO.setEmission(engines.getEmission());
//            enginesDTO.setInduction(engines.getInduction());
//            dto.setEnginesDTO(enginesDTO);  // EnginesDTO 설정
//        }
//
//        if (motorcycle.getFrames() != null) {
//            Frames frames = motorcycle.getFrames();
//            FramesDTO framesDTO = new FramesDTO();
//            framesDTO.setFrameID(frames.getId());
//            framesDTO.setFrame(frames.getFrame());
//            framesDTO.setFrontSuspension(frames.getFrontSuspension());
//            framesDTO.setRearSuspension(frames.getRearSuspension());
//            framesDTO.setFrontWheelTravel(frames.getFrontWheelTravel());
//            framesDTO.setRearWheelTravel(frames.getRearWheelTravel());
//            framesDTO.setFrontBrakes(frames.getFrontBrakes());
//            framesDTO.setRearBrakes(frames.getRearBrakes());
//            framesDTO.setAbsSystem(frames.getAbsSystem());
//            framesDTO.setFrontWheel(frames.getFrontWheel());
//            framesDTO.setRearWheel(frames.getRearWheel());
//            framesDTO.setFrontTyre(frames.getFrontTyre());
//            framesDTO.setRearTyre(frames.getRearTyre());
//            framesDTO.setWheels(frames.getWheels());
//            framesDTO.setAbs(frames.getAbs());
//            framesDTO.setAbsPro(frames.getAbsPro());
//            framesDTO.setRake(frames.getRake());
//            framesDTO.setTrail(frames.getTrail());
//            framesDTO.setFrontRim(frames.getFrontRim());
//            framesDTO.setRearRim(frames.getRearRim());
//            framesDTO.setCastor(frames.getCastor());
//            framesDTO.setSteeringAngle(frames.getSteeringAngle());
//            framesDTO.setSteeringHeadAngle(frames.getSteeringHeadAngle());
//            dto.setFramesDTO(framesDTO);  // FramesDTO 설정
//        }
//
//        if (motorcycle.getTransmissions() != null) {
//            Transmissions transmissions = motorcycle.getTransmissions();
//            TransmissionsDTO transmissionsDTO = new TransmissionsDTO();
//            transmissionsDTO.setTransmissionID(transmissions.getId());
//            transmissionsDTO.setTransmissionDrive(transmissions.getTransmissionDrive());
//            transmissionsDTO.setTransmission(transmissions.getTransmission());
//            transmissionsDTO.setFinalDrive(transmissions.getFinalDrive());
//            transmissionsDTO.setPrimaryDriveRatio(transmissions.getPrimaryDriveRatio());
//            transmissionsDTO.setPrimaryRatio(transmissions.getPrimaryRatio());
//            transmissionsDTO.setGearRatio(transmissions.getGearRatio());
//            transmissionsDTO.setTransmissionRatio(transmissions.getTransmissionRatio());
//            transmissionsDTO.setSecondaryRatio(transmissions.getSecondaryRatio());
//            transmissionsDTO.setGearRatios(transmissions.getGearRatios());
//            transmissionsDTO.setClutch(transmissions.getClutch());
//            dto.setTransmissionsDTO(transmissionsDTO);  // TransmissionsDTO 설정
//        }
//
//        return dto;
//    }
//
//
//    // DTO를 Motorcycle 엔티티로 변환
//    private Motorcycle convertToEntity(MotorcycleDTO dto) {
//        Motorcycle motorcycle = new Motorcycle();
//        updateEntity(motorcycle, dto);
//        return motorcycle;
//    }
//
//    // Motorcycle 엔티티 업데이트 (NullPointerException 방지 추가)
//    private void updateEntity(Motorcycle motorcycle, MotorcycleDTO dto) {
//        motorcycle.setBrand(dto.getBrand());
//        motorcycle.setModel(dto.getModel());
//        motorcycle.setYears(dto.getYears());
//        motorcycle.setProduction(dto.getProduction());
//
//        // 서브 엔티티 찾기 및 설정 (Null 체크 추가)
//        if (dto.getDimensionsDTO() != null && dto.getDimensionsDTO().getDimensionID() != null) {
//            Dimensions dimensions = dimensionsRepository.findById(dto.getDimensionsDTO().getDimensionID())
//                    .orElseThrow(() -> new RuntimeException("Dimensions not found"));
//            motorcycle.setDimensions(dimensions);
//        }
//
//        if (dto.getElectronicsDTO() != null && dto.getElectronicsDTO().getElectronicsID() != null) {
//            Electronics electronics = electronicsRepository.findById(dto.getElectronicsDTO().getElectronicsID())
//                    .orElseThrow(() -> new RuntimeException("Electronics not found"));
//            motorcycle.setElectronics(electronics);
//        }
//
//        if (dto.getEnginesDTO() != null && dto.getEnginesDTO().getEngineID() != null) {
//            Engines engines = enginesRepository.findById(dto.getEnginesDTO().getEngineID())
//                    .orElseThrow(() -> new RuntimeException("Engines not found"));
//            motorcycle.setEngines(engines);
//        }
//
//        if (dto.getFramesDTO() != null && dto.getFramesDTO().getFrameID() != null) {
//            Frames frames = framesRepository.findById(dto.getFramesDTO().getFrameID())
//                    .orElseThrow(() -> new RuntimeException("Frames not found"));
//            motorcycle.setFrames(frames);
//        }
//
////        if (dto.getMotorcycleSpecsDTO() != null && dto.getMotorcycleSpecsDTO().getMotorcycleSpecsID() != null) {
////            MotorcycleSpecs specs = motorcycleSpecsRepository.findById(dto.getMotorcycleSpecsDTO().getMotorcycleSpecsID())
////                    .orElseThrow(() -> new RuntimeException("MotorcycleSpecs not found"));
////            motorcycle.setMotorcycleSpecs(specs);
////        }
//
//        if (dto.getTransmissionsDTO() != null && dto.getTransmissionsDTO().getTransmissionID() != null) {
//            Transmissions transmission = transmissionsRepository.findById(dto.getTransmissionsDTO().getTransmissionID())
//                    .orElseThrow(() -> new RuntimeException("Transmission not found"));
//            motorcycle.setTransmissions(transmission);
//        }
//    }
//
//
//    // saveFromDTO 메서드
//    public void saveFromDTO(MotorcycleDTO motorcycleDTO) {
//        Motorcycle motorcycle = convertToEntity(motorcycleDTO);
//        motorcycleRepository.save(motorcycle);
//    }
//
//    // updateFromDTO 메서드
//    public void updateFromDTO(Long id, MotorcycleDTO motorcycleDTO) {
//        Motorcycle motorcycle = motorcycleRepository.findById(id).orElseThrow(() -> new RuntimeException("Motorcycle not found"));
//        updateEntity(motorcycle, motorcycleDTO);
//        motorcycleRepository.save(motorcycle);
//    }
//
//    // 여러 Motorcycle 업데이트
//    public void updateMultipleFromDTO(List<MotorcycleDTO> motorcycleDTOs) {
//        for (MotorcycleDTO dto : motorcycleDTOs) {
//            Motorcycle motorcycle = motorcycleRepository.findById(dto.getMotorcycleID())
//                    .orElseThrow(() -> new RuntimeException("Motorcycle not found"));
//            updateEntity(motorcycle, dto);
//            motorcycleRepository.save(motorcycle);
//        }
//    }
//    public List<MotorcycleDTO> convertToDTOList(List<Motorcycle> motorcycles) {
//        return motorcycles.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }
//}
