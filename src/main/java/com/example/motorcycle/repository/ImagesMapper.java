package com.example.motorcycle.repository;

import com.example.motorcycle.dto.ImagesDTO;
import com.example.motorcycle.dto.InstagramDTO;
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

}