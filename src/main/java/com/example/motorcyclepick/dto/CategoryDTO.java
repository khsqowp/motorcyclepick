package com.example.motorcyclepick.dto;

import com.example.motorcyclepick.domain.CategoryDomain;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String categoryName;
    private String description;

    public static CategoryDTO fromDomain(CategoryDomain domain) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(domain.getId());
        dto.setCategoryName(domain.getCategoryName());
        dto.setDescription(domain.getDescription());
        return dto;
    }

    public CategoryDomain toDomain() {
        CategoryDomain domain = new CategoryDomain();
        domain.setId(this.id);
        domain.setCategoryName(this.categoryName);
        domain.setDescription(this.description);
        return domain;
    }
}