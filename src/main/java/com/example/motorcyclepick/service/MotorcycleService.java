package com.example.motorcyclepick.service;

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.domain.DimensionsDomain;
import com.example.motorcyclepick.domain.ElectronicsDomain;
import com.example.motorcyclepick.domain.EnginesDomain;
import com.example.motorcyclepick.domain.MotorcycleDomain;
import com.example.motorcyclepick.dto.DimensionsDTO;
import com.example.motorcyclepick.dto.ElectronicsDTO;
import com.example.motorcyclepick.dto.EnginesDTO;
import com.example.motorcyclepick.dto.MotorcycleDTO;
import com.example.motorcyclepick.exception.*;
import com.example.motorcyclepick.form.MotorcycleForm;
import com.example.motorcyclepick.repository.MotorcycleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MotorcycleService {
    private final MotorcycleMapper motorcycleMapper;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private SecurityLogger securityLogger;

    @Autowired
    public MotorcycleService(MotorcycleMapper motorcycleMapper) {
        this.motorcycleMapper = motorcycleMapper;
    }

    //    ___________________________________________________________________________________________________________________________

    public void insertFullMotorcycle(@Valid MotorcycleForm form) {
        try {
            log.info("Starting to insert new motorcycle data");
            validateMotorcycleForm(form);

            // 데이터 입력값 검증 추가
            validateAndSanitizeInput(form);
            securityLogger.logSecurityEvent("MOTORCYCLE_INSERT_ATTEMPT", "SYSTEM", "N/A");

            MotorcycleDTO motorcycleDTO = form.toDTO();
            MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);

            motorcycleMapper.insertMotorcycleData(motorcycle);
            Long motorcycleID = motorcycle.getMotorcycleID();

            securityLogger.logSecurityEvent("MOTORCYCLE_INSERT_SUCCESS", "SYSTEM", "ID: " + motorcycleID);
            log.info("Successfully inserted motorcycle data with ID: {}", motorcycleID);
        } catch (Exception e) {
            securityLogger.logSecurityEvent("MOTORCYCLE_INSERT_FAILURE", "SYSTEM", e.getMessage());
            log.error("Error occurred while inserting motorcycle data", e);

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("오토바이 데이터 입력 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("오토바이 데이터 저장 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("오토바이 데이터 처리 중 오류가 발생했습니다.", e);
        }
    }

    //    ___________________________________________________________________________________________________________________________

    @Transactional
    public void updateFullMotorcycle(@Valid MotorcycleForm form) {
        try {
            log.info("Starting to update motorcycle data for ID: {}", form.getMotorcycleID());
            log.info("Form data: {}", form);  // 로그 추가
            validateMotorcycleForm(form);

            MotorcycleDTO motorcycleDTO = form.toDTO();
            log.info("Update DTO data: {}", motorcycleDTO.toString());  // DTO 데이터 확인

            MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);
            log.info("Update Domain data: {}", motorcycle.toString());  // Domain 데이터 확인

            motorcycleMapper.updateMotorcycle(motorcycle);
            Long motorcycleID = motorcycle.getMotorcycleID();

            Assert.notNull(motorcycleID, "Motorcycle ID cannot be null for update operation");

            if (motorcycleMapper.findByMotorcycleId(motorcycleID) == null) {
                throw new MotorcycleNotFoundException("Motorcycle not found with ID: " + motorcycleID);
            }
            log.info("Before updating sub domains for ID: {}", motorcycleID);
            log.info("After initialization, before update for ID: {}", motorcycleID);
            log.info("Successfully updated motorcycle data with ID: {}", motorcycleID);


            log.info("Successfully updated motorcycle data with ID: {}", motorcycleID);
        } catch (MotorcycleNotFoundException e) {
            log.error("Motorcycle not found", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating motorcycle data", e);

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("오토바이 데이터 수정 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("오토바이 데이터 수정 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("오토바이 데이터 처리 중 오류가 발생했습니다.", e);
        }
    }

    //    ___________________________________________________________________________________________________________________________

    public void deleteFullMotorcycle(Long motorcycleID) {
        try {
            log.info("Starting to delete motorcycle data with ID: {}", motorcycleID);
            Assert.notNull(motorcycleID, "Motorcycle ID cannot be null for delete operation");

            if (motorcycleMapper.findByMotorcycleId(motorcycleID) == null) {
                throw new MotorcycleNotFoundException("Motorcycle not found with ID: " + motorcycleID);
            }

            motorcycleMapper.deleteMotorcycle(motorcycleID);

            log.info("Successfully deleted motorcycle data with ID: {}", motorcycleID);
        } catch (Exception e) {
            log.error("Error occurred while deleting motorcycle data", e);

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("오토바이 데이터 삭제 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("오토바이 데이터 삭제 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("오토바이 데이터 처리 중 오류가 발생했습니다.", e);
        }
    }

    //    ___________________________________________________________________________________________________________________________

    @Transactional(readOnly = true)
    public MotorcycleDTO findOneMotorcycle(Long motorcycleID) {
        try {
            securityLogger.logSecurityEvent("MOTORCYCLE_ACCESS", "SYSTEM", "ID: " + motorcycleID);
            log.info("Fetching motorcycle data with ID: {}", motorcycleID);
            Assert.notNull(motorcycleID, "Motorcycle ID cannot be null");

            if (motorcycleID <= 0) {
                securityLogger.logSecurityEvent("INVALID_MOTORCYCLE_ID_ACCESS", "SYSTEM", "ID: " + motorcycleID);
                throw new MotorcycleValidationException("Invalid motorcycle ID");
            }

            MotorcycleDomain motorcycle = motorcycleMapper.findByMotorcycleId(motorcycleID);
            if (motorcycle == null) {
                securityLogger.logSecurityEvent("MOTORCYCLE_NOT_FOUND", "SYSTEM", "ID: " + motorcycleID);
                throw new MotorcycleNotFoundException("Motorcycle not found with ID: " + motorcycleID);
            }

            MotorcycleDTO dto = MotorcycleDTO.fromDomain(motorcycle);

            securityLogger.logSecurityEvent("MOTORCYCLE_ACCESS_SUCCESS", "SYSTEM", "ID: " + motorcycleID);
            log.info("Successfully fetched motorcycle data with ID: {}", motorcycleID);
            return dto;
        } catch (Exception e) {
            securityLogger.logSecurityEvent("MOTORCYCLE_ACCESS_FAILURE", "SYSTEM", e.getMessage());
            log.error("Error occurred while fetching motorcycle data", e);

            if (e instanceof AccessDeniedException) {
                throw new AuthorizationException("오토바이 데이터 조회 권한이 없습니다.", e);
            } else if (e instanceof DataAccessException) {
                throw new DataAccessException("오토바이 데이터 조회 중 오류가 발생했습니다.", e);
            }
            throw new DataIntegrityException("오토바이 데이터 처리 중 오류가 발생했습니다.", e);
        }
    }

    //    ___________________________________________________________________________________________________________________________

    @Transactional(readOnly = true)
    public List<MotorcycleDTO> findFullMotorcycleList() {
        try {
            log.info("Fetching all motorcycle data");
            List<MotorcycleDomain> motorcycles = motorcycleMapper.findAll();
            List<MotorcycleDTO> dtoList = motorcycles.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            log.info("Successfully fetched {} motorcycles", dtoList.size());
            return dtoList;
        } catch (Exception e) {
            log.error("Error occurred while fetching motorcycle list", e);
            throw new MotorcycleValidationException("Failed to fetch motorcycle list: " + e.getMessage());
        }
    }

    //    ___________________________________________________________________________________________________________________________

    @Transactional
    public void updateMultipleMotorcycles(List<MotorcycleForm> forms) {
        try {
            log.info("Starting batch update for {} motorcycles", forms.size());
            for (MotorcycleForm form : forms) {
                updateFullMotorcycle(form);
            }
            log.info("Successfully completed batch update");
        } catch (Exception e) {
            log.error("Error occurred during batch update", e);
            throw new MotorcycleValidationException("Failed to update multiple motorcycles: " + e.getMessage());
        }
    }

    //    ___________________________________________________________________________________________________________________________

    private void validateMotorcycleForm(MotorcycleForm form) {
        Assert.notNull(form, "Motorcycle form cannot be null");
    }

    //    ___________________________________________________________________________________________________________________________

    private MotorcycleDTO convertToDTO(MotorcycleDomain domain) {
        return MotorcycleDTO.fromDomain(domain);  // initializeNullDomains와 initializeNullDTOs 호출 제거
    }

    private void updateSubDomainData(MotorcycleDomain motorcycle) {
        Long motorcycleID = motorcycle.getMotorcycleID();

        try {

        } catch (Exception e) {
            log.error("Error in updateSubDomainData: ", e);
            throw e;
        }
    }

    //    ___________________________________________________________________________________________________________________________

    // Dimensions 필드 null 체크
    private boolean hasNonNullDimensionsFields(DimensionsDomain domain) {
        return domain.getWheelbase() != null ||
                domain.getSeatHeight() != null ||
                domain.getWetWeight() != null ||
                domain.getFuelCapacity() != null ||
                domain.getGroundClearance() != null ||
                domain.getDryWeight() != null ||
                domain.getLength() != null ||
                domain.getWidth() != null ||
                domain.getHeight() != null ||
                domain.getFrontTyreWidth() != null ||
                domain.getFrontTyreAspectRatio() != null ||
                domain.getFrontTyreStructure() != null ||
                domain.getFrontTyreDiameter() != null ||
                domain.getRearTyreWidth() != null ||
                domain.getRearTyreAspectRatio() != null ||
                domain.getRearTyreStructure() != null ||
                domain.getRearTyreDiameter() != null ||
                domain.getFrontBrakeDiscCount() != null ||
                domain.getFrontBrakeDiscSize() != null ||
                domain.getFrontBrakeDiscType() != null ||
                domain.getFrontBrakePistonCount() != null ||
                domain.getRearBrakeDiscSize() != null ||
                domain.getRearBrakeDiscType() != null ||
                domain.getRearBrakePistonCount() != null ||
                domain.getFrameType() != null ||
                domain.getFrameMaterial() != null;
    }

    // Electronics 필드 null 체크
    private boolean hasNonNullElectronicsFields(ElectronicsDomain domain) {
        return domain.getStartSystem() != null ||
                domain.getAbs() != null ||
                domain.getTcs() != null ||
                domain.getCruiseControl() != null ||
                domain.getAssistSlipperClutch() != null ||
                domain.getElectricScreen() != null ||
                domain.getClutchAssistSystem() != null ||
                domain.getImu() != null ||
                domain.getCorneringAbs() != null ||
                domain.getWheelieControl() != null ||
                domain.getRidingModeChange() != null ||
                domain.getBankingAngleDisplay() != null ||
                domain.getAbsLevelControl() != null ||
                domain.getQuickshiftUp() != null ||
                domain.getQuickshiftUpDown() != null;
    }

    // Engines 필드 null 체크
    private boolean hasNonNullEnginesFields(EnginesDomain domain) {
        return domain.getCapacity() != null ||
                domain.getBoreStroke() != null ||
                domain.getCompressionRatio() != null ||
                domain.getCoolingSystem() != null ||
                domain.getLubrication() != null ||
                domain.getFuelSystem() != null ||
                domain.getEmission() != null ||
                domain.getInduction() != null ||
                domain.getMileage() != null ||
                domain.getTopSpeed() != null ||
                domain.getClutch() != null ||
                domain.getTransmissionGearCount() != null ||
                domain.getTransmissionType() != null ||
                domain.getEngineStroke() != null ||
                domain.getEngineCylinder() != null ||
                domain.getEngineCamshaft() != null ||
                domain.getEngineType() != null ||
                domain.getEngineCrankAngle() != null ||
                domain.getMaxPowerHp() != null ||
                domain.getMaxPowerRpm() != null ||
                domain.getMaxTorqueNm() != null ||
                domain.getMaxTorqueRpm() != null ||
                domain.getClassGrade() != null;
    }

    private void validateAndSanitizeInput(MotorcycleForm form) {
        // 기본 정보 검증
        if (form.getBrand() != null) {
            form.setBrand(securityService.sanitizeInput(form.getBrand()));
        }
        if (form.getModel() != null) {
            form.setModel(securityService.sanitizeInput(form.getModel()));
        }
        if (form.getYears() != null && (form.getYears() < 1900 || form.getYears() > 2100)) {
            throw new MotorcycleValidationException("Invalid year value");
        }
        if (form.getReplica() != null && form.getReplica() < 0) {
            throw new MotorcycleValidationException("Invalid replica value");
        }
        if (form.getCruiser() != null && form.getCruiser() < 0) {
            throw new MotorcycleValidationException("Invalid cruiser value");
        }
        if (form.getTourer() != null && form.getTourer() < 0) {
            throw new MotorcycleValidationException("Invalid tourer value");
        }
        if (form.getAdventure() != null && form.getAdventure() < 0) {
            throw new MotorcycleValidationException("Invalid adventure value");
        }
        if (form.getMultiPurpose() != null && form.getMultiPurpose() < 0) {
            throw new MotorcycleValidationException("Invalid multiPurpose value");
        }
        if (form.getNaked() != null && form.getNaked() < 0) {
            throw new MotorcycleValidationException("Invalid naked value");
        }
        if (form.getScrambler() != null && form.getScrambler() < 0) {
            throw new MotorcycleValidationException("Invalid scrambler value");
        }
        if (form.getOffRoad() != null && form.getOffRoad() < 0) {
            throw new MotorcycleValidationException("Invalid offRoad value");
        }
        if (form.getMotard() != null && form.getMotard() < 0) {
            throw new MotorcycleValidationException("Invalid motard value");
        }
        if (form.getScooter() != null && form.getScooter() < 0) {
            throw new MotorcycleValidationException("Invalid scooter value");
        }
        if (form.getClassic() != null && form.getClassic() < 0) {
            throw new MotorcycleValidationException("Invalid classic value");
        }
        if (form.getCafeRacer() != null && form.getCafeRacer() < 0) {
            throw new MotorcycleValidationException("Invalid cafeRacer value");
        }
        if (form.getPrice() != null && form.getPrice() < 0) {
            throw new MotorcycleValidationException("Invalid price value");
        }

        // Dimensions 데이터 검증
        if (form.getWheelbase() != null && form.getWheelbase() < 0) {
            throw new MotorcycleValidationException("Invalid wheelbase value");
        }
        if (form.getSeatHeight() != null && form.getSeatHeight() < 0) {
            throw new MotorcycleValidationException("Invalid seat height value");
        }
        if (form.getWetWeight() != null && form.getWetWeight() < 0) {
            throw new MotorcycleValidationException("Invalid wet weight value");
        }
        if (form.getFuelCapacity() != null && form.getFuelCapacity() < 0) {
            throw new MotorcycleValidationException("Invalid fuel capacity value");
        }
        if (form.getGroundClearance() != null && form.getGroundClearance() < 0) {
            throw new MotorcycleValidationException("Invalid ground clearance value");
        }
        if (form.getDryWeight() != null && form.getDryWeight() < 0) {
            throw new MotorcycleValidationException("Invalid dry weight value");
        }
        if (form.getLength() != null && form.getLength() < 0) {
            throw new MotorcycleValidationException("Invalid length value");
        }
        if (form.getWidth() != null && form.getWidth() < 0) {
            throw new MotorcycleValidationException("Invalid width value");
        }
        if (form.getHeight() != null && form.getHeight() < 0) {
            throw new MotorcycleValidationException("Invalid height value");
        }
        if (form.getFrontTyreWidth() != null && form.getFrontTyreWidth() < 0) {
            throw new MotorcycleValidationException("Invalid front tyre width value");
        }
        if (form.getFrontTyreAspectRatio() != null && form.getFrontTyreAspectRatio() < 0) {
            throw new MotorcycleValidationException("Invalid front tyre aspect ratio value");
        }
        if (form.getFrontTyreStructure() != null) {
            form.setFrontTyreStructure(securityService.sanitizeInput(form.getFrontTyreStructure()));
        }
        if (form.getFrontTyreDiameter() != null && form.getFrontTyreDiameter() < 0) {
            throw new MotorcycleValidationException("Invalid front tyre diameter value");
        }
        if (form.getRearTyreWidth() != null && form.getRearTyreWidth() < 0) {
            throw new MotorcycleValidationException("Invalid rear tyre width value");
        }
        if (form.getRearTyreAspectRatio() != null && form.getRearTyreAspectRatio() < 0) {
            throw new MotorcycleValidationException("Invalid rear tyre aspect ratio value");
        }
        if (form.getRearTyreStructure() != null) {
            form.setRearTyreStructure(securityService.sanitizeInput(form.getRearTyreStructure()));
        }
        if (form.getRearTyreDiameter() != null && form.getRearTyreDiameter() < 0) {
            throw new MotorcycleValidationException("Invalid rear tyre diameter value");
        }
        if (form.getFrontBrakeDiscCount() != null) {
            form.setFrontBrakeDiscCount(securityService.sanitizeInput(form.getFrontBrakeDiscCount()));
        }
        if (form.getFrontBrakeDiscSize() != null && form.getFrontBrakeDiscSize() < 0) {
            throw new MotorcycleValidationException("Invalid front brake disc size value");
        }
        if (form.getFrontBrakeDiscType() != null) {
            form.setFrontBrakeDiscType(securityService.sanitizeInput(form.getFrontBrakeDiscType()));
        }
        if (form.getFrontBrakePistonCount() != null && form.getFrontBrakePistonCount() < 0) {
            throw new MotorcycleValidationException("Invalid front brake piston count value");
        }
        if (form.getRearBrakeDiscSize() != null && form.getRearBrakeDiscSize() < 0) {
            throw new MotorcycleValidationException("Invalid rear brake disc size value");
        }
        if (form.getRearBrakeDiscType() != null) {
            form.setRearBrakeDiscType(securityService.sanitizeInput(form.getRearBrakeDiscType()));
        }
        if (form.getRearBrakePistonCount() != null && form.getRearBrakePistonCount() < 0) {
            throw new MotorcycleValidationException("Invalid rear brake piston count value");
        }
        if (form.getFrameType() != null) {
            form.setFrameType(securityService.sanitizeInput(form.getFrameType()));
        }
        if (form.getFrameMaterial() != null) {
            form.setFrameMaterial(securityService.sanitizeInput(form.getFrameMaterial()));
        }

        // Electronics 데이터 검증
        if (form.getStartSystem() != null) {
            form.setStartSystem(securityService.sanitizeInput(form.getStartSystem()));
        }

        // Engines 데이터 검증
        if (form.getCapacity() != null && form.getCapacity() < 0) {
            throw new MotorcycleValidationException("Invalid capacity value");
        }
        if (form.getBoreStroke() != null) {
            form.setBoreStroke(securityService.sanitizeInput(form.getBoreStroke()));
        }
        if (form.getCompressionRatio() != null) {
            form.setCompressionRatio(securityService.sanitizeInput(form.getCompressionRatio()));
        }
        if (form.getCoolingSystem() != null) {
            form.setCoolingSystem(securityService.sanitizeInput(form.getCoolingSystem()));
        }
        if (form.getLubrication() != null) {
            form.setLubrication(securityService.sanitizeInput(form.getLubrication()));
        }
        if (form.getFuelSystem() != null) {
            form.setFuelSystem(securityService.sanitizeInput(form.getFuelSystem()));
        }
        if (form.getEmission() != null) {
            form.setEmission(securityService.sanitizeInput(form.getEmission()));
        }
        if (form.getInduction() != null) {
            form.setInduction(securityService.sanitizeInput(form.getInduction()));
        }
        if (form.getMileage() != null && form.getMileage() < 0) {
            throw new MotorcycleValidationException("Invalid mileage value");
        }
        if (form.getTopSpeed() != null && form.getTopSpeed() < 0) {
            throw new MotorcycleValidationException("Invalid top speed value");
        }
        if (form.getClutch() != null) {
            form.setClutch(securityService.sanitizeInput(form.getClutch()));
        }
        if (form.getTransmissionGearCount() != null) {
            form.setTransmissionGearCount(securityService.sanitizeInput(form.getTransmissionGearCount()));
        }
        if (form.getTransmissionType() != null) {
            form.setTransmissionType(securityService.sanitizeInput(form.getTransmissionType()));
        }
        if (form.getEngineStroke() != null && form.getEngineStroke() < 0) {
            throw new MotorcycleValidationException("Invalid engine stroke value");
        }
        if (form.getEngineCylinder() != null && form.getEngineCylinder() < 0) {
            throw new MotorcycleValidationException("Invalid engine cylinder value");
        }
        if (form.getEngineCamshaft() != null) {
            form.setEngineCamshaft(securityService.sanitizeInput(form.getEngineCamshaft()));
        }
        if (form.getEngineType() != null) {
            form.setEngineType(securityService.sanitizeInput(form.getEngineType()));
        }
        if (form.getEngineCrankAngle() != null) {
            form.setEngineCrankAngle(securityService.sanitizeInput(form.getEngineCrankAngle()));
        }
        if (form.getMaxPowerHp() != null && form.getMaxPowerHp() < 0) {
            throw new MotorcycleValidationException("Invalid max power value");
        }
        if (form.getMaxPowerRpm() != null && form.getMaxPowerRpm() < 0) {
            throw new MotorcycleValidationException("Invalid max power RPM value");
        }
        if (form.getMaxTorqueNm() != null && form.getMaxTorqueNm() < 0) {
            throw new MotorcycleValidationException("Invalid max torque value");
        }
        if (form.getMaxTorqueRpm() != null && form.getMaxTorqueRpm() < 0) {
            throw new MotorcycleValidationException("Invalid max torque RPM value");
        }
        if (form.getClassGrade() != null) {
            form.setClassGrade(securityService.sanitizeInput(form.getClassGrade()));
        }

        securityLogger.logSecurityEvent("FORM_VALIDATION_COMPLETE", "SYSTEM", "Form validated and sanitized successfully");
    }
}