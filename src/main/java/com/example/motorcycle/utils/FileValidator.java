package com.example.motorcycle.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileValidator {
    @Value("${images.path}")
    private String imagePath;

    public boolean exists(String filename) {
        Path path = Paths.get(imagePath, filename);
        return Files.exists(path);
    }
}