package com.example.motorcycle.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageService {

    @Value("${image.upload.directory:src/main/resources/static/images}")
    private String uploadBaseDir;

    private static final String FILE_EXTENSION = ".jpg";
    private static final String NUMBER_FORMAT = "%04d";

    public String saveImage(MultipartFile file, String brand, String model) {
        try {
            // 브랜드 폴더 경로 생성
            Path brandPath = Paths.get(uploadBaseDir, brand);
            createDirectoryIfNotExists(brandPath);

            // 다음 사용 가능한 파일 번호 찾기
            int nextNumber = findNextAvailableNumber(brandPath, model);

            // 파일명 생성 (예: model_0001.jpg)
            String fileName = String.format("%s_%s%s",
                    model,
                    String.format(NUMBER_FORMAT, nextNumber),
                    FILE_EXTENSION);

            // 최종 저장 경로
            Path destinationPath = brandPath.resolve(fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Image saved successfully: {}", destinationPath);
            return fileName;

        } catch (IOException e) {
            log.error("Failed to save image", e);
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    // 모델에 해당하는 모든 이미지들을 찾아서 반환
    public List<String> getImagesForModel(String brand, String model) {
        List<String> images = new ArrayList<>();
        try {
            Path brandPath = Paths.get(uploadBaseDir, brand);
            String prefix = model.trim() + "_";

            log.info("========== 이미지 검색 시작 ==========");
            log.info("검색 브랜드: {}", brand);
            log.info("검색 모델명: {}", model);
            log.info("검색할 파일 접두어: {}", prefix);
            log.info("검색 경로: {}", brandPath.toString());

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(brandPath)) {
                for (Path entry : stream) {
                    String fileName = entry.getFileName().toString();
                    if (fileName.startsWith(prefix) && fileName.endsWith(FILE_EXTENSION)) {
                        images.add(fileName);
                        log.info("찾은 이미지: {}", fileName);
                    } else {
                        log.debug("스킵된 파일: {} (패턴 불일치)", fileName);
                    }
                }
            }

            log.info("검색된 총 이미지 수: {}", images.size());
            if (!images.isEmpty()) {
                log.info("찾은 이미지 목록:");
                images.forEach(img -> log.info(" - {}", img));
            } else {
                log.warn("해당 모델의 이미지를 찾을 수 없습니다.");
            }
            log.info("========== 이미지 검색 완료 ==========");

            return images;

        } catch (IOException e) {
            log.error("이미지 검색 중 오류 발생: {}", e.getMessage());
            log.error("검색 실패 - 브랜드: {}, 모델: {}", brand, model);
            return images;
        }
    }

    private void createDirectoryIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created directory: {}", path);
        }
    }

    private int findNextAvailableNumber(Path brandPath, String model) throws IOException {
        int number = 1;

        // 해당 브랜드 폴더의 모든 파일을 검사
        if (Files.exists(brandPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(brandPath)) {
                // 현재 모델의 최대 번호 찾기
                String prefix = model + "_";
                for (Path entry : stream) {
                    String fileName = entry.getFileName().toString();
                    if (fileName.startsWith(prefix) && fileName.endsWith(FILE_EXTENSION)) {
                        try {
                            // 파일명에서 번호 추출 (예: model_0001.jpg -> 1)
                            String numberStr = fileName.substring(
                                    prefix.length(),
                                    fileName.length() - FILE_EXTENSION.length()
                            );
                            int currentNumber = Integer.parseInt(numberStr);
                            number = Math.max(number, currentNumber + 1);
                        } catch (NumberFormatException e) {
                            log.warn("Invalid file number format: {}", fileName);
                        }
                    }
                }
            }
        }

        return number;
    }
}