package com.example.motorcyclepick.controller;

import com.example.motorcyclepick.dto.ImagesDTO;
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
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.github.bucket4j.Bucket;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/gallery")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {
    @Autowired
    private SecurityService securityService;

    // 파일명 검증 로직 추가
    private String validateFileName(String fileName) {
        return securityService.sanitizeInput(fileName);
    }
    private final ImageService imageService;

//__________________________________________________________________________________

    @GetMapping
    @PreAuthorize("permitAll()")
    public String showGallery(Model model) {
        try {
            List<String> brands = imageService.getAllBrands()
                    .stream()
                    .map(brand -> securityService.sanitizeInput(brand))
                    .collect(Collectors.toList());
            model.addAttribute("brands", brands);
            return "gallery";
        } catch (Exception e) {
            log.error("갤러리 페이지 로딩 중 오류 발생", e);
            model.addAttribute("error", "갤러리를 불러오는데 실패했습니다");
            return "error";
        }
    }

    @GetMapping("/api/brands")
    @ResponseBody
    public ResponseEntity<List<String>> getBrands() {
        try {
            return ResponseEntity.ok(imageService.getAllBrands());
        } catch (Exception e) {
            log.error("브랜드 목록 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/models/{brand}")
    @ResponseBody
    public ResponseEntity<List<String>> getModelsByBrand(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand) {
        try {
            String sanitizedBrand = securityService.sanitizeInput(brand);
            List<String> models = imageService.getModelsByBrand(sanitizedBrand)
                    .stream()
                    .map(model -> securityService.sanitizeInput(model))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            log.error("모델 목록 조회 실패. 브랜드: " + brand, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/images/brand/{brand}")
    @ResponseBody
    public ResponseEntity<List<ImagesDTO>> getImagesByBrand(@PathVariable String brand) {
        try {
            List<ImagesDTO> images = imageService.getImagesByBrand(brand);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            log.error("브랜드별 이미지 조회 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

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
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            log.info("이미지 {}개 찾음", images.size());
            return ResponseEntity.ok(images);

        } catch (Exception e) {
            log.error("이미지 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ArrayList<>());
        }
    }

    @GetMapping("/{brand}")
    public String showBrandGallery(@PathVariable String brand, Model modelMap) {
        try {
            List<ImagesDTO> images = imageService.getImagesByBrand(brand);
            List<String> models = imageService.getModelsByBrand(brand);

            modelMap.addAttribute("brand", brand);
            modelMap.addAttribute("models", models);
            modelMap.addAttribute("images", images);
            modelMap.addAttribute("siteTitle", "16Motorbikes - " + brand + " Gallery");

            return "galleryDetail";
        } catch (Exception e) {
            log.error("브랜드 갤러리 페이지 로딩 중 오류 발생: ", e);
            modelMap.addAttribute("error", "갤러리 페이지 로딩 중 오류가 발생했습니다.");
            modelMap.addAttribute("errorMessage", e.getMessage());
            modelMap.addAttribute("errorLocation", "brandGallery");
            return "error";
        }
    }

    @GetMapping("/{brand}/{model}")
    public String showModelGallery(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand,
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String model,
            Model modelMap) {
        try {
            String sanitizedBrand = securityService.sanitizeInput(brand);
            String sanitizedModel = securityService.sanitizeInput(model);
            List<ImagesDTO> images = imageService.getImagesByBrandAndModel(brand, model);

            modelMap.addAttribute("brand", brand);
            modelMap.addAttribute("model", model);
            modelMap.addAttribute("images", images);
            modelMap.addAttribute("siteTitle", "16Motorbikes - " + brand + " " + model + " Gallery");

            return "galleryDetail";
        } catch (Exception e) {
            log.error("모델 갤러리 페이지 로딩 중 오류 발생: ", e);
            modelMap.addAttribute("error", "갤러리 페이지 로딩 중 오류가 발생했습니다.");
            modelMap.addAttribute("errorMessage", e.getMessage());
            modelMap.addAttribute("errorLocation", "modelGallery");
            return "error";
        }
    }

    @GetMapping("/api/recent")
    @ResponseBody
    public ResponseEntity<List<ImagesDTO>> getRecentImages(
            @RequestParam(defaultValue = "1") @Min(1) @Max(100) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int pageSize) {
        try {
            List<ImagesDTO> images = imageService.getRecentImagesWithPaging(page, pageSize);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            log.error("최근 이미지 조회 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<String>> searchModels(
            @RequestParam @Size(min = 2, max = 50)
            @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$") String query) {
        try {
            String sanitizedQuery = securityService.sanitizeInput(query);
            if (sanitizedQuery.length() < 2) {
                return ResponseEntity.badRequest().build();
            }
            List<String> allModels = new ArrayList<>();
            List<String> brands = imageService.getAllBrands();

            for (String brand : brands) {
                allModels.addAll(imageService.getModelsByBrand(brand));
            }

            List<String> filteredModels = allModels.stream()
                    .filter(model -> model.toLowerCase().contains(query.toLowerCase()))
                    .distinct()
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredModels);
        } catch (Exception e) {
            log.error("모델 검색 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}