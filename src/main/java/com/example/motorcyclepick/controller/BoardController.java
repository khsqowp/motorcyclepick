package com.example.motorcyclepick.controller;

import com.example.motorcyclepick.domain.CategoryDomain;
import com.example.motorcyclepick.domain.MotorcycleDomain;
import com.example.motorcyclepick.dto.DictionaryDTO;
import com.example.motorcyclepick.dto.MotorcycleDTO;
import com.example.motorcyclepick.form.BoardForm;
import com.example.motorcyclepick.service.*;
import com.example.motorcyclepick.utils.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
@CrossOrigin(origins = "*", maxAge = 3600)
@SessionAttributes({"boardForm", "results"})
public class BoardController {
    private final BoardService boardService;
    private final ImageService imageService;
    private final DictionaryService dictionaryService;
    private final BoardAnalService boardAnalService;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private FileValidator fileValidator;

    @ModelAttribute("boardForm")
    public BoardForm boardForm() {
        try {
            return new BoardForm();
        } catch (Exception e) {
            log.error("BoardForm 초기화 중 오류 발생: ", e);
            throw new RuntimeException("폼 초기화 실패", e);
        }
    }

    //    _________________________________________________________________________________________________________
    // 시작 페이지
    @GetMapping({"/", "/startPage"})
    public String startPage(Model model) {
        try {
            model.addAttribute("siteTitle", "16Motorbikes");
            model.addAttribute("heroText", "바이크 입문, 기변병 고민 해결을 도와드려요.");
            model.addAttribute("heroDescription", "테스트를 통해 자신에게 맞는 오토바이를 추천받아보세요!");
            return "startPage";
        } catch (Exception e) {
            log.error("시작 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "startPage");
            model.addAttribute("lastProcessedData", "시작 페이지 로딩 시도");
            return "error";
        }
    }


    // 설문조사 페이지
    @GetMapping("/surveyMotorcycle")
    public String showSurveyMotorcycle(
            @ModelAttribute("boardForm") BoardForm boardForm,
            Model model,
            HttpSession session) {
        try {
            log.info("설문조사 시작!");
            session.setAttribute("lastProcessedData", "설문조사 페이지 진입");
            return "surveyMotorcycle";
        } catch (Exception e) {
            log.error("설문조사 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "설문조사 페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "surveyMotorcycle");
            model.addAttribute("errorType", e.getClass().getSimpleName());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error";
        }
    }

    // 결과 페이지
    @PostMapping("/resultPage")
    public String showResultPage(
            @ModelAttribute("boardForm") BoardForm boardForm,
            Model model,
            HttpSession session) {
        if (!isValidSurvey(boardForm)) {
            throw new IllegalArgumentException("Invalid survey data");
        }
        session.setMaxInactiveInterval(1800); // 30분
        try {
            List<MotorcycleDomain> domains = boardService.getRecommendedBikes(boardForm);
            // Domain을 DTO로 변환하여 세션에 저장
            List<MotorcycleDTO> dtos = domains.stream()
                    .map(MotorcycleDTO::fromDomain)
                    .collect(Collectors.toList());

            session.setAttribute("results", dtos);

            if (!dtos.isEmpty()) {
                model.addAttribute("motorcycle", dtos.get(0).toDomain());
            }

            model.addAttribute("currentIndex", 0);
            model.addAttribute("totalResults", dtos.size());

            return "resultPage";
        } catch (Exception e) {
            log.error("결과 페이지 처리 중 오류 발생: ", e);
            return "error";
        }
    }

    // 결과 페이지 네비게이션
    @GetMapping("/results")
    public String navigateResults(
            @RequestParam(defaultValue = "0") int index,
            Model model,
            HttpSession session) {
        if (session.getAttribute("results") == null) {
            return "redirect:/surveyMotorcycle";
        }
        try {
            // List<MotorcycleDomain>을 List<MotorcycleDTO>로 변경
            List<MotorcycleDTO> results = (List<MotorcycleDTO>) session.getAttribute("results");

            if (results == null || results.isEmpty()) {
                throw new IllegalStateException("검색 결과를 찾을 수 없습니다");
            }

            if (index < 0 || index >= results.size()) {
                throw new IllegalArgumentException("유효하지 않은 인덱스입니다");
            }

            // DTO를 Domain으로 변환
            MotorcycleDomain motorcycle = results.get(index).toDomain();

            model.addAttribute("motorcycle", motorcycle);
            model.addAttribute("currentIndex", index);
            model.addAttribute("totalResults", results.size());

            return "resultPage";
        } catch (Exception e) {
            log.error("결과 페이지 네비게이션 중 오류 발생: ", e);
            model.addAttribute("error", "결과 페이지 처리 중 오류가 발생했습니다");
            return "error";
        }
    }

    // 설문조사 유효성 검사
    private boolean isValidSurvey(BoardForm boardForm) {
        return boardForm != null &&
                boardForm.getQuestion1() != null && !boardForm.getQuestion1().isEmpty() &&
                boardForm.getQuestion2() != null && !boardForm.getQuestion2().isEmpty() &&
                boardForm.getQuestion3() != null && !boardForm.getQuestion3().isEmpty() &&
                boardForm.getQuestion4() != null && !boardForm.getQuestion4().isEmpty() &&
                boardForm.getQuestion5() != null && !boardForm.getQuestion5().isEmpty() &&
                boardForm.getQuestion6() != null && !boardForm.getQuestion6().isEmpty() &&
                boardForm.getQuestion7() != null && !boardForm.getQuestion7().isEmpty();
    }

    // 가격 포맷팅
    private String formatPrice(int price) {
        if (price >= 10000) {
            return String.format("%d억원", price / 10000);
        }
        return String.format("%d만원", price);
    }

    // 설문 응답 로깅
    private void logSurveyResponses(BoardForm boardForm) {
        // 메서드 시작 부분에 추가
        String sanitizedData = securityService.sanitizeInput(boardForm.toString());
        log.info("Sanitized survey data: {}", sanitizedData);
        log.info("====== 설문 응답 데이터 ======");
        log.info("투자 가능 비용: {}", formatPrice(Integer.parseInt(boardForm.getQuestion1())));
        log.info("주행 목적: {}", boardForm.getQuestion2());
        log.info("주행 스타일: {}", boardForm.getQuestion3());
        log.info("평균 주행 속도: {}", boardForm.getQuestion4());
        log.info("평균 주행 거리: {}", boardForm.getQuestion5());
        log.info("희망 주행 RPM: {}", boardForm.getQuestion6());
        log.info("선호하는 바이크 외형: {}", boardForm.getQuestion7());
        log.info("==============================");
        log.info("surveyMotorcycle 문항 완료");
    }


    //    ________________________________________________________________________________________________

    @GetMapping("/api/images/{brand}/{model}")
    public List<String> getImages(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand,
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String model) {
        try {
            List<String> images = imageService.getImagesForModel(brand, model);
            log.info("Images for {}/{}: {}", brand, model, images); // 로그 추가
            return images;
        } catch (Exception e) {
            log.error("이미지 로딩 중 오류 발생: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/uploadMotorcycle")
    public String showUploadMotorcycle(Model model) {
        try {
            List<String> brands = boardService.getDistinctBrands();
            model.addAttribute("brands", brands);
            return "uploadMotorcycle";
        } catch (Exception e) {
            log.error("오토바이 자랑하기 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @PostMapping("/api/upload")
    @ResponseBody  // 추가
    public ResponseEntity<?> uploadImage(

            @RequestParam("image") MultipartFile file,
            @RequestParam("brand") String brand,
            @RequestParam("model") String model,
            @RequestParam(value = "instagramId", required = false) String instagramId,
            @RequestParam(value = "isCustom", defaultValue = "false") boolean isCustom) {
        if (!fileValidator.validateFile(file, file.getOriginalFilename())) {
            return ResponseEntity.badRequest().body("Invalid file type or size");
        }

        // 입력값 검증 추가
        brand = securityService.sanitizeInput(brand);
        model = securityService.sanitizeInput(model);
        instagramId = securityService.sanitizeInput(instagramId);

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // 파일 타입 검증
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // 인스타그램 ID 검증
            if (instagramId != null && !instagramId.trim().isEmpty()) {
                if (!instagramId.startsWith("@")) {
                    instagramId = "@" + instagramId;
                }
            }
            // 이미지 저장
            String savedFileName = imageService.saveImage(file, brand, model, instagramId, isCustom);

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("fileName", savedFileName);
            response.put("instagramId", instagramId);
            response.put("isCustom", isCustom);

            log.info("Image upload successful - Brand: {}, Model: {}, Instagram: {}, Custom: {}",
                    brand, model, instagramId, isCustom);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to upload image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/api/models/{brand}")
    public ResponseEntity<List<String>> getModelsByBrand(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$") String brand) {
        try {
            List<String> models = boardService.getModelsByBrand(brand);
            log.info("Brand: {} - Found {} models", brand, models.size());
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            log.error("모델 목록 조회 중 오류 발생: ", e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

    @GetMapping("/dictionary")
    public String showDictionary(Model model) {
        try {
            List<CategoryDomain> categories = dictionaryService.findAllCategoriesForPublic();
            model.addAttribute("categories", categories);
            return "dictionary";
        } catch (Exception e) {
            log.error("사전 페이지 로딩 중 오류 발생: ", e);
            model.addAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorLocation", "dictionary");
            return "error";
        }
    }

    @GetMapping("/api/dictionary/terms/{category}")
    @ResponseBody
    public ResponseEntity<List<DictionaryDTO>> getTermsByCategory(@PathVariable String category) {
        try {
            List<DictionaryDTO> terms = dictionaryService.findByCategoryForPublic(category);
            return ResponseEntity.ok(terms);
        } catch (Exception e) {
            log.error("카테고리별 용어 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/api/dictionary/term/{id}")
    @ResponseBody
    public ResponseEntity<DictionaryDTO> getTermById(@PathVariable @Positive Long id) {
        try {
            DictionaryDTO term = dictionaryService.findByIdForPublic(id);
            if (term == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(term);
        } catch (Exception e) {
            log.error("용어 상세 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/api/dictionary/request")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> requestTerm(@Valid @RequestBody DictionaryDTO dictionaryDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 입력값 검증 및 살균 처리
            if (dictionaryDTO.getTerm() != null) {
                dictionaryDTO.setTerm(securityService.sanitizeInput(dictionaryDTO.getTerm()));
            }
            if (dictionaryDTO.getDefinition() != null) {
                dictionaryDTO.setDefinition(securityService.sanitizeInput(dictionaryDTO.getDefinition()));
            }
            if (dictionaryDTO.getCategory() != null) {
                dictionaryDTO.setCategory(securityService.sanitizeInput(dictionaryDTO.getCategory()));
            }

            // 현재 인증된 사용자 정보 설정
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            dictionaryDTO.setCreatedBy(auth.getName());
            dictionaryDTO.setCreatedAt(LocalDateTime.now());
            dictionaryDTO.setStatus("PENDING");  // 관리자 승인 대기 상태로 설정

            dictionaryService.insertTermRequest(dictionaryDTO);

            // 보안 로깅 추가
            log.info("Dictionary term request created by user: {}, term: {}",
                    auth.getName(), dictionaryDTO.getTerm());

            response.put("success", true);
            response.put("message", "용어 추가 요청이 성공적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Dictionary term request failed for user: {}, error: {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), e.getMessage());
            response.put("success", false);
            response.put("message", "용어 추가 요청 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}