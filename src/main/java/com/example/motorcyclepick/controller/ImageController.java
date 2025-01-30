// 이미지 관련 기능을 처리하는 컨트롤러 패키지
package com.example.motorcyclepick.controller;

// 필요한 의존성들을 임포트

import com.example.motorcyclepick.dto.ImagesDTO;
import com.example.motorcyclepick.exception.DataAccessException;
import com.example.motorcyclepick.exception.DataNotFoundException;
import com.example.motorcyclepick.service.ImageService;
import com.example.motorcyclepick.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 스프링 MVC 컨트롤러 선언
@Controller
// 기본 URL 경로를 /gallery로 지정
@RequestMapping("/gallery")
// Lombok을 사용하여 필수 생성자 자동 생성
@RequiredArgsConstructor
// Lombok을 사용한 로깅 기능 활성화
@Slf4j
// CORS 설정
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {
    // 보안 서비스 의존성 주입
    @Autowired
    private SecurityService securityService;

    // 파일명 유효성 검사 메서드
    private String validateFileName(String fileName) {
        return securityService.sanitizeInput(fileName);
    }

    // 이미지 서비스 의존성 주입
    private final ImageService imageService;

    // 갤러리 메인 페이지 표시 (모든 사용자 접근 가능)
    @GetMapping
    @PreAuthorize("permitAll()")
    public String showGallery(Model model) {
        try {
            // 모든 브랜드 목록 조회 및 살균 처리
            List<String> brands = imageService.getAllBrands()
                    .stream()
                    .map(brand -> securityService.sanitizeInput(brand))
                    .collect(Collectors.toList());
            model.addAttribute("brands", brands);
            return "gallery";
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("갤러리 페이지 로딩 중 오류 발생", e);
            model.addAttribute("error", "갤러리를 불러오는데 실패했습니다");
            return "error";
        }
    }

    // 브랜드 목록 조회 API
    @GetMapping("/api/brands")
    @ResponseBody
    public ResponseEntity<List<String>> getBrands() {
        try {
            // 브랜드 목록 반환
            return ResponseEntity.ok(imageService.getAllBrands());
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("브랜드 목록 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 브랜드별 모델 목록 조회 API
    @GetMapping("/api/models/{brand}")
    @ResponseBody
    public ResponseEntity<List<String>> getModelsByBrand(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand) {
        try {
            // 브랜드명 살균 처리
            String sanitizedBrand = securityService.sanitizeInput(brand);
            // 모델 목록 조회 및 살균 처리
            List<String> models = imageService.getModelsByBrand(sanitizedBrand)
                    .stream()
                    .map(model -> securityService.sanitizeInput(model))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("모델 목록 조회 실패. 브랜드: " + brand, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 브랜드별 이미지 목록 조회 API
    @GetMapping("/api/images/brand/{brand}")
    @ResponseBody
    public ResponseEntity<List<ImagesDTO>> getImagesByBrand(@PathVariable String brand) {
        try {
            // 브랜드별 이미지 목록 조회
            List<ImagesDTO> images = imageService.getImagesByBrand(brand);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("브랜드별 이미지 조회 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 브랜드와 모델별 이미지 목록 조회 API
    @GetMapping("/api/images/{brand}/{model}")
    @ResponseBody
    public ResponseEntity<List<ImagesDTO>> getImagesByBrandAndModel(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand,
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String model) {
        try {
            String sanitizedBrand = securityService.sanitizeInput(brand);
            String sanitizedModel = securityService.sanitizeInput(model);
            log.info("이미지 요청 - 브랜드: {}, 모델: {}", sanitizedBrand, sanitizedModel);

            List<ImagesDTO> images = imageService.getImagesByBrandAndModel(brand, model);

            if (images.isEmpty()) {
                log.warn("이미지를 찾을 수 없음 - 브랜드: {}, 모델: {}", brand, model);
                throw new DataNotFoundException("해당 브랜드와 모델의 이미지를 찾을 수 없습니다");
            }

            log.info("이미지 {}개 찾음", images.size());
            return ResponseEntity.ok(images);

        } catch (DataNotFoundException e) {
            log.warn("이미지를 찾을 수 없음: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            log.error("데이터 액세스 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("이미지 조회 중 예기치 않은 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 브랜드별 갤러리 페이지 표시
    @GetMapping("/{brand}")
    public String showBrandGallery(@PathVariable String brand, Model modelMap) {
        try {
            // 브랜드별 이미지와 모델 목록 조회
            List<ImagesDTO> images = imageService.getImagesByBrand(brand);
            List<String> models = imageService.getModelsByBrand(brand);

            // 모델에 데이터 추가
            modelMap.addAttribute("brand", brand);
            modelMap.addAttribute("models", models);
            modelMap.addAttribute("images", images);
            modelMap.addAttribute("siteTitle", "16Motorbikes - " + brand + " Gallery");

            return "galleryDetail";
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("브랜드 갤러리 페이지 로딩 중 오류 발생: ", e);
            modelMap.addAttribute("error", "갤러리 페이지 로딩 중 오류가 발생했습니다.");
            modelMap.addAttribute("errorMessage", e.getMessage());
            modelMap.addAttribute("errorLocation", "brandGallery");
            return "error";
        }
    }

    // 모델별 갤러리 페이지 표시
    @GetMapping("/{brand}/{model}")
    public String showModelGallery(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand,
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String model,
            Model modelMap) {
        try {
            String sanitizedBrand = securityService.sanitizeInput(brand);
            String sanitizedModel = securityService.sanitizeInput(model);

            List<ImagesDTO> images = imageService.getImagesByBrandAndModel(brand, model);

            if (images.isEmpty()) {
                throw new DataNotFoundException("해당 브랜드와 모델의 이미지를 찾을 수 없습니다");
            }

            modelMap.addAttribute("brand", brand);
            modelMap.addAttribute("model", model);
            modelMap.addAttribute("images", images);
            modelMap.addAttribute("siteTitle", "16Motorbikes - " + brand + " " + model + " Gallery");

            return "galleryDetail";
        } catch (DataNotFoundException e) {
            log.warn("이미지를 찾을 수 없음: {}", e.getMessage());
            modelMap.addAttribute("error", e.getMessage());
            return "error";
        } catch (DataAccessException e) {
            log.error("데이터 액세스 중 오류 발생: ", e);
            modelMap.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다");
            return "error";
        } catch (Exception e) {
            log.error("모델 갤러리 페이지 로딩 중 오류 발생: ", e);
            modelMap.addAttribute("error", "갤러리 페이지 로딩 중 오류가 발생했습니다");
            modelMap.addAttribute("errorLocation", "modelGallery");
            return "error";
        }
    }

    // 최근 이미지 목록 조회 API (페이징 지원)
    @GetMapping("/api/recent")
    @ResponseBody
    public ResponseEntity<List<ImagesDTO>> getRecentImages(
            @RequestParam(defaultValue = "1") @Min(1) @Max(100) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int pageSize) {
        try {
            // 페이징 처리된 최근 이미지 목록 조회
            List<ImagesDTO> images = imageService.getRecentImagesWithPaging(page, pageSize);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("최근 이미지 조회 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 모델 검색 API
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<String>> searchModels(
            @RequestParam @Size(min = 2, max = 50)
            @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$") String query) {
        try {
            // 검색어 살균 처리
            String sanitizedQuery = securityService.sanitizeInput(query);
            if (sanitizedQuery.length() < 2) {
                return ResponseEntity.badRequest().build();
            }

            // 모든 브랜드의 모델 목록 수집
            List<String> allModels = new ArrayList<>();
            List<String> brands = imageService.getAllBrands();

            for (String brand : brands) {
                allModels.addAll(imageService.getModelsByBrand(brand));
            }

            // 검색어로 모델 필터링
            List<String> filteredModels = allModels.stream()
                    .filter(model -> model.toLowerCase().contains(query.toLowerCase()))
                    .distinct()
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredModels);
        } catch (Exception e) {
            // 오류 로깅 및 처리
            log.error("모델 검색 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}