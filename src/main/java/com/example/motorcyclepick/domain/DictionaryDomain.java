package com.example.motorcyclepick.domain;

import com.example.motorcyclepick.dto.DictionaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DictionaryDomain {
    private Long id;
    private String term;
    private String definition;
    private String category;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String status;

    public static DictionaryDomain fromDTO(DictionaryDTO dto) {
        DictionaryDomain domain = new DictionaryDomain();
        domain.setId(dto.getId());
        domain.setTerm(dto.getTerm());
        domain.setDefinition(dto.getDefinition());
        domain.setCategory(dto.getCategory());
        domain.setCreatedBy(dto.getCreatedBy());
        domain.setCreatedAt(dto.getCreatedAt());
        domain.setUpdatedBy(dto.getUpdatedBy());
        domain.setUpdatedAt(dto.getUpdatedAt());
        domain.setStatus(dto.getStatus());
        return domain;
    }
}