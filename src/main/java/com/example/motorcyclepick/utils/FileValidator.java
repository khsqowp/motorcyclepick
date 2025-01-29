package com.example.motorcyclepick.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class FileValidator {
    @Value("${images.path}")
    private String imagePath;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB (properties에 정의된 값과 동일)
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    public boolean validateFile(MultipartFile file, String filename) {
        if (file != null && file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }

        String extension = StringUtils.getFilenameExtension(filename);
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            return false;
        }

        // 경로 주입 방지
        String sanitizedFilename = new File(filename).getName();
        if (!sanitizedFilename.equals(filename)) {
            return false;
        }

        Path path = Paths.get(imagePath, sanitizedFilename);
        try {
            return !Files.exists(path);
        } catch (SecurityException e) {
            return false;
        }
    }

    public String generateSecureFilename(String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename).toLowerCase();
        return UUID.randomUUID().toString() + "." + extension;
    }
}