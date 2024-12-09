//package com.example.motorcycle.controller;
//
//import com.example.motorcycle.service.ImageService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//@Slf4j
//public class ImageUploadController {
//
//    private final ImageService imageService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadImage(
//            @RequestParam("image") MultipartFile file,
//            @RequestParam("brand") String brand,
//            @RequestParam("model") String model) {
//        try {
//            if (file.isEmpty()) {
//                return ResponseEntity.badRequest().body("File is empty");
//            }
//
//            // 파일 타입 검증
//            String contentType = file.getContentType();
//            if (contentType == null || !contentType.startsWith("image/")) {
//                return ResponseEntity.badRequest().body("Only image files are allowed");
//            }
//
//            // 이미지 저장
//            String savedFileName = imageService.saveImage(file, brand, model);
//
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Image uploaded successfully");
//            response.put("fileName", savedFileName);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            log.error("Failed to upload image", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload image: " + e.getMessage());
//        }
//    }
//}