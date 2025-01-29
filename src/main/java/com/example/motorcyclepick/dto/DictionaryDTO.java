package com.example.motorcyclepick.dto;

import com.example.motorcyclepick.domain.DictionaryDomain;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DictionaryDTO {
    private Long id;
    private String term;
    private String definition;
    private String category;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String status;

    public static DictionaryDTO fromDomain(DictionaryDomain domain) {
        DictionaryDTO dto = new DictionaryDTO();
        dto.setId(domain.getId());
        dto.setTerm(domain.getTerm());
        dto.setDefinition(domain.getDefinition());
        dto.setCategory(domain.getCategory());
        dto.setCreatedBy(domain.getCreatedBy());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setUpdatedBy(domain.getUpdatedBy());
        dto.setUpdatedAt(domain.getUpdatedAt());
        dto.setStatus(domain.getStatus());
        return dto;
    }

    public DictionaryDomain toDomain() {
        DictionaryDomain domain = new DictionaryDomain();
        domain.setId(this.id);
        domain.setTerm(this.term);
        domain.setDefinition(this.definition);
        domain.setCategory(this.category);
        domain.setCreatedBy(this.createdBy);
        domain.setCreatedAt(this.createdAt);
        domain.setUpdatedBy(this.updatedBy);
        domain.setUpdatedAt(this.updatedAt);
        domain.setStatus(this.status);
        return domain;
    }
}