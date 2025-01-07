package com.example.motorcycle.domain;

import com.example.motorcycle.dto.CategoryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDomain {
    private Long id;
    private String categoryName;
    private String description;

    public static CategoryDomain fromDTO(CategoryDTO dto) {
        CategoryDomain domain = new CategoryDomain();
        domain.setId(dto.getId());
        domain.setCategoryName(dto.getCategoryName());
        domain.setDescription(dto.getDescription());
        return domain;
    }
}