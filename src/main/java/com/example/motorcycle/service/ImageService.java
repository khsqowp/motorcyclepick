package com.example.motorcycle.service;

import com.example.motorcycle.dto.ImagesDTO;
import com.example.motorcycle.dto.InstagramDTO;
import com.example.motorcycle.repository.ImagesMapper;
import com.example.motorcycle.utils.ImageConverterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImagesMapper imagesMapper;
    private final ImageConverterUtil imageConverterUtil;


    @Value("${image.upload.directory:src/main/resources/static/images}")
    private String uploadBaseDir;

    @Value("${image.temp.directory:${image.upload.directory}/temp}")
    private String tempDir;

    @Value("${image.trashcan.directory:src/main/resources/static/images/TrashCan}")
    private String trashcanDir;


    private static final String FILE_EXTENSION = ".jpg";
    private static final String NUMBER_FORMAT = "%04d";



    @Transactional
    public String saveImage(MultipartFile file, String brand, String model, String username, boolean isCustom) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("Invalid file name");
            }

            MultipartFile jpgFile = imageConverterUtil.convertToJpg(file);

            // 파일 확장자 검증 추가
            validateFileExtension(jpgFile.getOriginalFilename());

            // 다음 사용 가능한 파일 번호 찾기
            int nextNumber = findNextAvailableNumber(Paths.get(uploadBaseDir, brand), model);

            // 파일명 생성 (예: model_0001.jpg)
            String fileName = String.format("%s_%s%s",
                    model,
                    String.format(NUMBER_FORMAT, nextNumber),
                    FILE_EXTENSION);

            // 브랜드 폴더 경로 생성
            Path tempPath = Paths.get(tempDir);
            createDirectoryIfNotExists(tempPath);
            Path tempFilePath = tempPath.resolve(fileName);
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            //Instagram 계정 처리
            Integer instagramId = null;
            if (username != null && !username.trim().isEmpty()) {
                InstagramDTO existingInstagram = imagesMapper.findInstagramByUsername(username);
                if (existingInstagram != null) {
                    instagramId = existingInstagram.getId();
                } else {
                    InstagramDTO instagramDTO = new InstagramDTO();
                    instagramDTO.setUsername(username);
                    imagesMapper.insertInstagram(instagramDTO);
                    instagramId = instagramDTO.getId();
                }
            }

            // DB에 최종 경로로 저장
            ImagesDTO imagesDTO = new ImagesDTO();
            imagesDTO.setFilePath( brand + "/" + fileName);
            imagesDTO.setInstagramID(instagramId);
            imagesDTO.setCustom(isCustom);
            imagesMapper.insertImage(imagesDTO);

            log.info("Image saved to temp: {}", tempFilePath);
            log.info("Final path will be: {}/{}", brand, fileName);

            return fileName;

        } catch (IOException e) {
            log.error("Failed to save image", e);
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    public void approveImage(String fileName) {
        try {
            // DB에서 최종 경로 조회
            String finalPath = imagesMapper.getImagePathByFileName(fileName);
            if (finalPath == null) {
                throw new RuntimeException("Image path not found for file: " + fileName);
            }

            if(finalPath.startsWith("images/")){
                finalPath = finalPath.substring(7);
            }
            // 임시 파일과 최종 경로 설정
            Path tempFile = Paths.get(tempDir, fileName);

            if (!Files.exists(tempFile)) {
                throw new RuntimeException("Original file not found in temp directory: " + fileName);
            }

            Path destinationFile = Paths.get(uploadBaseDir, finalPath);

            // 최종 저장 디렉토리 생성
            createDirectoryIfNotExists(destinationFile.getParent());

            // 파일 이동
            Files.move(tempFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);

            log.info("Image approved and moved from {} to {}", tempFile, destinationFile);

        } catch (IOException e) {
            log.error("Failed to approve image", e);
            throw new RuntimeException("Failed to approve image: " + e.getMessage());
        }
    }

    public List<ImagesDTO> getApprovedImages(String brand, String model) {
        return imagesMapper.getImagesByBrandAndModel(brand, model);
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
        int maxNumber = 0;

        // 데이터베이스에서 현재 파일 경로들을 조회
        List<String> existingPaths = imagesMapper.getAllImagePathsByBrandAndModel(brandPath.getFileName().toString(), model);

        for (String path : existingPaths) {
            try {
                String fileName = Paths.get(path).getFileName().toString();
                String prefix = model + "_";
                if (fileName.startsWith(prefix) && fileName.endsWith(FILE_EXTENSION)) {
                    String numberStr = fileName.substring(
                            prefix.length(),
                            fileName.length() - FILE_EXTENSION.length()
                    );
                    int currentNumber = Integer.parseInt(numberStr);
                    maxNumber = Math.max(maxNumber, currentNumber);
                }
            } catch (Exception e) {
                log.warn("Invalid file number format in path: {}", path);
            }
        }

        return maxNumber + 1;
    }

    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png"};


    private void validateFileExtension(String fileName) {
        if (fileName == null || (!fileName.toLowerCase().endsWith(".jpg") &&
                !fileName.toLowerCase().endsWith(".jpeg") &&
                !fileName.toLowerCase().endsWith(".png") &&
                !fileName.toLowerCase().endsWith(".gif"))) {
            throw new IllegalArgumentException("Unsupported file format. Only jpg, jpeg, png, and gif are allowed");
        }
    }

//    ______________________________________________________________________________________________________________________
// 승인 대기 중인 이미지 정보 조회
public List<Map<String, String>> getPendingImages() {
    List<Map<String, String>> pendingImages = new ArrayList<>();
    try {
        Path tempPath = Paths.get(tempDir);
        if (Files.exists(tempPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempPath, "*.jpg")) {
                for (Path entry : stream) {
                    String fileName = entry.getFileName().toString();
                    List<String> storedPaths = imagesMapper.getStoredPathsByFileName(fileName);

                    if (!storedPaths.isEmpty()) {
                        String storedPath = storedPaths.get(0);  // 첫 번째 경로 사용
                        Map<String, String> imageInfo = new HashMap<>();
                        String[] pathParts = storedPath.split("/");

                        imageInfo.put("fileName", fileName);
                        imageInfo.put("brand", pathParts[1]);
                        imageInfo.put("model", pathParts[2].split("_")[0]);
                        pendingImages.add(imageInfo);
                    }
                }
            }
        }
        return pendingImages;
    } catch (IOException e) {
        log.error("Failed to get pending images", e);
        throw new RuntimeException("Failed to get pending images: " + e.getMessage());
    }
}

    // 이미지 거절 처리
    public void rejectImageToTrashCan(String fileName) {
        try {
            Path tempFile = Paths.get(tempDir, fileName);
            if (!Files.exists(tempFile)) {
                throw new RuntimeException("File not found in temp directory: " + fileName);
            }

            // TrashCan 디렉토리 생성
            Path trashCanPath = Paths.get(trashcanDir);
            createDirectoryIfNotExists(trashCanPath);

            // 파일을 TrashCan으로 이동
            Files.move(tempFile, trashCanPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            log.info("Image rejected and moved to trash: {}", fileName);

        } catch (IOException e) {
            log.error("Failed to reject image", e);
            throw new RuntimeException("Failed to reject image: " + e.getMessage());
        }
    }
}
