package com.example.motorcycle.controller;

import com.example.motorcycle.repository.ImagesMapper;
import com.example.motorcycle.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final ImageService imageService;

    @GetMapping("/imageAdmin")
    public String showImageAdmin(Model model) {
        try {
            List<Map<String, String>> images = imageService.getPendingImages();
            model.addAttribute("images", images);
            return "imageAdmin";
        } catch (Exception e) {
            log.error("이미지 로딩 중 오류 발생", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @PostMapping("/approve")
    @ResponseBody
    public ResponseEntity<String> approveImage(@RequestParam String fileName) {
        try {
            imageService.approveImage(fileName);
            return ResponseEntity.ok("승인 완료");
        } catch (Exception e) {
            log.error("이미지 승인 중 오류: " + fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("승인 실패");
        }
    }

    @PostMapping("/reject")
    @ResponseBody
    public ResponseEntity<String> rejectImage(@RequestParam String fileName) {
        try {
            imageService.rejectImageToTrashCan(fileName);
            return ResponseEntity.ok("거절 완료");
        } catch (Exception e) {
            log.error("이미지 거절 중 오류: " + fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("거절 실패");
        }
    }
}