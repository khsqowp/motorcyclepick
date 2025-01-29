package com.example.motorcyclepick.repository;

import com.example.motorcyclepick.dto.ImagesDTO;
import com.example.motorcyclepick.dto.InstagramDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImagesMapper {

    void insertImage(ImagesDTO imagesDTO);

    void insertInstagram(InstagramDTO instagramDTO);

    String getImagePathByFileName(String fileName);

    List<ImagesDTO> getImagesByBrandAndModel(String brand, String model);

    InstagramDTO findInstagramByUsername(String username);

    List<String> getAllImagePathsByBrandAndModel(String brand, String model);

    List<String> getStoredPathsByFileName(String fileName);

    boolean isValidBrandModel(String brand, String model);

    // 모든 브랜드 조회
    List<String> findAllBrandsFromFiles();

    // 특정 브랜드의 모든 모델 조회
    List<String> findAllModelsByBrandFromFiles(String brand);

    // 특정 브랜드의 모든 이미지 조회
    List<ImagesDTO> findAllImagesByBrand(String brand);

    // 특정 브랜드와 모델의 모든 이미지 조회
    List<ImagesDTO> findAllImagesByBrandAndModel(String brand, String model);

    List<ImagesDTO> findAllImages();

    List<ImagesDTO> findRecentImagesWithPaging(int limit, int offset);

    void deleteImageByPath(String filePath);
}