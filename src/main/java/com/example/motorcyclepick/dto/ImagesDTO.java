package com.example.motorcyclepick.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagesDTO {
    private Integer id;
    private String filePath;
    private Integer instagramID;
    private boolean custom;
    private String instagram_username; // 추가
}