package com.example.motorcyclepick.service;

import com.example.motorcyclepick.config.SecurityLogger;
import com.example.motorcyclepick.dto.ImagesDTO;
import com.example.motorcyclepick.dto.InstagramDTO;
import com.example.motorcyclepick.repository.ImagesMapper;
import com.example.motorcyclepick.utils.ImageConverterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

//import static sun.font.CreatedFontTracker.MAX_FILE_SIZE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImagesMapper imagesMapper;
    private final ImageConverterUtil imageConverterUtil;

    @Autowired
    private SecurityLogger securityLogger;

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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/approve")
    @ResponseBody
    public ResponseEntity<String> approveImage(@RequestParam String fileName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();

        if (fileName == null || fileName.trim().isEmpty()) {
            securityLogger.logSecurityEvent("INVALID_FILE_APPROVE_ATTEMPT", username, remoteAddr);
            return ResponseEntity.badRequest().body("파일명이 누락되었습니다.");
        }

        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension == null || !Arrays.asList(ImageService.ALLOWED_EXTENSIONS).contains("." + extension.toLowerCase())) {
            securityLogger.logSecurityEvent("INVALID_FILE_EXTENSION_ATTEMPT", username, remoteAddr);
            return ResponseEntity.badRequest().body("지원하지 않는 파일 형식입니다.");
        }

        try {
            // DB에서 최종 경로 조회
            String finalPath = imagesMapper.getImagePathByFileName(fileName);
            if (finalPath == null) {
                throw new RuntimeException("Image path not found for file: " + fileName);
            }

            if (finalPath.startsWith("images/")) {
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

            securityLogger.logSecurityEvent("IMAGE_APPROVE_SUCCESS", username, remoteAddr);
            log.info("Image approved and moved from {} to {}", tempFile, destinationFile);

            return ResponseEntity.ok("승인 완료");

        } catch (Exception e) {
            log.error("Failed to approve image", e);
            securityLogger.logSecurityEvent("IMAGE_APPROVE_FAILURE", username, remoteAddr);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("승인 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public List<ImagesDTO> getApprovedImages(String brand, String model) {
        return imagesMapper.getImagesByBrandAndModel(brand, model);
    }

    // 모델에 해당하는 모든 이미지들을 찾아서 반환
    public List<String> getImagesForModel(String brand, String model) {
        try {
            log.info("=== 이미지 검색 시작 ===");
            log.info("브랜드: {}, 모델: {}", brand, model);

            // DB의 기존 경로 형식 유지하면서 검색을 위한 정규화
            String searchTerm = model.replaceAll("\\s+", "") // 공백 제거
                    .replaceAll("[^a-zA-Z0-9]", "") // 특수문자 제거
                    .toLowerCase(); // 소문자 변환

            log.info("검색어: {}", searchTerm);

            Path brandPath = Paths.get(uploadBaseDir, brand);
            log.info("검색 경로: {}", brandPath);

            if (Files.exists(brandPath)) {
                return Files.list(brandPath)
                        .filter(path -> {
                            String fileName = path.getFileName().toString()
                                    .toLowerCase()
                                    .replaceAll("[^a-zA-Z0-9]", "");
                            boolean matches = fileName.contains(searchTerm);
                            log.info("파일 검사: {} -> {}", path.getFileName(), matches);
                            return matches;
                        })
                        .map(path -> "/images/" + brand + "/" + path.getFileName().toString())
                        .collect(Collectors.toList());
            }

            log.warn("브랜드 디렉토리를 찾을 수 없음: {}", brandPath);
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("이미지 검색 중 오류 발생: ", e);
            e.printStackTrace();
            return new ArrayList<>();
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

    public static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png"};


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
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempPath, "*.{jpg,jpeg,png}")) {
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
            securityLogger.logSecurityEvent("EMPTY_FILE_UPLOAD", "SYSTEM", "N/A");
            throw new SecurityException("Empty file detected");
        }

        // 파일 크기 검증 (10MB)
        if (file.getSize() > MAX_FILE_SIZE) {
            securityLogger.logSecurityEvent("FILE_SIZE_EXCEEDED", "SYSTEM", "N/A");
            throw new SecurityException("File size exceeds maximum limit");
        }

        // MIME 타입 검증 강화
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png"))) {
            securityLogger.logSecurityEvent("INVALID_FILE_TYPE", "SYSTEM", "N/A");
            throw new SecurityException("Invalid file type");
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                securityLogger.logSecurityEvent("INVALID_IMAGE_FORMAT", "SYSTEM", "N/A");
                throw new SecurityException("Invalid image format");
            }

            if (image.getWidth() > 4096 || image.getHeight() > 4096) {
                securityLogger.logSecurityEvent("IMAGE_SIZE_EXCEEDED", "SYSTEM", "N/A");
                throw new SecurityException("Image dimensions exceed maximum allowed size");
            }
        } catch (IOException e) {
            securityLogger.logSecurityEvent("IMAGE_PROCESSING_ERROR", "SYSTEM", "N/A");
            throw new SecurityException("Failed to process image file");
        }
    }

    private void sanitizeInputParameters(String brand, String model, String username) {
        // 브랜드와 모델명에 공백 허용
        if (brand == null || model == null ||
                !brand.matches("^[a-zA-Z0-9\\s\\-_]{1,50}$") ||
                !model.matches("^[a-zA-Z0-9\\s\\-_]{1,50}$")) {
            throw new SecurityException("유효하지 않은 입력값입니다");
        }

        // Instagram ID 검증 규칙
        if (username != null && !username.trim().isEmpty() &&
                !username.matches("^@?[a-zA-Z0-9._]{1,50}$")) {
            throw new SecurityException("유효하지 않은 사용자명 형식입니다");
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
                } catch (NumberFormatException ignored) {
                }
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
        try {
            String normalizedPath = path.normalize().toString();
            String normalizedBaseDir = Paths.get(uploadBaseDir).normalize().toString();
            String normalizedTempDir = Paths.get(tempDir).normalize().toString();
            String normalizedTrashDir = Paths.get(trashcanDir).normalize().toString();

            if (!normalizedPath.startsWith(normalizedBaseDir) &&
                    !normalizedPath.startsWith(normalizedTempDir) &&
                    !normalizedPath.startsWith(normalizedTrashDir)) {

                securityLogger.logSecurityEvent(
                        "PATH_TRAVERSAL_ATTEMPT",
                        "SYSTEM",
                        "Attempted path: " + normalizedPath
                );
                throw new SecurityException("유효하지 않은 경로입니다");
            }
        } catch (Exception e) {
            securityLogger.logSecurityEvent("PATH_VALIDATION_ERROR", "SYSTEM", e.getMessage());
            throw new SecurityException("경로 검증 실패");
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

    /**
     * 모든 브랜드 목록 조회
     */
    public List<String> getAllBrands() {
        try {
            return imagesMapper.findAllBrandsFromFiles();
        } catch (Exception e) {
            log.error("브랜드 목록 조회 중 오류 발생: ", e);
            return new ArrayList<>();
        }
    }

    /**
     * 특정 브랜드의 모든 모델 목록 조회
     */
    public List<String> getModelsByBrand(String brand) {
        try {
            if (brand == null || brand.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return imagesMapper.findAllModelsByBrandFromFiles(brand);
        } catch (Exception e) {
            log.error("모델 목록 조회 중 오류 발생: ", e);
            return new ArrayList<>();
        }
    }

    /**
     * 특정 브랜드의 모든 이미지 조회
     */
    public List<ImagesDTO> getImagesByBrand(String brand) {
        try {
            if (brand == null || brand.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return imagesMapper.findAllImagesByBrand(brand);
        } catch (Exception e) {
            log.error("브랜드별 이미지 조회 중 오류 발생 - 브랜드: {}", brand, e);
            return new ArrayList<>();
        }
    }

    /**
     * 특정 브랜드와 모델의 모든 이미지 조회
     */
    public List<ImagesDTO> getImagesByBrandAndModel(String brand, String model) {
        try {
            log.info("이미지 조회 시작 - 브랜드: {}, 모델: {}", brand, model);

            List<ImagesDTO> images = imagesMapper.findAllImagesByBrandAndModel(brand, model);
            log.info("DB에서 조회된 이미지 수: {}", images.size());

            List<ImagesDTO> validImages = new ArrayList<>();

            for (ImagesDTO img : images) {
                String filePath = img.getFilePath();

                // DB에 저장된 경로에서 'images/' 접두어 제거
                if (filePath.startsWith("images/")) {
                    filePath = filePath.substring(7);
                }

                // uploadBaseDir과 결합하여 전체 경로 생성
                Path fullPath = Paths.get(uploadBaseDir, filePath);
                log.info("확인할 전체 경로: {}", fullPath);

                if (Files.exists(fullPath)) {
                    log.info("파일 존재: {}", fullPath);
                    // 웹에서 접근할 때는 images/ 접두어 필요
                    img.setFilePath("images/" + filePath);
                    validImages.add(img);
                } else {
                    log.warn("파일 없음: {}", fullPath);
                }
            }

            log.info("유효한 이미지 수: {}", validImages.size());
            return validImages;

        } catch (Exception e) {
            log.error("이미지 조회 중 오류 발생", e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 이미지 경로가 유효한지 검증
     */
    public boolean isValidImagePath(String brand, String model) {
        try {
            if (brand == null || model == null) {
                return false;
            }
            Path brandPath = Paths.get(uploadBaseDir, brand);
            return Files.exists(brandPath) && !getImagesByBrandAndModel(brand, model).isEmpty();
        } catch (Exception e) {
            log.error("이미지 경로 검증 중 오류 발생 - 브랜드: {}, 모델: {}", brand, model, e);
            return false;
        }
    }

    public List<ImagesDTO> getAllImages() {
        try {
            return imagesMapper.findAllImages();
        } catch (Exception e) {
            log.error("전체 이미지 조회 중 오류 발생: ", e);
            return new ArrayList<>();
        }
    }

    public List<ImagesDTO> getRecentImagesWithPaging(int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;
            return imagesMapper.findRecentImagesWithPaging(pageSize, offset);
        } catch (Exception e) {
            log.error("이미지 페이징 조회 중 오류 발생 - 페이지: {}, 사이즈: {}", page, pageSize, e);
            return new ArrayList<>();
        }
    }

    private List<ImagesDTO> validateAndFilterImages(List<ImagesDTO> images) {
        return images.stream()
                .filter(img -> {
                    if (img.getFilePath() == null) return false;
                    try {
                        Path imagePath = Paths.get(uploadBaseDir, img.getFilePath());
                        return Files.exists(imagePath) && Files.isReadable(imagePath);
                    } catch (Exception e) {
                        log.error("이미지 검증 실패: {}", img.getFilePath(), e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void checkImageDirectory() {
        Path baseDir = Paths.get(uploadBaseDir);
        log.info("이미지 디렉토리 권한 확인: {}", baseDir);
        log.info("디렉토리 존재 여부: {}", Files.exists(baseDir));
        log.info("디렉토리 읽기 권한: {}", Files.isReadable(baseDir));
    }


}
