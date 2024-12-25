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

//import static sun.font.CreatedFontTracker.MAX_FILE_SIZE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImagesMapper imagesMapper;
    private final ImageConverterUtil imageConverterUtil;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB



    @Value("${images.path}")
    private String uploadBaseDir;

    @Value("${image.temp.directory}")
    private String tempDir;

    @Value("${image.trashcan.directory:${images.path}/TrashCan}")
    private String trashcanDir;


    private static final String FILE_EXTENSION = ".jpg";
    private static final String NUMBER_FORMAT = "%04d";



    @Transactional
    public String saveImage(MultipartFile file, String brand, String model, String username, boolean isCustom) {
        try {
            validateImageSecurity(file);
            sanitizeInputParameters(brand, model, username);

            if (!validateBrandModel(brand, model)) {
                throw new SecurityException("Invalid brand or model");
            }

            String secureFileName = generateSecureFilename(model, brand);
            Path tempPath = Paths.get(tempDir);
            createDirectoryIfNotExists(tempPath);

            Path tempFilePath = tempPath.resolve(secureFileName);
            validatePathTraversal(tempFilePath);

            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            Integer instagramId = processInstagramUsername(username);
            saveImageMetadata(brand, secureFileName, instagramId, isCustom);

            return secureFileName;
        } catch (IOException e) {
            log.error("IO error during image upload", e);
            throw new SecurityException("Security check failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Security violation in image upload", e);
            throw new SecurityException("Security check failed: " + e.getMessage());
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

    private Integer processInstagramUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        try {
            InstagramDTO existingInstagram = imagesMapper.findInstagramByUsername(username);
            if (existingInstagram != null) {
                return existingInstagram.getId();
            }

            InstagramDTO instagramDTO = new InstagramDTO();
            instagramDTO.setUsername(username);
            imagesMapper.insertInstagram(instagramDTO);
            return instagramDTO.getId();
        } catch (Exception e) {
            log.error("Instagram 계정 처리 중 오류 발생", e);
            return null;
        }
    }

    private void saveImageMetadata(String brand, String secureFileName, Integer instagramId, boolean isCustom) {
        ImagesDTO imagesDTO = new ImagesDTO();
        imagesDTO.setFilePath(brand + "/" + secureFileName);
        imagesDTO.setInstagramID(instagramId);
        imagesDTO.setCustom(isCustom);
        imagesMapper.insertImage(imagesDTO);
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
                        String storedPath = storedPaths.get(0);
                        Map<String, String> imageInfo = new HashMap<>();

                        // images/ 접두어 제거
                        if (storedPath.startsWith("images/")) {
                            storedPath = storedPath.substring(7);
                        }

                        // 첫 번째 '/'까지가 브랜드명
                        int brandEndIndex = storedPath.indexOf('/');
                        if (brandEndIndex != -1) {
                            String brand = storedPath.substring(0, brandEndIndex);
                            // 파일명에서 브랜드명과 모델명 추출
                            String model = fileName.substring(0, fileName.lastIndexOf('_'));

                            imageInfo.put("fileName", fileName);
                            imageInfo.put("brand", brand);
                            imageInfo.put("model", model);
                            pendingImages.add(imageInfo);
                        }
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

    private void validateImageSecurity(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new SecurityException("Empty file detected");
        }

        // 파일 크기 검증 (10MB)
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new SecurityException("File size exceeds maximum limit");
        }

        // MIME 타입 검증 강화
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png"))) {
            throw new SecurityException("Invalid file type");
        }

        // 파일명 보안 검증 강화
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.contains("..") ||
                fileName.contains("/") ||
                !fileName.matches("^[a-zA-Z0-9\\-_\\.]{1,100}$")) {
            throw new SecurityException("Invalid filename detected");
        }
    }

    private void sanitizeInputParameters(String brand, String model, String username) {
        if (brand == null || model == null ||
                !brand.matches("^[a-zA-Z0-9\\-_]{1,50}$") ||
                !model.matches("^[a-zA-Z0-9\\-_]{1,50}$")) {
            throw new SecurityException("Invalid input parameters");
        }

        // Instagram ID의 유효성 검사 규칙 완화:
        // 1. @로 시작하는 것은 선택적 (@가 없어도 됨)
        // 2. 영숫자와 밑줄, 마침표를 허용
        // 3. 길이는 1-50자로 제한
        if (username != null && !username.trim().isEmpty() &&
                !username.matches("^@?[a-zA-Z0-9._]{1,50}$")) {
            throw new SecurityException("Invalid username format");
        }
    }

    private boolean validateBrandModel(String brand, String model) {
        if (brand == null || model == null) {
            return false;
        }

        // 기본적인 문자열 검증
        if (!brand.matches("^[a-zA-Z0-9\\s\\-_]{1,50}$") ||
                !model.matches("^[a-zA-Z0-9\\s\\-_]{1,50}$")) {
            return false;
        }

        // 경로 순회 공격 방지
        if (brand.contains("..") || model.contains("..") ||
                brand.contains("/") || model.contains("/")) {
            return false;
        }

        // DB에서 유효성 검증
        return imagesMapper.isValidBrandModel(brand, model);
    }

    private String generateSecureFilename(String model, String brand) {
        try {
            List<String> existingPaths = imagesMapper.getAllImagePathsByBrandAndModel(brand, model);
            int maxNumber = 0;

            for (String path : existingPaths) {
                String fileName = Paths.get(path).getFileName().toString();
                String numberStr = fileName.substring(fileName.lastIndexOf('_') + 1, fileName.lastIndexOf('.'));
                try {
                    int number = Integer.parseInt(numberStr);
                    maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException ignored) {}
            }

            return String.format("%s_%04d%s",
                    model.replaceAll("[^a-zA-Z0-9\\-_]", ""),
                    maxNumber + 1,
                    FILE_EXTENSION);
        } catch (Exception e) {
            log.error("파일명 생성 중 오류 발생", e);
            return model + "_0001" + FILE_EXTENSION;
        }
    }

    private void validatePathTraversal(Path path) {
        String normalizedPath = path.normalize().toString();
        if (!normalizedPath.startsWith(uploadBaseDir) &&
                !normalizedPath.startsWith(tempDir) &&
                !normalizedPath.startsWith(trashcanDir)) {
            throw new SecurityException("Path traversal attempt detected");
        }
    }

    private void validateFileOperations(Path sourcePath, Path targetPath) {
        validatePathTraversal(sourcePath);
        validatePathTraversal(targetPath);

        // 파일 크기 검증
        try {
            if (Files.size(sourcePath) > MAX_FILE_SIZE) {
                throw new SecurityException("File size exceeds maximum limit");
            }
        } catch (IOException e) {
            throw new SecurityException("Unable to validate file size");
        }
    }
}
