package com.example.motorcycle.service;

import com.example.motorcycle.domain.*;
import com.example.motorcycle.dto.*;
import com.example.motorcycle.form.MotorcycleForm;
import com.example.motorcycle.repository.MotorcycleMapper;
import com.example.motorcycle.exception.MotorcycleNotFoundException;
import com.example.motorcycle.exception.MotorcycleValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MotorcycleService {

    private final MotorcycleMapper motorcycleMapper;

    @Autowired
    public MotorcycleService(MotorcycleMapper motorcycleMapper) {
        this.motorcycleMapper = motorcycleMapper;
    }

    //    ___________________________________________________________________________________________________________________________

    public void insertFullMotorcycle(@Valid MotorcycleForm form) {
        try {
            log.info("Starting to insert new motorcycle data");
            validateMotorcycleForm(form);

            MotorcycleDTO motorcycleDTO = form.toDTO();
            MotorcycleDomain motorcycle = MotorcycleDomain.fromDTO(motorcycleDTO);

            motorcycleMapper.insertMotorcycleData(motorcycle);
            Long motorcycleID = motorcycle.getMotorcycleID();

            initializeSubDomains(motorcycle, motorcycleID);
            insertSubDomainData(motorcycle);

            log.info("Successfully inserted motorcycle data with ID: {}", motorcycleID);
        } catch (Exception e) {
            log.error("Error occurred while inserting motorcycle data", e);
            throw new MotorcycleValidationException("Failed to insert motorcycle data: " + e.getMessage());
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
            initializeSubDomains(motorcycle, motorcycleID);
            log.info("After initialization, before update for ID: {}", motorcycleID);
            updateSubDomainData(motorcycle);
            log.info("Successfully updated motorcycle data with ID: {}", motorcycleID);


            log.info("Successfully updated motorcycle data with ID: {}", motorcycleID);
        } catch (MotorcycleNotFoundException e) {
            log.error("Motorcycle not found", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating motorcycle data", e);
            throw new MotorcycleValidationException("Failed to update motorcycle data: " + e.getMessage());
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
            throw new MotorcycleValidationException("Failed to delete motorcycle data: " + e.getMessage());
        }
    }

    //    ___________________________________________________________________________________________________________________________

    @Transactional(readOnly = true)
    public MotorcycleDTO findOneMotorcycle(Long motorcycleID) {
        try {
            log.info("Fetching motorcycle data with ID: {}", motorcycleID);
            Assert.notNull(motorcycleID, "Motorcycle ID cannot be null");

            MotorcycleDomain motorcycle = motorcycleMapper.findByMotorcycleId(motorcycleID);
            if (motorcycle == null) {
                throw new MotorcycleNotFoundException("Motorcycle not found with ID: " + motorcycleID);
            }

            initializeNullDomains(motorcycle);
            MotorcycleDTO dto = MotorcycleDTO.fromDomain(motorcycle);
            initializeNullDTOs(dto);

            log.info("Successfully fetched motorcycle data with ID: {}", motorcycleID);
            return dto;
        } catch (Exception e) {
            log.error("Error occurred while fetching motorcycle data", e);
            throw new MotorcycleValidationException("Failed to fetch motorcycle data: " + e.getMessage());
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

    private void initializeSubDomains(MotorcycleDomain motorcycle, Long motorcycleID) {

    }

    private void insertSubDomainData(MotorcycleDomain motorcycle) {

    }

    private void initializeNullDomains(MotorcycleDomain motorcycle) {

    }

    private void initializeNullDTOs(MotorcycleDTO dto) {
        if (dto.getEnginesDTO() == null) {
            dto.setEnginesDTO(new EnginesDTO());
        }
        if (dto.getDimensionsDTO() == null) {
            dto.setDimensionsDTO(new DimensionsDTO());
        }
        if (dto.getElectronicsDTO() == null) {
            dto.setElectronicsDTO(new ElectronicsDTO());
        }
    }

    //    ___________________________________________________________________________________________________________________________

    private MotorcycleDTO convertToDTO(MotorcycleDomain domain) {
        initializeNullDomains(domain);
        MotorcycleDTO dto = MotorcycleDTO.fromDomain(domain);
        initializeNullDTOs(dto);
        return dto;
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
}