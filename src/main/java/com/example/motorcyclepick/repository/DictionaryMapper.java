package com.example.motorcyclepick.repository;

import com.example.motorcyclepick.domain.CategoryDomain;
import com.example.motorcyclepick.domain.DictionaryDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.Dictionary;
import java.util.List;

@Mapper
public interface DictionaryMapper {
    DictionaryDomain findById(Long id);

    List<DictionaryDomain> findAll();

    List<DictionaryDomain> findByCategory(String category);

    DictionaryDomain findByTerm(String term);

    void insertTerm(DictionaryDomain dictionary);

    void updateTerm(DictionaryDomain dictionary);

    void deleteTerm(Long id);

    List<CategoryDomain> findAllCategories();

    void insertCategory(CategoryDomain categoryDomain);

    void updateCategory(Long categoryId, String categoryName, String description);

    void deleteCategory(Long categoryId);

    List<DictionaryDomain> findByStatus(String status);
}